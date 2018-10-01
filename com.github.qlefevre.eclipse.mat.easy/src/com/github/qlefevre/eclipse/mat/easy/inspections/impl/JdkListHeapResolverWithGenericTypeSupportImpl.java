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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_ARRAYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_ARRAYS_ARRAYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST1;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST2;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IMMUTABLECOLLECTIONS_LISTN;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolverWithGenericTypeSupport;

/**
 * JdkListHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { JAVA_UTIL_ARRAYLIST, JAVA_UTIL_ARRAYS_ARRAYLIST, JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST1,
		JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST2, JAVA_UTIL_IMMUTABLECOLLECTIONS_LISTN })
public class JdkListHeapResolverWithGenericTypeSupportImpl
		extends AbstractCollectionHeapResolverWithGenericTypeSupport {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		int size;
		IObjectArray array;
		String classname = object.getClazz().getName();
		switch (classname) {
		case JAVA_UTIL_ARRAYS_ARRAYLIST:
			array = (IObjectArray) object.resolveValue("a");
			size = array != null ? array.getLength() : -1;
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST1:
			size = 1;
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST2:
			size = 2;
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LISTN:
			array = (IObjectArray) object.resolveValue("elements");
			size = array != null ? array.getLength() : -1;
			break;
		case JAVA_UTIL_ARRAYLIST:
		default:
			Integer resolvedSize = (Integer) object.resolveValue("size");
			size = resolvedSize != null ? resolvedSize : -1;
		}
		return size;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_LIST;
	}

	@Override
	protected Object[] getStorageObjects(IObject object) throws SnapshotException {
		Object[] storageObjects;
		String classname = object.getClazz().getName();
		switch (classname) {
		case JAVA_UTIL_ARRAYLIST:
			storageObjects = new Object[] { object.resolveValue("elementData") };
			break;
		case JAVA_UTIL_ARRAYS_ARRAYLIST:
			storageObjects = new Object[] { object.resolveValue("a") };
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST1:
			storageObjects = new Object[] { object.resolveValue("e0") };
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LIST2:
			storageObjects = new Object[] { object.resolveValue("e0"), object.resolveValue("e1") };
			break;
		case JAVA_UTIL_IMMUTABLECOLLECTIONS_LISTN:
			storageObjects = new Object[] { object.resolveValue("elements") };
			break;
		default:
			storageObjects = null;
			break;
		}
		return storageObjects;
	}

}
