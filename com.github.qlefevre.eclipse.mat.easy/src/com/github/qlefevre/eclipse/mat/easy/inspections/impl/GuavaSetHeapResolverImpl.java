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

import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionImplementations.COM_GOOGLE_COMMON_COLLECT_REGULARIMMUTABLESET;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * GuavaSetHeapResolverImpl
 * 
 * @author Quentin Lef�vre
 */
@Subjects(value = { COM_GOOGLE_COMMON_COLLECT_REGULARIMMUTABLESET })
public class GuavaSetHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		IObjectArray array = (IObjectArray) object.resolveValue("elements");
		return array != null ? array.getLength() : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_SET;
	}

}
