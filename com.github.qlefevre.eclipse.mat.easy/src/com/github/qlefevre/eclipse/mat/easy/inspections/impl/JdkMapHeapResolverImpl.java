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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_CONCURRENT_CONCURRENTHASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_HASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_IDENTITYHASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_LINKEDHASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_TREEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_WEAKHASHMAP;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * 
 * @author Quentin Lefèvre
 *
 */
@Subjects(value = { JAVA_UTIL_HASHMAP, JAVA_UTIL_TREEMAP, JAVA_UTIL_LINKEDHASHMAP,
		JAVA_UTIL_CONCURRENT_CONCURRENTHASHMAP, JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTMAP, JAVA_UTIL_WEAKHASHMAP,
		JAVA_UTIL_IDENTITYHASHMAP, "org.apache.commons.collections.map.LinkedMap" })
public class JdkMapHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "Map<Object,Object> ";
	}

}
