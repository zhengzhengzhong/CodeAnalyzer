package cn.letterme.code.analyzer.marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;

import cn.letterme.code.analyzer.param.DeadCodeMarkerParam;

public class DeadCodeMarkerFactory
{
    public static final String ID_UNUSED_CODE_MARKER = "cn.letterme.code.analyzer.marker.deadcode";

    /*
     * Creates a Marker
     */
    public static IMarker createMarker(IResource res, DeadCodeMarkerParam param) throws CoreException
    {
        IFile file = (IFile)res.getAdapter(IFile.class);
        IMarker marker = null;
        // note: you use the id that is defined in your plugin.xml
        marker = file.createMarker(ID_UNUSED_CODE_MARKER);
        marker.setAttribute(IMarker.MESSAGE, param.getMessage());
//        marker.setAttribute(IMarker.LINE_NUMBER, param.getLineNumber());
        marker.setAttribute(IMarker.PRIORITY, param.getPriority());      
        marker.setAttribute(IMarker.SEVERITY, param.getSeverity());
        marker.setAttribute(IMarker.CHAR_START, param.getStart());
        marker.setAttribute(IMarker.CHAR_END, param.getEnd());
        
//        res.findMarker(arg0)
        
        System.out.println("Create marker: " + param);
        return marker;
    }

    /*
     * returns a list of a resources markers
     */
    public static List<IMarker> findMarkers(IResource resource)
    {
        try
        {
            return Arrays.asList(resource.findMarkers(ID_UNUSED_CODE_MARKER, true, IResource.DEPTH_ZERO));
        }
        catch (CoreException e)
        {
            return new ArrayList<IMarker>();
        }
    }

    /*
     * Returns a list of markers that are linked to the resource or any sub
     * resource of the resource
     */
    public static List<IMarker> findAllMarkers(IResource resource)
    {
        try
        {
            return Arrays.asList(resource.findMarkers(ID_UNUSED_CODE_MARKER, true, IResource.DEPTH_INFINITE));
        }
        catch (CoreException e)
        {
            return new ArrayList<IMarker>();
        }
    }

    /*
     * Returns the selection of the package explorer
     */
    public static TreeSelection getTreeSelection()
    {

        ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
                .getSelection();
        if (selection instanceof TreeSelection)
        {
            return (TreeSelection) selection;
        }
        return null;
    }

}
