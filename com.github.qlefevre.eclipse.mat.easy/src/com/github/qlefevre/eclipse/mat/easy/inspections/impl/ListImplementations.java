/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections.impl;

public class ListImplementations {

	public static final String JAVA_UTIL_ARRAYLIST = "java.util.ArrayList";

	public static final String JAVA_UTIL_LINKEDLIST = "java.util.LinkedList";

	public static final String JAVA_UTIL_ARRAYS_ARRAYLIST = "java.util.Arrays$ArrayList";

	public static final String[] IMPLEMENTATIONS = new String[] { JAVA_UTIL_ARRAYLIST, JAVA_UTIL_LINKEDLIST,
			JAVA_UTIL_ARRAYS_ARRAYLIST };

	public static final String JAVA_UTIL_HASHMAP = "java.util.HashMap";

	public static final String JAVA_UTIL_TREEMAP = "java.util.TreeMap";

	public static final String JAVA_UTIL_LINKEDHASHMAP = "java.util.LinkedHashMap";

}
