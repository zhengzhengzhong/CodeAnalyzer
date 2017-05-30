package cn.letterme.code.analyzer.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import cn.letterme.code.analyzer.searcher.ProjectSearcher;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class DeadCodeHandler extends AbstractHandler
{
    /**
     * The constructor.
     */
    public DeadCodeHandler()
    {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try
        {
            IRunnableWithProgress runnable = new IRunnableWithProgress()
            {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {

                    // 数字是totalWork
                    monitor.beginTask("处理中……", 100);
                    
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    IWorkspaceRoot root = workspace.getRoot();
                    IProject[] projects = root.getProjects();

                    int total = projects.length;
                    int processed = 0;
                    for (IProject project : projects)
                    {
                        if (monitor.isCanceled())
                        {
                            break;
                        }
                        monitor.worked(100 * processed / total);
                        monitor.subTask(project.getName());
                        try
                        {
                            ProjectSearcher.process(project, monitor);
                        }
                        catch (CoreException e)
                        {
                            e.printStackTrace();
                        }
                        processed++;
                    }
                    monitor.done();
                }
            };
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(
                    PlatformUI.getWorkbench().getDisplay().getActiveShell());
            dialog.setCancelable(false);
            dialog.run(true, true, runnable);
        }
        catch (InvocationTargetException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
