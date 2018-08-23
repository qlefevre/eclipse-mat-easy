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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.GNU_TROVE_SET_HASH_THASHSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.GNU_TROVE_SET_HASH_TLINKEDHASHSET;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * Trove4JSetHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { GNU_TROVE_SET_HASH_THASHSET, GNU_TROVE_SET_HASH_TLINKEDHASHSET })
public class Trove4JSetHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = (Integer) object.resolveValue("_size");
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_SET;
	}

}
