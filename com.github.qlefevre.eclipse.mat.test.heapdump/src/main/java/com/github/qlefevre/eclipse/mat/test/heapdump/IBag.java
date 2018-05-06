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
 * Interface Bag use to retain Heap
 * 
 * @author Quentin Lefèvre
 */
public interface IBag {

	/**
	 * Return Bag contained in this bag
	 * 
	 * @return Bag contained in this bag
	 */
	public IBag getBag();

}
