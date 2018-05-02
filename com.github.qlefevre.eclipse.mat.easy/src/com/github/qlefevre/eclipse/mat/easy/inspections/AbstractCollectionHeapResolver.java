package com.github.qlefevre.eclipse.mat.easy.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;

public abstract class AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		long heap = object.getRetainedHeapSize();
		for (NamedReference ref : object.getOutboundReferences()) {
			heap += ref.getObject().getRetainedHeapSize();
		}
		return heap;
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		Integer resolvedSize = (Integer) object.resolveValue("size");
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public String getSourceCodeReference(IObject object) throws SnapshotException {
		ISnapshot snapshot = object.getSnapshot();
		int dominatorId = snapshot.getImmediateDominatorId(object.getObjectId());
		IObject dominatorObject = snapshot.getObject(dominatorId);
		String referenceName = "?";
		for (NamedReference ref : dominatorObject.getOutboundReferences()) {
			if (ref.getObjectId() == object.getObjectId()) {
				referenceName = ref.getName();
			}
		}
		return getSourceCodeReferencePrefix(object) + referenceName;
	}

	protected abstract String getSourceCodeReferencePrefix(IObject object) throws SnapshotException;

}
