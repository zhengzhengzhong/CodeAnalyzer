package cn.letterme.code.analyzer.searcher;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;

import cn.letterme.code.analyzer.Formatter;
import cn.letterme.code.analyzer.Searcher;
import cn.letterme.code.analyzer.marker.DeadCodeMarkerFactory;
import cn.letterme.code.analyzer.param.DeadCodeMarkerParam;

public class FieldSearcher
{
    public static void process(IType type) throws CoreException
    {
        IField[] fieldList = type.getFields();
        for (IField field : fieldList)
        {
            search(field);
        }
    }

    private static void search(IField field) throws CoreException
    {
        Searcher searcher = new Searcher();
        searcher.search(field);
        Set<IMember> callerSet = searcher.getCallers(); 
        
        if (callerSet.isEmpty())
        {
            System.out.println("Field(" + Formatter.format(field) + ") is unused.");
            if (callerSet.isEmpty())
            {
                ISourceRange range = field.getNameRange();
                int start = range.getOffset();
                int length = range.getLength();
                IResource resource = field.getDeclaringType().getResource();
                
                DeadCodeMarkerParam param = new DeadCodeMarkerParam();
                param.setStart(start);
                param.setEnd(start + length);
                param.setMessage("Unused Field");
                
                DeadCodeMarkerFactory.createMarker(resource, param);
                return;
            }
            return;
        }
        
        for (IMember member : callerSet)
        {
            if (member instanceof IMethod)
            {
                System.out.println("Field Usage1>> " + Formatter.format((IMethod)member));
            }
            else
            {
                System.out.println("Field Usage2>> " + member.getHandleIdentifier());
            }
        }
    }
}
