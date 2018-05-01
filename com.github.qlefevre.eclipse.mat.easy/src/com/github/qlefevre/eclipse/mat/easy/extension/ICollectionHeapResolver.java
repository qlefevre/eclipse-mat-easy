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

	public long getCollectionHeapSize(IObject object) throws SnapshotException;

	public int getCollectionSize(IObject object) throws SnapshotException;

	public String getSourceCodeReference(IObject object) throws SnapshotException;

}
