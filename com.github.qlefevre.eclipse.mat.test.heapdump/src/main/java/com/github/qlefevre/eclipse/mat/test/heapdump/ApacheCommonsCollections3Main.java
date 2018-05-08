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

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections.list.GrowthList;
import org.apache.commons.collections.list.NodeCachingLinkedList;
import org.apache.commons.collections.list.TreeList;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.collections.map.ReferenceIdentityMap;
import org.apache.commons.collections.map.StaticBucketMap;
import org.apache.commons.collections.set.ListOrderedSet;

/**
 * Main program
 * 
 * Don't forget to add VM arguments in order to generate OutOfMemoryError:
 * -Xmx24m -XX:+HeapDumpOnOutOfMemoryError
 * 
 * @author Quentin Lefèvre
 */
public class ApacheCommonsCollections3Main extends AbstractTestMain {

	public static void main(String[] args) {

		BagC bagContainer = new BagC(null);
		bagContainer.getBags().add(createBagsTree(new CursorableLinkedList()));
		bagContainer.getBags().add(createBagsTree(new GrowthList()));
		bagContainer.getBags().add(createBagsTree(new NodeCachingLinkedList()));
		bagContainer.getBags().add(createBagsTree(new TreeList()));

		bagContainer.getBags().add(createBagsTree(ListOrderedSet.decorate(new HashSet())));

		bagContainer.getBags().add(createBagsTree(new Flat3Map()));
		bagContainer.getBags().add(createBagsTree(new LRUMap()));
		bagContainer.getBags().add(createBagsTree(new ReferenceIdentityMap()));
		bagContainer.getBags().add(createBagsTree(new StaticBucketMap()));

		bagContainer.getBags().add(createBagsTree(ListOrderedMap.decorate(new HashMap())));

		int i = 0;
		while (1 < 2) {
			for (IBag bag : bagContainer.getBags()) {
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
