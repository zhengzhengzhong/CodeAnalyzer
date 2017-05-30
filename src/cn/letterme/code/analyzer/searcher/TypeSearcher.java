package cn.letterme.code.analyzer.searcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;

import cn.letterme.code.analyzer.Formatter;
import cn.letterme.code.analyzer.Searcher;
import cn.letterme.code.analyzer.marker.DeadCodeMarkerFactory;
import cn.letterme.code.analyzer.param.DeadCodeMarkerParam;

public class TypeSearcher
{
    public static void process(IType type) throws CoreException
    {
        System.out.println("Searching class: " + Formatter.format(type));
        // process method
        MethodSearcher.process(type);// MethodProcessor.process(type);

        // process field
        FieldSearcher.process(type);

        // process type
        search(type);
    }

    private static boolean isInOtherResources(final IContainer parent, final String keyword) throws CoreException
    {
        final AtomicBoolean isInOtherResources = new AtomicBoolean(false);
        parent.accept(new IResourceVisitor()
        {
            public boolean visit(IResource resource) throws CoreException
            {
                if (resource.equals(parent))
                {
                    // 不包含本身
                    return true;
                }

                if (resource.getType() != IResource.FILE)
                {
                    // 只有文件类型
                    return true;
                }

                IFile file = (IFile) resource;
                if (file.getName().endsWith(".xml") && (file.getName().contains("spring") || file.getName().contains("extension")))
                {
                    // 只读取spring/extension文件
                    String content = getContent(file);

                    if (file.getName().contains("spring"))
                    {
                        System.out.println(content);
                    }
                    if (content.contains(keyword))
                    {
                        isInOtherResources.set(true);
                        return false;
                    }
                }
                
                return true;
            }
        }, IResource.DEPTH_INFINITE, IResource.NONE);
        return isInOtherResources.get();
    }

    private static String getContent(IFile file) throws CoreException
    {
        InputStream is = file.getContents(true);
        int ch = -1;

        StringBuilder sb = new StringBuilder();
        try
        {
            while ((ch = is.read()) != -1)
            {
                sb.append((char) ch);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != is)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        return sb.toString();
    }

    private static void search(IType type) throws CoreException
    {
        Searcher searcher = new Searcher();
        searcher.search(type);
        Set<IMember> callerSet = searcher.getCallers();

        // 移除自身的引用。比如日志
        removeSelfUsage(type, callerSet);

        // 自身没有被代码引用，xml中也没有，则真的没有引用
        if (callerSet.isEmpty() && !isInOtherResources(type.getJavaProject().getProject(), Formatter.format(type)))
        {
            ISourceRange range = type.getNameRange();
            int start = range.getOffset();
            int length = range.getLength();
            IResource resource = type.getResource();

            DeadCodeMarkerParam param = new DeadCodeMarkerParam();
            param.setStart(start);
            param.setEnd(start + length);
            param.setMessage("Unused Class");

            DeadCodeMarkerFactory.createMarker(resource, param);
            return;
        }

        for (IMember member : callerSet)
        {
            if (member instanceof IMethod)
            {
                System.out.println("Class Usage>> " + Formatter.format((IMethod) member));
            }
            else
            {
                System.err.println("Class Usage2>> " + member.getHandleIdentifier());
            }
        }
    }

    private static void removeSelfUsage(IType type, Set<IMember> callerSet)
    {
        if (callerSet.isEmpty())
        {
            return;
        }

        Iterator<IMember> iterator = callerSet.iterator();
        while (iterator.hasNext())
        {
            IMember member = iterator.next();
            if (type.equals(member.getDeclaringType()))
            {
                iterator.remove();
            }
        }
    }
}
