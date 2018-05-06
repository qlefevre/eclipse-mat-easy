/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;

/**
 * @author Quentin
 *
 */
public final class SnapshotUtil {

	/**
	 * Default constructor
	 */
	private SnapshotUtil() {
	}

	public static String getReferenceName(IObject object) throws SnapshotException {
		String referenceName = "?";
		ISnapshot snapshot = object.getSnapshot();
		int dominatorId = snapshot.getImmediateDominatorId(object.getObjectId());
		if (dominatorId != -1) {
			IObject dominatorObject = snapshot.getObject(dominatorId);

			for (NamedReference ref : dominatorObject.getOutboundReferences()) {
				if (ref.getObjectId() == object.getObjectId()) {
					referenceName = ref.getName();
				}
			}
		}
		return referenceName;
	}
}
