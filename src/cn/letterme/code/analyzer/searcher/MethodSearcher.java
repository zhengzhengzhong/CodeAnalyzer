package cn.letterme.code.analyzer.searcher;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;

import cn.letterme.code.analyzer.Formatter;
import cn.letterme.code.analyzer.Searcher;
import cn.letterme.code.analyzer.marker.DeadCodeMarkerFactory;
import cn.letterme.code.analyzer.param.DeadCodeMarkerParam;

public class MethodSearcher
{
    public static void process(IType type) throws CoreException
    {
        IMethod[] methodList = type.getMethods();
        for (IMethod method : methodList)
        {
           search(method);
        }
    }

    private static void search(IMethod method) throws CoreException
    {
        System.out.println(">> Search method: " + Formatter.format(method));

        
        Searcher searcher = new Searcher();
        searcher.search(method);
        Set<IMember> callerSet = searcher.getCallers();
        
        if (callerSet.isEmpty())
        {
            ISourceRange range = method.getNameRange();
            int start = range.getOffset();
            int length = range.getLength();
            IResource resource = method.getDeclaringType().getResource();
            
            DeadCodeMarkerParam param = new DeadCodeMarkerParam();
            param.setStart(start);
            param.setEnd(start + length);
            param.setMessage("Unused method");
            
            DeadCodeMarkerFactory.createMarker(resource, param);
            return;
        }
        
        for (IMember member : callerSet)
        {
            if (!(member instanceof IMethod))
            {
                System.err.println("The member is not instanceof IMethod: " + member.getElementName() + ", class = " + member.getClass().getCanonicalName());
                continue;
            }
            IMethod caller = (IMethod) member;
            System.out.println(">> Caller method: " + Formatter.format(caller));
        }
    }
}
