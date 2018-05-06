/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.test.heapdump;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Quentin
 *
 */
public class BagC {

	private final List<BagA> bags = new ArrayList<>();

	/**
	 * 
	 */
	public BagC() {
	}

	public List<BagA> getBags() {
		return bags;
	}

}
