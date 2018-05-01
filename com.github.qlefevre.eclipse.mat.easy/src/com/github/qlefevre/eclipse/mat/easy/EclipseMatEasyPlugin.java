/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.mat.query.registry.QueryDescriptor;
import org.eclipse.mat.ui.IconLabels;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Quentin
 *
 */
@SuppressWarnings("restriction") 
public class EclipseMatEasyPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.github.qlefevre.eclipse.mat.easy"; //$NON-NLS-1$

	private static final String PREFIX = "$nl$/icons/"; //$NON-NLS-1$
	public static final String COLLECTION_TREE = PREFIX + "collection_tree.gif"; //$NON-NLS-1$
	public static final String LIST = PREFIX + "list.gif"; //$NON-NLS-1$
	public static final String SET = PREFIX + "set.gif"; //$NON-NLS-1$
	public static final String MAP = PREFIX + "map.gif"; //$NON-NLS-1$
	public static final String ARRAY = PREFIX + "array.gif"; //$NON-NLS-1$
	
	private static EclipseMatEasyPlugin plugin;

	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(20);

	// Mappings to permit textual descriptions of Images to be recovered from
	// Images.
	private Map<Image, String> imageTextMap = new HashMap<Image, String>(20);
	private Map<ImageDescriptor, String> descriptorTextMap = new HashMap<ImageDescriptor, String>(20);
	private Map<URI, ImageDescriptor> imagePathCache = new HashMap<URI, ImageDescriptor>(20);

	public static EclipseMatEasyPlugin getDefault() {
		return plugin;
	}

	public static Image getImage(String name) {
		return EclipseMatEasyPlugin.getDefault().getImage(getImageDescriptor(name));
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		// Use singleton instance so that ImageDescriptor can be mapped to text.
		return EclipseMatEasyPlugin.getDefault().getPluginImageDescriptor(path);
	}
	
    public ImageDescriptor getImageDescriptor(QueryDescriptor query)
    {
        URL url = query != null ? query.getIcon() : null;
        return url != null ? getImageDescriptor(url) : null;
    }
    
    public ImageDescriptor getImageDescriptor(URL path)
    {
        // Use URI for maps to avoid blocking equals operation
        URI pathKey;
        try
        {
            pathKey = path.toURI();
        }
        catch (URISyntaxException e)
        {
            // Will cause a missing image to be used instead
            pathKey = null;
        }
        ImageDescriptor descriptor = imagePathCache.get(pathKey);
        if (descriptor == null)
        {
            descriptor = ImageDescriptor.createFromURL(path);
            imagePathCache.put(pathKey, descriptor);
            // Map new descriptor to descriptive text for the Image.
            // Should not cause a memory leak as this is a new descriptor,
            // and equivalent descriptors should overwrite existing entries.
            descriptorTextMap.put(descriptor, getIconString(path));
        }

        return descriptor;
    }

	private ImageDescriptor getPluginImageDescriptor(String path) {
		ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
		if (descriptor != null) { // Add map entry for new descriptor to appropriate text.
									// This should not result in a memory leak, assuming that two
									// equivalent ImageDescriptors match under equals().
									// This is already assumed in the usage of imageCache.
			descriptorTextMap.put(descriptor, getIconString(path));
		}
		return descriptor;
	}

	public Image getImage(ImageDescriptor descriptor) {
		Image image = imageCache.get(descriptor);
		if (image == null && descriptor != null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
			// Map new Image to descriptive text.
			// Should not cause memory leak as this must be a new descriptor.
			imageTextMap.put(image, descriptorTextMap.get(descriptor));
		}
		return image;
	}

	/**
	 * @param path
	 *            String representing the path to an image file for which a
	 *            description is needed.
	 * @return String with meaningful description of image located at input path.
	 *         NLS enabled as the string is obtained from a properties file.
	 */
	private String getIconString(String path) {
		// Construct system independent string representing path below "icons"
		// This is then used to map to a NLS enabled textual description of the
		// image.
		File imageFile = new File(path); // Full path
		String[] iconPath = parseIconPath(imageFile); // Split into elements
		String iconKey = buildIconKey(iconPath); // Construct key for property
		String iconLabel = IconLabels.getString(iconKey); // Obtain NLS value
		return iconLabel;
	}
	
    /**
     * @param url
     *            URL of image file for which a description is required.
     * @return String with meaningful description of image given by input url.
     */
    private String getIconString(URL url)
    {
        // Delegate lookup based on path element of URL.
        return getIconString(url.getPath());
    }
	
	 /**
     * @param imageFile
     *            File representing the path to the image. This is converted
     *            into a String[] by splitting the path into elements below the
     *            /icons directory and stripping off the suffix. Returns null if
     *            the file is not below an /icons directory.
     * @return String[] representing the path split into elements as above.
     */
    private static String[] parseIconPath(File imageFile)
    {
        String[] iconPath = null; // Initial and default value to return.
        ArrayList<String> pathList = new ArrayList<String>(); // Accumulator
        // Strip off file suffix.
        pathList.add(imageFile.getName().split("\\.")[0]); //$NON-NLS-1$

        // Iterate backwards up the path, inserting the directory names at the
        // front of the ArrayList.
        // This results in a sequence matching the original order of the path.
        // Do not include the common parent directory "/icons" or ancestors.
        while (imageFile != null)
        { // iterate up the path
            imageFile = imageFile.getParentFile();
            if (imageFile != null) // There was a parent to include.
            {
                String fileName = imageFile.getName();
                if (fileName.equals("icons")) // Iteration complete. //$NON-NLS-1$
                { // Convert ArrayList to array for return.
                    iconPath = pathList.toArray(new String[0]);
                    imageFile = null; // terminate loop
                }
                else
                { // More to do - prepend the name of parent to sequence.
                    pathList.add(0, fileName); // add parent to front of list
                }
            }
        }
        return iconPath; // Return parsed path, or null if unexpected error.
    }

    /**
     * @param iconPath
     *            String[] representing path to icon file below /icons
     * @return String A mangled version of the path with path separators
     *         replaced with '-' to use as a key into the properties file
     *         containing the textual descriptions of the icons. This utility is
     *         used offline to build the properties file, and at runtime to look
     *         up the icon labels from the NLS properties file(s).
     */
	private static String buildIconKey(String[] iconPath)
    {
        if (iconPath == null)
            return IconLabels.UNKNOWN_ICON_KEY;
        // Initialize key with common prefix from IconLabels class.
        StringBuffer propertyBuf = new StringBuffer(IconLabels.ICON_KEY_PREFIX);
        // Iterate through iconPath appending each element after '-'
        for (String pathStr : iconPath)
        {
            propertyBuf.append('-');
            propertyBuf.append(pathStr);
        }
        return propertyBuf.toString(); // Return constructed key.
    }

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;

		for (Image image : imageCache.values())
			image.dispose();
		imageCache.clear();
		// Clear mappings from Image/Descriptor to descriptive text.
		imageTextMap.clear();
		descriptorTextMap.clear();

		super.stop(context);
	}

}
