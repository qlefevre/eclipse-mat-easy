/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.extension;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.model.IObject;

/**
 * @author Quentin
 *
 */
public interface ICollectionHeapResolver {
	
	public static final byte TYPE_OBJECT = 0;
	public static final byte TYPE_LIST = 1;
	public static final byte TYPE_SET = 2;
	public static final byte TYPE_MAP = 3;
	public static final byte TYPE_ARRAY = 4;
	public static final byte TYPE_STRING = 5;
	public static final byte TYPE_NUMBER = 6;

	public long getCollectionHeapSize(IObject object) throws SnapshotException;

	public int getCollectionSize(IObject object) throws SnapshotException;

	public String getSourceCodeReference(IObject object) throws SnapshotException;
	
	public byte getType(IObject object) throws SnapshotException;

}
