package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery.Tree;

public class CollectionTreeContentProvider implements ITreeContentProvider {

	private CollectionPane2 pane;

	public CollectionTreeContentProvider(CollectionPane2 pane) {
		this.pane = pane;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public Object[] getChildren(Object arg0) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = (List<Object>) tree.getChildren(arg0);
		Object[] children = nodes.stream().filter(node -> {
			return ((double) tree.getColumnValue(node, 3)) > 0.005;
		}).toArray();
		return children;
	}

	@Override
	public Object[] getElements(Object arg0) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = ((List<Object>) arg0);
		Object[] children = nodes.stream().filter(node -> {
			return ((double) tree.getColumnValue(node, 3)) > 0.005;
		}).toArray();
		return children;
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = (List<Object>) tree.getChildren(arg0);
		boolean hasChildren = nodes.stream().anyMatch(node -> {
			return ((double) tree.getColumnValue(node, 3)) > 0.01;
		});
		return hasChildren;
	}

}
