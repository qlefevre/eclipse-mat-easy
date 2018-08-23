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

import static com.github.qlefevre.eclipse.mat.easy.inspections.SnapshotUtil.extractGenericType;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.model.IObject;

/**
 * @author Quentin Lefèvre
 *
 */
public abstract class AbstractMapHeapResolverWithGenericTypeSupport extends AbstractCollectionHeapResolver {

	@Override
	final public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		String keyType = extractGenericType(getKeyStorageObjects(object));
		String valueType = extractGenericType(getValueStorageObjects(object));
		String sourceCodeReferencePrefix = "Map<" + keyType + "," + valueType + "> ";
		return sourceCodeReferencePrefix;
	}

	/**
	 * @return objects into which the keys of the Map are stored.
	 */
	protected abstract Object[] getKeyStorageObjects(IObject object) throws SnapshotException;

	/**
	 * @return objects into which the values of the Map are stored.
	 */
	protected abstract Object[] getValueStorageObjects(IObject object) throws SnapshotException;

}
