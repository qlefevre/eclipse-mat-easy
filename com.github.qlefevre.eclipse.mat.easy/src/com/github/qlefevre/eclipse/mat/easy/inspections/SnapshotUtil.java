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

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;
import org.eclipse.mat.snapshot.model.NamedReference;

/**
 * @author Quentin Lefèvre
 */
public final class SnapshotUtil {

	private static final String UNKNOWN_REF_JAVA_LOCAL = "<Java Local>";
	private static final String UNKNOWN_REF = "localVar";

	/**
	 * Default constructor
	 */
	private SnapshotUtil() {
	}

	public static String getReferenceName(IObject object) throws SnapshotException {
		String referenceName = UNKNOWN_REF;
		ISnapshot snapshot = object.getSnapshot();
		int dominatorId = snapshot.getImmediateDominatorId(object.getObjectId());
		if (dominatorId != -1) {
			IObject dominatorObject = snapshot.getObject(dominatorId);

			for (NamedReference ref : dominatorObject.getOutboundReferences()) {
				if (ref.getObjectId() == object.getObjectId()) {
					referenceName = ref.getName();
				}
			}
		}
		if (UNKNOWN_REF_JAVA_LOCAL.equals(referenceName)) {
			referenceName = UNKNOWN_REF;
		}
		return referenceName;
	}

	public static String extractGenericType(Object[] objects) throws SnapshotException {
		String genericType = "Object";
		if (objects != null) {
			if (objects.length == 1 && objects[0] instanceof IObjectArray) {
				IObjectArray objectArray = (IObjectArray) objects[0];
				long references[] = objectArray.getReferenceArray();
				if (references != null && references.length > 0) {
					ISnapshot snapshot = objectArray.getSnapshot();
					String type = snapshot.getObject(snapshot.mapAddressToId(references[0])).getClazz().getName();
					boolean allMatch = Arrays.stream(references).parallel().mapToObj(reference -> {
						try {
							return snapshot.getObject(snapshot.mapAddressToId(reference));
						} catch (SnapshotException e) {
							throw new RuntimeException(e);
						}
					}).map(obj -> obj.getClazz().getName()).filter(clazz -> !"java.lang.ClassLoader".equals(clazz))
							.allMatch(clazz -> type.equals(clazz));
					if (allMatch) {
						genericType = type.substring(type.lastIndexOf('.') + 1);
					}
				}
			} else if (objects.length > 0 && objects[0] != null) {
				String type = ((IObject) objects[0]).getClazz().getName();
				if (Arrays.stream(objects).parallel().filter(obj -> obj != null).map(obj -> (IObject) obj)
						.allMatch(obj -> type.equals(obj.getClazz().getName()))) {
					genericType = type.substring(type.lastIndexOf('.') + 1);
				}
			}
		}
		return genericType;
	}
}
