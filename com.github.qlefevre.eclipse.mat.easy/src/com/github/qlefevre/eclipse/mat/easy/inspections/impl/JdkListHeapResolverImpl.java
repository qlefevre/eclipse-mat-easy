/*******************************************************************************
 * Copyright (c) 2018 Quentin Lef�vre and others
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
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_LINKEDLIST;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * 
 * @author Quentin Lef�vre
 *
 */
@Subjects(value = { JAVA_UTIL_ARRAYLIST, JAVA_UTIL_LINKEDLIST, JAVA_UTIL_ARRAYS_ARRAYLIST })
public class JdkListHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		int size = super.getCollectionSize(object);
		if (size == -1) {
			IObjectArray array = (IObjectArray) object.resolveValue("a");
			size = array != null ? array.getLength() : -1;
		}
		return size;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_LIST;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "List<Object> ";
	}

}