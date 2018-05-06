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
 * Object BagC use to retain Heap
 * 
 * @author Quentin Lefèvre
 */
public class BagC implements IBag {

	private final List<IBag> bags = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public BagC(IBag bag) {
		if (bag != null)
			bags.add(bag);
	}

	public List<IBag> getBags() {
		return bags;
	}

	@Override
	public IBag getBag() {
		return bags.isEmpty() ? null : bags.get(0);
	}

}
