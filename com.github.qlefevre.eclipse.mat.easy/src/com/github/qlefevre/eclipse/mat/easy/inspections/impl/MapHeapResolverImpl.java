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
@Subjects(value = { "java.util.HashMap", "java.util.TreeMap", "java.util.LinkedHashMap",
		"java.util.concurrent.ConcurrentHashMap", "org.apache.commons.collections.map.LinkedMap" })
public class MapHeapResolverImpl extends AbstractCollectionHeapResolver implements ICollectionHeapResolver {

	@Override
	public byte getType(IObject object) throws SnapshotException {
		return TYPE_MAP;
	}

	@Override
	protected String getSourceCodeReferencePrefix(IObject object) throws SnapshotException {
		return "Map<Object,Object> ";
	}

}
