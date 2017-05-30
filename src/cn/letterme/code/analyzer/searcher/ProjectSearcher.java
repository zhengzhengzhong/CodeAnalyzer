package cn.letterme.code.analyzer.searcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class ProjectSearcher
{

    public static void process(IProject project, IProgressMonitor monitor) throws CoreException
    {
        if (!project.isNatureEnabled("org.eclipse.jdt.core.javanature"))
        {
            return;
        }
        
        IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
        for (IPackageFragment mypackage : packages)
        {
            if (mypackage.getKind() != IPackageFragmentRoot.K_SOURCE)
            {
                continue;
            }

            for (ICompilationUnit unit : mypackage.getCompilationUnits())
            {
                IType[] typeDeclarationList = unit.getTypes();

                for (IType typeDeclaration : typeDeclarationList)
                {
                    if (monitor.isCanceled())
                    {
                        return;
                    }
                    monitor.subTask(typeDeclaration.getFullyQualifiedName());
                    TypeSearcher.process(typeDeclaration);
                }
            }
        }
    }
}
