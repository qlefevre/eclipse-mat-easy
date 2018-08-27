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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.GNU_TROVE_MAP_HASH_THASHMAP;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractMapHeapResolverWithGenericTypeSupport;

/**
 * Trove4JMapHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { GNU_TROVE_MAP_HASH_THASHMAP })
public class Trove4JMapHeapResolverImpl extends AbstractMapHeapResolverWithGenericTypeSupport
		implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = (Integer) object.resolveValue("_size");
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	protected Object[] getKeyStorageObjects(IObject object) throws SnapshotException {
		return new Object[] { object.resolveValue("_set") };
	}

	@Override
	protected Object[] getValueStorageObjects(IObject object) throws SnapshotException {
		return new Object[] { object.resolveValue("_values") };
	}

}
