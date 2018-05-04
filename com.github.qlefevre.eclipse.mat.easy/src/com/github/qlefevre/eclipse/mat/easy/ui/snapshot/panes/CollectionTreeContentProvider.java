package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery.Tree;

public class CollectionTreeContentProvider implements ITreeContentProvider {

	private static final int CHILDREN_LIMIT = 10;

	private static final double MAX_NODE_RETAINEDHEAP_PERCENTAGE = 0.01;

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
		// Filter by percentage
		Object[] children = nodes.stream().filter(predicate(tree)).toArray();
		// No children ?
		if (children.length == 0) {
			children = nodes.stream().limit(CHILDREN_LIMIT).toArray();
		}
		return children;
	}

	@Override
	public Object[] getElements(Object arg0) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = ((List<Object>) arg0);
		// Filter by percentage
		Object[] children = nodes.stream().filter(predicate(tree)).toArray();

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
		// Has children ?
		boolean hasChildren = nodes.stream().anyMatch(predicate(tree));
		// No children ?
		if (!hasChildren) {
			hasChildren = getChildren(arg0).length > 0;
		}
		return hasChildren;
	}

	private Predicate<Object> predicate(Tree tree) {
		return node -> {
			return ((double) tree.getColumnValue(node, 3)) > MAX_NODE_RETAINEDHEAP_PERCENTAGE;
		};
	}

}
