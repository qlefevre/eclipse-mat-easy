/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * @author Quentin Lefèvre
 *
 */
public final class CollectionHeapResolverRegistry implements ICollectionHeapResolver {

	private static final String EXTENSION_POINT_ID = "com.github.qlefevre.eclipse.mat.easy.extension.collectionHeapResolver";

	private static final String STRING_CLASS = "java.lang.String";

	private static final List<String> NUMBER_CLASS = Arrays.asList("java.lang.Byte", //
			"java.lang.Character", //
			"java.lang.Short", //
			"java.lang.Integer", //
			"java.lang.Long", //
			"java.lang.Float", //
			"java.lang.Double");

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
		String classname = object.getClazz().getName();
		ICollectionHeapResolver resolver = resolvers.get(classname);
		if (resolver != null) {
			return resolver.getCollectionHeapSize(object);

		} else {
			return object.getRetainedHeapSize();
		}
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if (object == null)
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
		if (STRING_CLASS.equals(classname)) {
			return TYPE_STRING;
		} else if (NUMBER_CLASS.contains(classname)) {
			return TYPE_NUMBER;
		}
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
		String classname = object.getClazz().getName();
		String name = classname;
		try {
			if (resolvers.containsKey(classname)) {

				name = getSourceCodeReference(object) + " (" + name + ")";

			} else if (STRING_CLASS.equals(classname)) {
				String value = ClassSpecificNameResolverRegistry.resolve(object);
				name = "String " + SnapshotUtil.getReferenceName(object) + " = \"" + value + "\";";
			} else if (NUMBER_CLASS.contains(classname)) {
				String value = ClassSpecificNameResolverRegistry.resolve(object);
				name = classname.replace("java.lang.", "") + " " + SnapshotUtil.getReferenceName(object) + " = " + value
						+ ";";
			} else {

				String value = ClassSpecificNameResolverRegistry.resolve(object);
				if (value != null) {
					name += " - " + value;
				}
			}
		} catch (SnapshotException e) {
			e.printStackTrace();
		}
		return name;
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
