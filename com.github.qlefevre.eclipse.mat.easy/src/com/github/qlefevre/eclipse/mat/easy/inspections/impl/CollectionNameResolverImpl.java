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
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_HASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_LINKEDHASHMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_LINKEDLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.JAVA_UTIL_TREEMAP;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.query.BytesFormat;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.inspections.CollectionHeapResolverRegistry;

/**
 * @author Quentin Lefèvre
 *
 */
@Subjects(value = { JAVA_UTIL_ARRAYLIST, JAVA_UTIL_LINKEDLIST, JAVA_UTIL_ARRAYS_ARRAYLIST, JAVA_UTIL_HASHMAP,
		JAVA_UTIL_TREEMAP, JAVA_UTIL_LINKEDHASHMAP })
public class CollectionNameResolverImpl implements IClassSpecificNameResolver {

	private final CollectionHeapResolverRegistry collectionHeapResolver;

	/**
	 * Default constructor
	 */
	public CollectionNameResolverImpl() {
		collectionHeapResolver = CollectionHeapResolverRegistry.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver#resolve(org.
	 * eclipse.mat.snapshot.model.IObject)
	 */
	public String resolve(IObject object) throws SnapshotException {
		StringBuffer stringBuffer = new StringBuffer(100);
		getCollectionHeapSize(object, stringBuffer);
		getCollectionSize(object, stringBuffer);
		getSourceCodeReference(object, stringBuffer);
		return stringBuffer.toString();
	}

	private void getCollectionHeapSize(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		long heap = collectionHeapResolver.getCollectionHeapSize(object);
		stringBuffer.append("Heap: ");
		stringBuffer.append(BytesFormat.getInstance().format(heap));
	}

	private void getCollectionSize(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		stringBuffer.append(" Size: ");
		int resolvedSize = collectionHeapResolver.getCollectionSize(object);
		if (resolvedSize >= 0) {
			stringBuffer.append(resolvedSize);
		} else {
			stringBuffer.append("?");
		}
	}

	private void getSourceCodeReference(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		stringBuffer.append(" Source: ");
		String referenceName = "?";
		referenceName = collectionHeapResolver.getSourceCodeReference(object);
		stringBuffer.append(referenceName);
	}

}
