package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery.Tree;

public class CollectionTreeContentProvider implements ITreeContentProvider {

	private static final int CHILDREN_LIMIT = 10;

	private static final double MAX_NODE_RETAINEDHEAP_PERCENTAGE = 0.01;

	private CollectionPane pane;

	public CollectionTreeContentProvider(CollectionPane pane) {
		this.pane = pane;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public Object[] getChildren(Object node) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = (List<Object>) tree.getChildren(node);
		// Filter by percentage
		Object[] children = nodes.stream().filter(predicate(tree)).toArray();
		// No children ?
		if (children.length == 0) {
			List<Object> objects = nodes.stream().limit(CHILDREN_LIMIT).collect(Collectors.toList());
			if(objects.size() == CHILDREN_LIMIT) {
				objects.add("Total: "+nodes.size()+" entries");
			}
			children = objects.toArray();
		}
		return children;
	}

	@Override
	public Object[] getElements(Object node) {
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = ((List<Object>) node);
		// Filter by percentage
		Object[] children = nodes.stream().filter(predicate(tree)).toArray();

		return children;
	}

	@Override
	public Object getParent(Object node) {
		return null;
	}

	@Override
	public boolean hasChildren(Object node) {
		if(node instanceof String) {
			return false;
		}
		final Tree tree = pane.getTree();
		@SuppressWarnings("unchecked")
		List<Object> nodes = (List<Object>) tree.getChildren(node);
		// Has children ?
		boolean hasChildren = nodes.parallelStream().anyMatch(predicate(tree));
		// No children ?
		if (!hasChildren) {
			hasChildren = getChildren(node).length > 0;
		}
		return hasChildren;
	}

	private Predicate<Object> predicate(Tree tree) {
		return node -> {
			return ((double) tree.getColumnValue(node, 3)) > MAX_NODE_RETAINEDHEAP_PERCENTAGE;
		};
	}

}
