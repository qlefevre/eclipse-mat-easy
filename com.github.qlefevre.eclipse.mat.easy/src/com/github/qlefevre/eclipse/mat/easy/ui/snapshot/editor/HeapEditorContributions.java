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