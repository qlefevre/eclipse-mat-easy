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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_CURSORABLELINKEDLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_NODECACHINGLINKEDLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS4_LIST_TREELIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_CURSORABLELINKEDLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_NODECACHINGLINKEDLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.ORG_APACHE_COMMONS_COLLECTIONS_LIST_TREELIST;

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
@Subjects(value = { ORG_APACHE_COMMONS_COLLECTIONS_LIST_CURSORABLELINKEDLIST,
		ORG_APACHE_COMMONS_COLLECTIONS_LIST_NODECACHINGLINKEDLIST, ORG_APACHE_COMMONS_COLLECTIONS_LIST_TREELIST,
		ORG_APACHE_COMMONS_COLLECTIONS4_LIST_CURSORABLELINKEDLIST,
		ORG_APACHE_COMMONS_COLLECTIONS4_LIST_NODECACHINGLINKEDLIST, ORG_APACHE_COMMONS_COLLECTIONS4_LIST_TREELIST })
public class ApacheCommonsCollectionsListHeapResolverImpl extends AbstractCollectionHeapResolver
		implements ICollectionHeapResolver {

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_LIST;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "List<Object> ";
	}

}
