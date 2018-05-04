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
@Subjects(value = { "java.util.HashSet", "java.util.TreeSet", "java.util.LinkedHashSet" })
public class SetHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		IObject map = (IObject) object.resolveValue("map");
		Integer resolvedSize = (Integer) map.resolveValue("size");
		return resolvedSize != null ? resolvedSize : -1;
	}

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_SET;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "Set<Object> ";
	}

}
