package cn.letterme.code.analyzer.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeAnalyzerPlugin extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "cn.letterme.code.analyzer.plugin";

    // The shared instance
    private static CodeAnalyzerPlugin plugin;

    /**
     * The constructor
     */
    public CodeAnalyzerPlugin()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
     * BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
     * BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    public static Shell getShell()
    {
        return getActiveWorkbenchWindow().getShell();
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow()
    {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static CodeAnalyzerPlugin getDefault()
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
