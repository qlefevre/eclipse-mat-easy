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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_SET_LISTORDEREDSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESORTEDSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_SET_LISTORDEREDSET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESET;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESORTEDSET;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * ApacheCommonsCollectionsListHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { ORG_APACHE_COMMONS_COLLECTIONS_SET_LISTORDEREDSET,
		ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESET, ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESORTEDSET,
		ORG_APACHE_COMMONS_COLLECTIONS4_SET_LISTORDEREDSET, ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESET,
		ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESORTEDSET

})
public class ApacheCommonsCollectionsDecoratorSetHeapResolverImpl extends AbstractCollectionHeapResolver
		implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = null;
		// org.apache.commons.collections.set.AbstractSetDecorator
		IObject set = (IObject) object.resolveValue("collection");
		if (set != null) {
			IObject map = (IObject) set.resolveValue("map");
			// java.util.TreeSet
			if (map == null) {
				map = (IObject) set.resolveValue("m");
			}
			if (map != null) {
				resolvedSize = (Integer) map.resolveValue("size");
			}
		}
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_SET;
	}

}
