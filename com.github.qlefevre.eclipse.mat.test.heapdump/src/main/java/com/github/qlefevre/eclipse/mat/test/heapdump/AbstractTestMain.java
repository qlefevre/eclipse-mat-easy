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

import java.util.Collection;
import java.util.Map;

public abstract class AbstractTestMain {

	protected static IBag createBagsTree(Collection<String> collection) {
		BagB bagB = new BagB(null);
		bagB.setValues(collection);
		IBag bag = new BagA(bagB);
		bag = new BagC(bag);
		bag = new BagA(bag);
		return bag;
	}

	protected static IBag createBagsTree(Map<String, String> map) {
		BagB bagB = new BagB(null);
		bagB.setMap(map);
		IBag bag = new BagA(bagB);
		bag = new BagC(bag);
		bag = new BagA(bag);
		return bag;
	}

}
