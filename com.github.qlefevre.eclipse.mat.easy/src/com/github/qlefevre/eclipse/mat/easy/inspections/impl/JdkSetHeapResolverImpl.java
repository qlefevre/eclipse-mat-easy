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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_HASHSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_LINKEDHASHSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_TREESET;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * JdkSetHeapResolverImpl
 * 
 * @author Quentin Lef�vre
 */
@Subjects(value = { JAVA_UTIL_HASHSET, JAVA_UTIL_TREESET, JAVA_UTIL_LINKEDHASHSET,
		JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTSET })
public class JdkSetHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = null;
		IObject map = (IObject) object.resolveValue("map");
		// java.util.TreeSet
		if (map == null) {
			map = (IObject) object.resolveValue("m");
		}
		if (map != null) {
			resolvedSize = (Integer) map.resolveValue("size");
		}
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_SET;
	}

}
