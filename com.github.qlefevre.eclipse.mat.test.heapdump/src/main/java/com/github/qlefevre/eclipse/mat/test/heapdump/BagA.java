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

/**
 * Object BagA use to retain Heap
 * 
 * @author Quentin Lefèvre
 */
public class BagA implements IBag {

	private final IBag bag;

	private final int h2g2 = 42;

	/**
	 * Default constructor
	 */
	public BagA(IBag bag) {
		this.bag = bag;
	}

	public IBag getBag() {
		return bag;
	}

}
