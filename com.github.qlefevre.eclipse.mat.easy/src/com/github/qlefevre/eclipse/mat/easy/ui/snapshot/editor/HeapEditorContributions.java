/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mat.ui.editor.IMultiPaneEditorContributor;
import org.eclipse.mat.ui.editor.MultiPaneEditor;

import com.github.qlefevre.eclipse.mat.easy.actions.ExecuteQueryAction;

@SuppressWarnings("restriction")
public class HeapEditorContributions implements IMultiPaneEditorContributor {

	Action openCollectionTree;

	public void contributeToToolbar(IToolBarManager manager) {
		manager.add(this.openCollectionTree);
	}

	public void dispose() {
	}
	
	public void init(MultiPaneEditor editor) {
		this.openCollectionTree = new ExecuteQueryAction(editor, "collection_tree");
	}
}