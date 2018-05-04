/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.inspections.impl;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;
import com.github.qlefevre.eclipse.mat.easy.inspections.AbstractCollectionHeapResolver;

/**
 * 
 * @author Quentin
 *
 */
@Subjects(value = { "java.util.ArrayList", "java.util.LinkedList" })
public class ListHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		long heap = object.getRetainedHeapSize();
		/*
		 * for (NamedReference ref : object.getOutboundReferences()) { heap +=
		 * ref.getObject().getRetainedHeapSize(); }
		 */
		return heap;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_LIST;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "List<Object> ";
	}

}
