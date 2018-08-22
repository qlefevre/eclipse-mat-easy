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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_FIXEDSIZELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_GROWTHLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_LAZYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_SETUNIQUELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_UNMODIFIABLELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_FIXEDSIZELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_GROWTHLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_LAZYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_SETUNIQUELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_UNMODIFIABLELIST;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * ApacheCommonsCollectionsListHeapResolverImpl
 * 
 * @author Quentin Lef�vre
 */
@Subjects(value = { ORG_APACHE_COMMONS_COLLECTIONS_LIST_GROWTHLIST, ORG_APACHE_COMMONS_COLLECTIONS_LIST_FIXEDSIZELIST,
		ORG_APACHE_COMMONS_COLLECTIONS_LIST_LAZYLIST, ORG_APACHE_COMMONS_COLLECTIONS_LIST_SETUNIQUELIST,
		ORG_APACHE_COMMONS_COLLECTIONS_LIST_UNMODIFIABLELIST,

		ORG_APACHE_COMMONS_COLLECTIONS4_LIST_GROWTHLIST, ORG_APACHE_COMMONS_COLLECTIONS4_LIST_FIXEDSIZELIST,
		ORG_APACHE_COMMONS_COLLECTIONS4_LIST_LAZYLIST, ORG_APACHE_COMMONS_COLLECTIONS4_LIST_SETUNIQUELIST,
		ORG_APACHE_COMMONS_COLLECTIONS4_LIST_UNMODIFIABLELIST

})
public class ApacheCommonsCollectionsDecoratorListHeapResolverImpl extends AbstractCollectionHeapResolver
		implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// org.apache.commons.collections.list.AbstractListDecorator
		IObject list = (IObject) object.resolveValue("collection");
		Integer resolvedSize = null;
		if (list != null) {
			resolvedSize = (Integer) list.resolveValue("size");
		}
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_LIST;
	}

}
