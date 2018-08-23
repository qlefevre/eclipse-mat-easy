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

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;

public abstract class AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		long heap = object.getRetainedHeapSize();
		return heap;
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = (Integer) object.resolveValue("size");
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public String getSourceCodeReference(IObject object) throws SnapshotException {
		String referenceName = SnapshotUtil.getReferenceName(object);
		return getSourceCodeReferencePrefix(object) + referenceName;
	}

	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		String sourceCodeReferencePrefix;
		switch (getType(object)) {
		case TYPE_LIST:
			sourceCodeReferencePrefix = "List<Object> ";
			break;
		case TYPE_SET:
			sourceCodeReferencePrefix = "Set<Object> ";
			break;
		case TYPE_MAP:
			sourceCodeReferencePrefix = "Map<Object,Object> ";
			break;
		default:
			sourceCodeReferencePrefix = "";
		}
		return sourceCodeReferencePrefix;
	}

}
