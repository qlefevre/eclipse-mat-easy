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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_ENUMBIMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_ENUMHASHBIMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_HASHBIMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_IMMUTABLESORTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_REGULARIMMUTABLEMAP;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * GuavaMapHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { COM_GOOGLE_COMMON_COLLECT_HASHBIMAP, COM_GOOGLE_COMMON_COLLECT_REGULARIMMUTABLEMAP,
		COM_GOOGLE_COMMON_COLLECT_IMMUTABLESORTEDMAP, COM_GOOGLE_COMMON_COLLECT_ENUMHASHBIMAP,
		COM_GOOGLE_COMMON_COLLECT_ENUMBIMAP })
public class GuavaMapHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = (Integer) object.resolveValue("size");
		// RegularImmutableMap
		if (resolvedSize == null) {
			IObjectArray array = (IObjectArray) object.resolveValue("entries");
			resolvedSize = array != null ? array.getLength() : null;
		}
		// ImmutableSortedMap
		if (resolvedSize == null) {
			IObject valueList = (IObject) object.resolveValue("valueList");
			if (valueList != null) {
				IObjectArray array = (IObjectArray) valueList.resolveValue("array");
				resolvedSize = array != null ? array.getLength() : null;
			}
		}
		// AbstractBiMap
		if (resolvedSize == null) {
			IObject map = (IObject) object.resolveValue("delegate");
			if (map != null) {
				resolvedSize = (Integer) map.resolveValue("size");
			}
		}
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

}
