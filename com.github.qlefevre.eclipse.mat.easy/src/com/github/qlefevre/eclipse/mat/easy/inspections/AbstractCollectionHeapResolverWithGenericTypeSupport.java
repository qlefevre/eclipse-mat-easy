/*******************************************************************************
 * Copyright (c) 2018 Quentin Lef�vre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections;

import static com.github.qlefevre.eclipse.mat.easy.inspections.SnapshotUtil.extractGenericType;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.model.IObject;

/**
 * @author Quentin Lefèvre
 *
 */
public abstract class AbstractCollectionHeapResolverWithGenericTypeSupport extends AbstractCollectionHeapResolver {

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		String sourceCodeReferencePrefix;
		switch (getType(object)) {
		case TYPE_LIST:
			sourceCodeReferencePrefix = "List<" + extractGenericType(getStorageObjects(object)) + "> ";
			break;
		case TYPE_SET:
			sourceCodeReferencePrefix = "Set<" + extractGenericType(getStorageObjects(object)) + "> ";
			break;
		default:
			sourceCodeReferencePrefix = super.getSourceCodeReference(object);
		}
		return sourceCodeReferencePrefix;
	}

	/**
	 * @return objects into which the elements of the Collection are stored.
	 */
	protected abstract Object[] getStorageObjects(IObject object) throws SnapshotException;

}
