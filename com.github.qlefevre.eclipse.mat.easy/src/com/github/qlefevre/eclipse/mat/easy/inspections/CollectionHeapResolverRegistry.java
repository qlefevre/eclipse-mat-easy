/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.inspections;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.registry.ClassSpecificNameResolverRegistry;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;

/**
 * @author Quentin
 *
 */
public final class CollectionHeapResolverRegistry implements ICollectionHeapResolver {

	private static final String EXTENSION_POINT_ID = "com.github.qlefevre.eclipse.mat.easy.extension.collectionHeapResolver";

	private static CollectionHeapResolverRegistry instance;
	private Map<String, ICollectionHeapResolver> resolvers = new HashMap<>();

	private CollectionHeapResolverRegistry() {
	}

	public static synchronized CollectionHeapResolverRegistry getInstance() {
		if (instance == null) {
			instance = new CollectionHeapResolverRegistry();
			instance.initialize();
		}
		return instance;
	}

	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if (object == null)
			return 0;
		if (isCollection(object)) {
			String classname = object.getClazz().getName();
			ICollectionHeapResolver resolver = resolvers.get(classname);
			if (resolver != null) {
				return resolver.getCollectionHeapSize(object);
			}
			return 0;
		} else {
			return object.getRetainedHeapSize();
		}
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if (object == null)
			return -1;
		if (!isCollection(object))
			return -1;
		String classname = object.getClazz().getName();
		ICollectionHeapResolver resolver = resolvers.get(classname);
		if (resolver != null) {
			return resolver.getCollectionSize(object);
		}
		return -1;
	}

	@Override
	public String getSourceCodeReference(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if (object == null) {
			return "";
		}
		String classname = object.getClazz().getName();
		ICollectionHeapResolver resolver = resolvers.get(classname);
		if (resolver != null) {
			return resolver.getSourceCodeReference(object);
		}
		return "";
	}

	public byte getType(IObject object) {
		if (object == null)
			return TYPE_OBJECT;
		if (object.getClazz().isArrayType())
			return TYPE_ARRAY;
		String classname = object.getClazz().getName();
		ICollectionHeapResolver resolver = resolvers.get(classname);
		if (resolver != null) {
			try {
				return resolver.getType(object);
			} catch (SnapshotException e) {
			}
		}
		return TYPE_OBJECT;
	}

	public String getDisplayName(IObject object) {
		String name = object.getClazz().getName();
		if (isCollection(object)) {
			try {
				name = getSourceCodeReference(object) + " (" + name + ")";
			} catch (SnapshotException e) {
			}
		} else {
			String value = ClassSpecificNameResolverRegistry.resolve(object);
			if (value != null) {
				name += " - " + value;
			}
		}
		return name;
	}

	private boolean isCollection(IObject object) {
		try {
			String classname = object.getClazz().getName();
			return resolvers.containsKey(classname);
		} catch (Exception vEx) {
			return false;
		}
	}

	private String[] extractSubjects(ICollectionHeapResolver instance) {
		Subjects subjects = instance.getClass().getAnnotation(Subjects.class);
		if (subjects != null)
			return subjects.value();

		Subject subject = instance.getClass().getAnnotation(Subject.class);
		return subject != null ? new String[] { subject.value() } : null;
	}

	private void initialize() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(EXTENSION_POINT_ID);
		for (IConfigurationElement elem : elements) {
			ICollectionHeapResolver resolver;
			try {
				resolver = (ICollectionHeapResolver) elem.createExecutableExtension("impl");
				String[] subjects = extractSubjects(resolver);
				if (subjects != null && subjects.length > 0) {
					for (int ii = 0; ii < subjects.length; ii++)
						resolvers.put(subjects[ii], resolver);
				}
			} catch (CoreException e) {
				// TODO logger
				e.printStackTrace();
			}

		}
	}

}
