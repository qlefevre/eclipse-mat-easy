/*******************************************************************************
 * Copyright (c) 2010,2017 SAP AG, IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *     IBM Corporation - multiple snapshots in a file
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.github.qlefevre.eclipse.mat.easy.messages"; //$NON-NLS-1$

	public static String Column_ClassName;
	public static String Column_ClassLoaderName;
	public static String Column_Heap;
	public static String Column_Objects;
	public static String Column_Percentage;
	public static String Column_RetainedHeap;
	public static String Column_ShallowHeap;

	public static String CollectionQuery_Group_ByClass;
	public static String CollectionQuery_Group_ByClassLoader;
	public static String CollectionQuery_Group_ByPackage;
	public static String CollectionQuery_Group_None;
	public static String CollectionQuery_LabelAll;
	public static String CollectionQuery_Msg_Grouping;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

}
