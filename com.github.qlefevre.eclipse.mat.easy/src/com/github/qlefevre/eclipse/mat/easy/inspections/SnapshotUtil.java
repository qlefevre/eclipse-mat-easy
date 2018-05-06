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

	private static final String UNKNOWN_REF_JAVA_LOCAL = "<Java Local>";
	private static final String UNKNOWN_REF = "var?";
	
	/**
	 * Default constructor
	 */
	private SnapshotUtil() {
	}

	public static String getReferenceName(IObject object) throws SnapshotException {
		String referenceName = UNKNOWN_REF;
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
		if(UNKNOWN_REF_JAVA_LOCAL.equals(referenceName)) {
			referenceName = UNKNOWN_REF;
		}
		return referenceName;
	}
}
