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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Main program
 * 
 * Don't forget to add VM arguments in order to generate OutOfMemoryError:
 * -Xmx24m -XX:+HeapDumpOnOutOfMemoryError
 * 
 * @author Quentin Lefèvre
 */
public class OutOfMemoryErrorMain {

	public static void main(String[] args) {

		BagC bagContainer = new BagC(null);
		bagContainer.getBags().add(createBagsTree(new ArrayList<String>()));
		bagContainer.getBags().add(createBagsTree(new LinkedList<String>()));
		
		bagContainer.getBags().add(createBagsTree(new HashSet<String>()));
		bagContainer.getBags().add(createBagsTree(new TreeSet<String>()));
		bagContainer.getBags().add(createBagsTree(new LinkedHashSet<String>()));
		
		bagContainer.getBags().add(createBagsTree(new HashMap<String,String>()));
		bagContainer.getBags().add(createBagsTree(new TreeMap<String,String>()));
		bagContainer.getBags().add(createBagsTree(new LinkedHashMap<String,String>()));
		
		int i = 0;
		while (1 < 2) {
			for (IBag bag : bagContainer.getBags()) {
				((BagB) bag.getBag().getBag().getBag()).getValues().add("OutOfMemoryError soon " + i);
			}
			i++;
		}
	}

	private static IBag createBagsTree(Collection<String> collection) {
		BagB bagB = new BagB(null);
		bagB.setValues(collection);
		IBag bag = new BagA(bagB);
		bag = new BagC(bag);
		bag = new BagA(bag);
		return bag;
	}
	
	private static IBag createBagsTree(Map<String,String> map) {
		BagB bagB = new BagB(null);
		bagB.setMap(map);
		IBag bag = new BagA(bagB);
		bag = new BagC(bag);
		bag = new BagA(bag);
		return bag;
	}

}
