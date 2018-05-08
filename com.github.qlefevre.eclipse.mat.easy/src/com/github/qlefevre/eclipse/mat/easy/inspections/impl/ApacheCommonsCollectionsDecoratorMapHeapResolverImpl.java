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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_DEFAULTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZESORTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LAZYMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LISTORDEREDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_MULTIVALUEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_PREDICATEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_TRANSFORMEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEORDEREDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLESORTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_DEFAULTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZESORTEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_LAZYMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_LISTORDEREDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_MULTIVALUEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_PREDICATEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_TRANSFORMEDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEORDEREDMAP;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLESORTEDMAP;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * ApacheCommonsCollectionsMapHeapResolverImpl
 * 
 * @author Quentin Lefèvre
 */
@Subjects(value = { ORG_APACHE_COMMONS_COLLECTIONS_MAP_DEFAULTEDMAP, ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZEMAP,
		ORG_APACHE_COMMONS_COLLECTIONS_MAP_LAZYMAP, ORG_APACHE_COMMONS_COLLECTIONS_MAP_LISTORDEREDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS_MAP_MULTIVALUEMAP, ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEMAP,
		ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZESORTEDMAP, ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLESORTEDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEORDEREDMAP, ORG_APACHE_COMMONS_COLLECTIONS_MAP_TRANSFORMEDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS_MAP_PREDICATEDMAP,

		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_DEFAULTEDMAP, ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZEMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LAZYMAP, ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LISTORDEREDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_MULTIVALUEMAP, ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZESORTEDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLESORTEDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEORDEREDMAP, ORG_APACHE_COMMONS_COLLECTIONS4_MAP_TRANSFORMEDMAP,
		ORG_APACHE_COMMONS_COLLECTIONS4_MAP_PREDICATEDMAP })
public class ApacheCommonsCollectionsDecoratorMapHeapResolverImpl extends AbstractCollectionHeapResolver
		implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// org.apache.commons.collections.list.AbstractMapDecorator
		IObject map = (IObject) object.resolveValue("map");
		Integer resolvedSize = null;
		if (map != null) {
			resolvedSize = (Integer) map.resolveValue("size");
		}
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "Map<Object,Object> ";
	}

}
