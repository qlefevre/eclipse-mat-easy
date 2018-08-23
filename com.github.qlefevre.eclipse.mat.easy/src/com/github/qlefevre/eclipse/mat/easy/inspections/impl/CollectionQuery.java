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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.query.Column;
import org.eclipse.mat.query.Column.SortDirection;
import org.eclipse.mat.query.IContextObject;
import org.eclipse.mat.query.IContextObjectSet;
import org.eclipse.mat.query.IDecorator;
import org.eclipse.mat.query.IQuery;
import org.eclipse.mat.query.IResultTree;
import org.eclipse.mat.query.ResultMetaData;
import org.eclipse.mat.query.annotations.Argument;
import org.eclipse.mat.query.annotations.Category;
import org.eclipse.mat.query.annotations.CommandName;
import org.eclipse.mat.query.annotations.HelpUrl;
import org.eclipse.mat.query.annotations.Icon;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.GCRootInfo;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.VoidProgressListener;

import com.github.qlefevre.eclipse.mat.easy.Messages;
import com.github.qlefevre.eclipse.mat.easy.inspections.CollectionHeapResolverRegistry;

@CommandName("collection_tree")
@Category("__hidden__")
@Icon("/META-INF/icons/collection_tree.gif")
@HelpUrl("/org.eclipse.mat.ui.help/concepts/dominatortree.html")
public class CollectionQuery implements IQuery {

	private static final int NOT_INITIALIZED = -10;

	public enum Grouping {
		NONE(Messages.CollectionQuery_Group_None), BY_CLASS(Messages.CollectionQuery_Group_ByClass);

		String label;

		private Grouping(String label) {
			this.label = label;
		}

		public String toString() {
			return label;
		}

	}

	@Argument
	public ISnapshot snapshot;

	@Argument(isMandatory = false)
	public Grouping groupBy = Grouping.NONE;

	public Tree execute(IProgressListener listener) throws Exception {
		// Force a missing dominator tree to be built
		snapshot.getTopAncestorsInDominatorTree(new int[0], listener);
		return create(new int[] { -1 }, listener);
	}

	protected Tree create(int[] roots, IProgressListener listener) throws SnapshotException {
		if (groupBy == null)
			groupBy = Grouping.NONE;

		switch (groupBy) {
		case NONE:
			return Factory.create(snapshot, roots, listener);
		case BY_CLASS:
			return Factory.groupByClass(snapshot, roots, listener);

		}

		return null;
	}

	// //////////////////////////////////////////////////////////////
	// factory
	// //////////////////////////////////////////////////////////////

	public static class Factory {
		public static Tree create(ISnapshot snapshot, int[] roots, IProgressListener listener)
				throws SnapshotException {
			List<Node> elements;

			if (roots.length == 1 && roots[0] == -1)
				elements = DefaultTree.prepare(snapshot, roots[0], listener);
			else
				elements = DefaultTree.prepareSet(snapshot, roots, listener);

			return new DefaultTree(snapshot, roots, elements);
		}

		public static Tree groupByClass(ISnapshot snapshot, int[] roots, IProgressListener listener) {
			List<ClassNode> elements;

			if (roots.length == 1 && roots[0] == -1)
				elements = ClassTree.prepare(snapshot, roots, listener);
			else
				elements = ClassTree.prepareSet(snapshot, roots, listener);

			return new ClassTree(snapshot, roots, elements);
		}

	}

	// //////////////////////////////////////////////////////////////
	// nodes
	// //////////////////////////////////////////////////////////////

	private static class Node {
		int objectId;

		String label;
		int size;
		long retainedHeap;
		byte type;

		public Node(int objectId) {
			this.objectId = objectId;
			this.size = NOT_INITIALIZED;
			this.retainedHeap = NOT_INITIALIZED;
			this.type = (byte) NOT_INITIALIZED;
		}

