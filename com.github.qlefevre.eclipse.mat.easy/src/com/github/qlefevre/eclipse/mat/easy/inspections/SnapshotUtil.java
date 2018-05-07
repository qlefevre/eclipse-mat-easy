/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;

/**
 * @author Quentin Lefèvre
 *
 */
public final class SnapshotUtil {

	private static final String UNKNOWN_REF_JAVA_LOCAL = "<Java Local>";
	private static final String UNKNOWN_REF = "localVar";

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
		if (UNKNOWN_REF_JAVA_LOCAL.equals(referenceName)) {
			referenceName = UNKNOWN_REF;
		}
		return referenceName;
	}
}
