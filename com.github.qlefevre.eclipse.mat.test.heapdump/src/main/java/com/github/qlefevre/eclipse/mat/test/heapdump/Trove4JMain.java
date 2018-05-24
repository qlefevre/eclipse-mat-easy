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

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;

/**
 * Main program
 * 
 * Don't forget to add VM arguments in order to generate OutOfMemoryError:
 * -Xmx24m -XX:+HeapDumpOnOutOfMemoryError
 * 
 * @author Quentin Lefèvre
 */
public class Trove4JMain extends AbstractTestMain {

	public static void main(String[] args) {

		BagC bagContainer = new BagC(null);
		bagContainer.getBags().add(createBagsTree(new THashSet<>()));
		bagContainer.getBags().add(createBagsTree(new TLinkedHashSet<>()));
		bagContainer.getBags().add(createBagsTree(new THashMap<>()));
		THashSet<String> set = new THashSet<>();
		int i = 0;
		while (1 < 2) {
			for (IBag bag : bagContainer.getBags()) {
				set.add("OutOfMemoryError soon " + i);
				if (((BagB) bag.getBag().getBag().getBag()).getValues() != null) {
					((BagB) bag.getBag().getBag().getBag()).getValues().add("OutOfMemoryError soon " + i);
				} else {
					((BagB) bag.getBag().getBag().getBag()).getMap().put("OutOfMemoryError soon " + i,
							"OutOfMemoryError soon " + i);
				}
			}
			i++;
		}
	}

}
