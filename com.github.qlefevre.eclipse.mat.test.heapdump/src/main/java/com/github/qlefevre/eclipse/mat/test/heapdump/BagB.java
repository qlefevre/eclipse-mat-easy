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

/**
 * Object BagB use to retain Heap
 * 
 * @author Quentin Lefèvre
 */
public class BagB implements IBag {

	/**
	 * String values
	 */
	private Collection<String> values;

	private final IBag bag;

	/**
	 * Default constructor
	 */
	public BagB(IBag bag) {
		this.bag = bag;
	}

	public Collection<String> getValues() {
		return values;
	}

	public void setValues(Collection<String> values) {
		this.values = values;
	}

	@Override
	public IBag getBag() {
		return bag;
	}

}