		@Override
		public int hashCode() {
			return objectId;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Node) {
				Node other = (Node) obj;
				return objectId == other.objectId;
			}
			return false;
		}
	}

	private static class GroupedNode extends Node {
		ArrayInt objects = new ArrayInt();

		private GroupedNode(int objectId) {
			super(objectId);
		}
	}

	private static class ClassNode extends GroupedNode {
		private ClassNode(int objectId) {
			super(objectId);
		}
	}

	// //////////////////////////////////////////////////////////////
	// tree implementations
	// //////////////////////////////////////////////////////////////

	public abstract static class Tree implements IResultTree {
		protected ISnapshot snapshot;
		protected int[] roots;
		protected Grouping groupedBy;
		protected double totalHeap;

		public Tree(ISnapshot snapshot, int[] roots, Grouping groupedBy) {
			this.snapshot = snapshot;
			this.roots = roots;
			this.groupedBy = groupedBy;
			this.totalHeap = snapshot.getSnapshotInfo().getUsedHeapSize();
		}

		public Grouping getGroupedBy() {
			return groupedBy;
		}

		public int[] getRoots() {
			return roots;
		}

		public ResultMetaData getResultMetaData() {
			return null;
		}
	}

	private static class DefaultTree extends Tree implements IDecorator {
		static List<Node> prepareSet(ISnapshot snapshot, int[] roots, IProgressListener listener)
				throws SnapshotException {
			List<Node> nodes = new ArrayList<Node>();
			for (int ii = 0; ii < roots.length; ii++) {
				Node node = new Node(roots[ii]);
				node.retainedHeap = snapshot.getRetainedHeapSize(roots[ii]);
				nodes.add(node);
				if (listener.isCanceled())
					throw new IProgressListener.OperationCanceledException();
			}

			// these nodes are not sorted (result of top dominators api call)
			Collections.sort(nodes, new Comparator<Node>() {
				public int compare(Node o1, Node o2) {
					return o1.retainedHeap < o2.retainedHeap ? 1 : o1.retainedHeap == o2.retainedHeap ? 0 : -1;
				}
			});

			return nodes;
		}

		static List<Node> prepare(ISnapshot snapshot, int id, IProgressListener listener) {
			try {
				int[] objectIds = snapshot.getImmediateDominatedIds(id);
				if (objectIds == null)
					return null;

				List<Node> nodes = new ArrayList<Node>(objectIds.length);

				for (int ii = 0; ii < objectIds.length; ii++) {
					nodes.add(new Node(objectIds[ii]));
					if (listener.isCanceled())
						throw new IProgressListener.OperationCanceledException();
				}

				return nodes;
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}
		}

		private List<Node> elements;

		private CollectionHeapResolverRegistry collectionHeapResolver;

		private DefaultTree(ISnapshot snapshot, int[] roots, List<Node> elements) {
			super(snapshot, roots, Grouping.NONE);
			this.collectionHeapResolver = CollectionHeapResolverRegistry.getInstance();
			this.elements = elements;
		}

		@Override
		public ResultMetaData getResultMetaData() {
			return new ResultMetaData.Builder().setIsPreSortedBy(2, SortDirection.DESC).build();
		}

		public Column[] getColumns() {
			return new Column[] { new Column(Messages.Column_ClassName, String.class).decorator(this),
					new Column(Messages.Column_ShallowHeap, int.class).noTotals(),
					new Column(Messages.Column_RetainedHeap, long.class).noTotals(),
					new Column(Messages.Column_Percentage, double.class)
							.formatting(new java.text.DecimalFormat("0.00%")).noTotals() };
		}

		public List<?> getElements() {
			return elements;
		}

		public boolean hasChildren(Object element) {
			// too expensive to check up-front
			return true;
		}

		public List<?> getChildren(Object parent) {
			return prepare(snapshot, ((Node) parent).objectId, new VoidProgressListener());
		}

		public Object getColumnValue(Object row, int columnIndex) {
			try {
				Node node = (Node) row;

				switch (columnIndex) {
				case -1:
					if (node.type == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.type = collectionHeapResolver.getType(obj);
					}
					return node.type;
				case 0:
					if (node.label == null) {
						IObject obj = snapshot.getObject(node.objectId);
						node.label = collectionHeapResolver.getDisplayName(obj);
					}
					return node.label;
				case 1:
					if (node.size == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.size = collectionHeapResolver.getCollectionSize(obj);
					}
					return node.size;
				case 2:
					if (node.retainedHeap == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.retainedHeap = collectionHeapResolver.getCollectionHeapSize(obj);
					}
					return node.retainedHeap;
				case 3:
					if (node.retainedHeap == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.retainedHeap = collectionHeapResolver.getCollectionHeapSize(obj);
					}
					return node.retainedHeap / totalHeap;
				}

				return null;
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}
		}

		public IContextObject getContext(final Object row) {
			return new IContextObject() {
				public int getObjectId() {
					return ((Node) row).objectId;
				}
			};
		}

		public String prefix(Object row) {
			return null;
		}

		public String suffix(Object row) {
			try {
				Node node = (Node) row;
				GCRootInfo[] roots = snapshot.getGCRootInfo(node.objectId);
				return roots == null ? null : GCRootInfo.getTypeSetAsString(roots);
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static class ClassTree extends Tree {

		private CollectionHeapResolverRegistry collectionHeapResolver;

		public static List<ClassNode> prepare(ISnapshot snapshot, int[] objectIds, IProgressListener listener) {
			try {
				HashMapIntObject<ClassNode> class2node = new HashMapIntObject<ClassNode>();

				for (int ii = 0; ii < objectIds.length; ii++) {
					int[] dominatedIds = snapshot.getImmediateDominatedIds(objectIds[ii]);

					for (int jj = 0; jj < dominatedIds.length; jj++) {
						int objectId = dominatedIds[jj];
						IClass clazz = snapshot.getClassOf(objectId);
						ClassNode node = class2node.get(clazz.getObjectId());

						if (node == null) {
							node = new ClassNode(clazz.getObjectId());
							node.label = clazz.getName();
							class2node.put(node.objectId, node);
						}

						node.objects.add(objectId);
						node.retainedHeap += snapshot.getRetainedHeapSize(objectId);

						if (listener.isCanceled())
							throw new IProgressListener.OperationCanceledException();
					}
				}

				return Arrays.asList(class2node.getAllValues(new ClassNode[0]));
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}
		}

		public static List<ClassNode> prepareSet(ISnapshot snapshot, int[] roots, IProgressListener listener) {
			try {
				HashMapIntObject<ClassNode> class2node = new HashMapIntObject<ClassNode>();

				for (int jj = 0; jj < roots.length; jj++) {
					int objectId = roots[jj];
					IClass clazz = snapshot.getClassOf(objectId);
					ClassNode node = class2node.get(clazz.getObjectId());

					if (node == null) {
						node = new ClassNode(clazz.getObjectId());
						node.label = clazz.getName();
						class2node.put(node.objectId, node);
					}

					node.objects.add(objectId);
					node.retainedHeap += snapshot.getRetainedHeapSize(objectId);

					if (listener.isCanceled())
						throw new IProgressListener.OperationCanceledException();
				}

				return Arrays.asList(class2node.getAllValues(new ClassNode[0]));
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}
		}

		private List<ClassNode> elements;

		private ClassTree(ISnapshot snapshot, int[] roots, List<ClassNode> elements) {
			super(snapshot, roots, Grouping.BY_CLASS);
			this.elements = elements;
			this.collectionHeapResolver = CollectionHeapResolverRegistry.getInstance();
		}

		@Override
		public Column[] getColumns() {
			return new Column[] { new Column(Messages.Column_ClassName, String.class),
					new Column(Messages.Column_Objects, int.class), new Column(Messages.Column_ShallowHeap, int.class),
					new Column(Messages.Column_RetainedHeap, long.class).sorting(SortDirection.DESC),
					new Column(Messages.Column_Percentage, double.class)
							.formatting(new java.text.DecimalFormat("0.00%")) };
		}

		@Override
		public List<?> getElements() {
			return elements;
		}

		@Override
		public boolean hasChildren(Object element) {
			// too expensive to check up-front
			return true;
		}

		@Override
		public List<?> getChildren(Object parent) {
			return prepare(snapshot, ((GroupedNode) parent).objects.toArray(), new VoidProgressListener());
		}

		@Override
		public Object getColumnValue(Object row, int columnIndex) {
			try {
				ClassNode node = (ClassNode) row;

				switch (columnIndex) {
				case -1:
					if (node.type == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.type = collectionHeapResolver.getType(obj);
					}
					return node.type;
				case 0:
					if (node.label == null) {
						IObject obj = snapshot.getObject(node.objectId);
						node.label = collectionHeapResolver.getDisplayName(obj);
					}
					return node.label;
				case 1:
					if (node.size == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.size = collectionHeapResolver.getCollectionSize(obj);
					}
					return node.size;
				case 2:
					if (node.retainedHeap == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.retainedHeap = collectionHeapResolver.getCollectionHeapSize(obj);
					}
					return node.retainedHeap;
				case 3:
					if (node.retainedHeap == NOT_INITIALIZED) {
						IObject obj = snapshot.getObject(node.objectId);
						node.retainedHeap = collectionHeapResolver.getCollectionHeapSize(obj);
					}
					return node.retainedHeap / totalHeap;
				}

				return null;
			} catch (SnapshotException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public IContextObject getContext(final Object row) {
			return new IContextObjectSet() {
				public int getObjectId() {
					return ((ClassNode) row).objectId;
				}

				public int[] getObjectIds() {
					return ((ClassNode) row).objects.toArray();
				}

				public String getOQL() {
					return null;
				}
			};
		}
	}

}