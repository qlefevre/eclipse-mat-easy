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

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;

/**
 * Main program
 * 
 * Don't forget to add VM arguments in order to generate OutOfMemoryError:
 * -Xmx24m -XX:+HeapDumpOnOutOfMemoryError
 * 
 * @author Quentin Lefèvre
 */
public class GuavaMain extends AbstractTestMain {

	public static void main(String[] args) {

		ImmutableList.Builder<String> build0 = ImmutableList.builder();
		ImmutableSet.Builder<String> build1 = ImmutableSet.builder();
		ImmutableMap.Builder<String, String> build2 = ImmutableMap.builder();

		for (int i = 0; i < 25000; i++) {
			build0.add("OutOfMemoryError soon " + i);
			build1.add("OutOfMemoryError soon " + i);
			build2.put("OutOfMemoryError soon " + i, "OutOfMemoryError soon " + i);
		}

		BagC bagContainer = new BagC(null);
		bagContainer.getBags().add(createBagsTree(build0.build()));
		bagContainer.getBags().add(createBagsTree(build1.build()));
		ImmutableMap<String, String> map = build2.build();
		bagContainer.getBags().add(createBagsTree(map));
		bagContainer.getBags().add(createBagsTree(ImmutableSortedMap.copyOf(map)));
		bagContainer.getBags().add(createBagsTree(HashBiMap.create()));

		bagContainer.getBags().add(createBagsTree(new ArrayList<>()));

		int i = 0;
		while (1 < 2) {
			for (IBag bag : bagContainer.getBags()) {
				try {
					if (((BagB) bag.getBag().getBag().getBag()).getValues() != null) {
						((BagB) bag.getBag().getBag().getBag()).getValues().add("OutOfMemoryError soon " + i);
					} else {
						((BagB) bag.getBag().getBag().getBag()).getMap().put("OutOfMemoryError soon " + i,
								"OutOfMemoryError soon " + i);
					}
				} catch (Exception e) {
				}
			}
			i++;
		}
	}

}
