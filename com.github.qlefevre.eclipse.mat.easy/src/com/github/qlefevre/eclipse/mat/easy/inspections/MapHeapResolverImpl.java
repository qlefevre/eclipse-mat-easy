/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;

/**
 * 
 * @author Quentin
 *
 */
@Subjects(value= {"java.util.HashMap","java.util.TreeMap","java.util.LinkedHashMap"})
public class MapHeapResolverImpl implements ICollectionHeapResolver{

	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		long heap = object.getRetainedHeapSize();
		for(NamedReference ref : object.getOutboundReferences()) {
			heap += ref.getObject().getRetainedHeapSize();
		}
		return heap;
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		int resolvedSize = -1;
		String classname = object.getClazz().getName();
		switch(classname) {
			default:
					resolvedSize= (Integer) object.resolveValue("size");	
		}
		return resolvedSize;
	}

	@Override
	public String getSourceCodeReference(IObject object) throws SnapshotException {
		ISnapshot snapshot = object.getSnapshot();
		int dominatorId = snapshot.getImmediateDominatorId(object.getObjectId());
		IObject dominatorObject = snapshot.getObject(dominatorId);
		String referenceName = "?";
		for(NamedReference ref : dominatorObject.getOutboundReferences()) {
			if(ref.getObjectId() == object.getObjectId()) {
				referenceName = ref.getName();
			}
		}
		return "Map<Object,Object> "+referenceName;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

}
