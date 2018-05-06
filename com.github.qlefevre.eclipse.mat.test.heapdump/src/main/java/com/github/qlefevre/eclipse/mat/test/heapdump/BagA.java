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
 * @author Quentin
 *
 */
public class BagA {

	private final BagB bagB = new BagB();

	/**
	 * 
	 */
	public BagA() {
	}

	public BagB getBagB() {
		return bagB;
	}

}
