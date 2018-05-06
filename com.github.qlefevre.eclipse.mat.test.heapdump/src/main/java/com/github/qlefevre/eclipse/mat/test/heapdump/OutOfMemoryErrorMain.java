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
import java.util.HashSet;
import java.util.LinkedList;

public class OutOfMemoryErrorMain {

	public static void main(String[] args) {

		BagA bagA1 = new BagA();
		bagA1.getBagB().setValues(new ArrayList<String>());
		BagA bagA2 = new BagA();
		bagA2.getBagB().setValues(new LinkedList<String>());
		BagA bagA3 = new BagA();
		bagA3.getBagB().setValues(new HashSet<String>());

		BagC bagC = new BagC();
		bagC.getBags().add(bagA1);
		bagC.getBags().add(bagA2);
		bagC.getBags().add(bagA3);

		int i = 0;
		while (1 < 2) {
			bagA1.getBagB().getValues().add("OutOfMemoryError soon");
			bagA2.getBagB().getValues().add("OutOfMemoryError soon");
			bagA3.getBagB().getValues().add("OutOfMemoryError soon " + i);
			i++;
		}
	}

}
