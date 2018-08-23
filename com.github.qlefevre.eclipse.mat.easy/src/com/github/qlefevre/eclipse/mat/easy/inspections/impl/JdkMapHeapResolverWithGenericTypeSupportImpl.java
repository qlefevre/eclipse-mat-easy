/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections.impl;

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IMMUTABLECOLLECTIONS_MAP1;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IMMUTABLECOLLECTIONS_MAPN;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractMapHeapResolverWithGenericTypeSupport;

/**
 * JdkMapHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { JAVA_UTIL_IMMUTABLECOLLECTIONS_MAP1, JAVA_UTIL_IMMUTABLECOLLECTIONS_MAPN })
public class JdkMapHeapResolverWithGenericTypeSupportImpl extends AbstractMapHeapResolverWithGenericTypeSupport
		implements ICollectionHeapResolver {

	@Override
	protected Object[] getKeyStorageObjects(IObject object) throws SnapshotException {
		Object[] storageObjects;
		String classname = object.getClazz().getName();
		switch (classname) {
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_MAP1:
			storageObjects = new Object[] { object.resolveValue("k0") };
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_MAPN:
			IObjectArray objectArray = (IObjectArray) object.resolveValue("table");
			ISnapshot snapshot = objectArray.getSnapshot();
			long[] references = objectArray.getReferenceArray();
			storageObjects = new Object[] { snapshot.getObject(snapshot.mapAddressToId(references[0])) };
			break;
		default:
			storageObjects = null;
			break;
		}
		return storageObjects;
	}

	@Override
	protected Object[] getValueStorageObjects(IObject object) throws SnapshotException {
		Object[] storageObjects;
		String classname = object.getClazz().getName();
		switch (classname) {
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_MAP1:
			storageObjects = new Object[] { object.resolveValue("v0") };
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_MAPN:
			IObjectArray objectArray = (IObjectArray) object.resolveValue("table");
			ISnapshot snapshot = objectArray.getSnapshot();
			long[] references = objectArray.getReferenceArray();
			storageObjects = new Object[] { snapshot.getObject(snapshot.mapAddressToId(references[1])) };
			break;
		default:
			storageObjects = null;
			break;
		}
		return storageObjects;
	}

}
