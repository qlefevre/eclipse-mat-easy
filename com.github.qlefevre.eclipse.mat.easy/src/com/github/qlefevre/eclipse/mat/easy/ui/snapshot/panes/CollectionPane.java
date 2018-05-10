/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_LIST;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_MAP;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_SET;
import static com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes.CollectionTreeContentProvider.MAX_NODE_RETAINEDHEAP_PERCENTAGE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.query.IContextObject;
import org.eclipse.mat.query.registry.QueryResult;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.ui.MemoryAnalyserPlugin;
import org.eclipse.mat.ui.Messages;
import org.eclipse.mat.ui.editor.AbstractEditorPane;
import org.eclipse.mat.ui.editor.AbstractPaneJob;
import org.eclipse.mat.ui.editor.MultiPaneEditor;
import org.eclipse.mat.ui.util.EasyToolBarDropDown;
import org.eclipse.mat.ui.util.ErrorHelper;
import org.eclipse.mat.ui.util.PopupMenu;
import org.eclipse.mat.ui.util.ProgressMonitorWrapper;
import org.eclipse.mat.ui.util.QueryContextMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPart;

import com.github.qlefevre.eclipse.mat.easy.EclipseMatEasyPlugin;
import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery;
import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery.Tree;

@SuppressWarnings("restriction")
public class CollectionPane extends AbstractEditorPane implements ISelectionProvider {

	private List<ISelectionChangedListener> listeners = Collections
			.synchronizedList(new ArrayList<ISelectionChangedListener>());

	private static final int COLUMN_WIDTH_SIZE = 60;
	private static final int COLUMN_WIDTH_HEAP = 100;
	private static final int COLUMN_WIDTH_PERCENTAGE = 80;
	private static final int COLUMN_WIDTH_LAST_COLUMNS = COLUMN_WIDTH_SIZE + COLUMN_WIDTH_HEAP
			+ COLUMN_WIDTH_PERCENTAGE;

	protected Composite top;
	protected QueryResult srcQueryResult;
	protected TreeViewer viewer;
	protected Tree tree;
	protected QueryContextMenu contextMenu;

	private CollectionQuery.Grouping groupedBy;
	private int[] roots;

	public Tree getTree() {
		return tree;
	}

	protected CollectionTreeContentProvider treeContentProvider;

	@Override
	public void initWithArgument(Object argument) {

		srcQueryResult = (QueryResult) argument;
		tree = ((Tree) srcQueryResult.getSubject());
		viewer.setInput(tree.getElements());
		groupedBy = tree.getGroupedBy();
		roots = tree.getRoots();
		smartExpandAll(viewer, tree);

		hookContextMenu(viewer.getControl());
		hookContextAwareListeners();

		firePropertyChange(IWorkbenchPart.PROP_TITLE);
		firePropertyChange(MultiPaneEditor.PROP_FOLDER_IMAGE);

		super.initWithArgument(argument);

	}

	@Override
	public void createPartControl(Composite composite) {
		top = composite;
		viewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		viewer.setContentProvider(new CollectionTreeContentProvider(this));
		viewer.getTree().setHeaderVisible(true);

		TreeViewerColumn viewerColumnName = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumnName.getColumn().setText("Name");
		viewerColumnName.setLabelProvider(new CollectionTreeLabelProvider(this, 0));

		TreeViewerColumn viewerColumnSize = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumnSize.getColumn().setWidth(COLUMN_WIDTH_SIZE);
		viewerColumnSize.getColumn().setText("Size");
		viewerColumnSize.setLabelProvider(new CollectionTreeLabelProvider(this, 1));

		TreeViewerColumn viewerColumnRetainedHeap = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumnRetainedHeap.getColumn().setWidth(COLUMN_WIDTH_HEAP);
		viewerColumnRetainedHeap.getColumn().setText("Retained Heap");
		viewerColumnRetainedHeap.setLabelProvider(new CollectionTreeLabelProvider(this, 2));

		TreeViewerColumn viewerColumnPercentage = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumnPercentage.getColumn().setWidth(COLUMN_WIDTH_PERCENTAGE);
		viewerColumnPercentage.getColumn().setText("Percentage");
		viewerColumnPercentage.setLabelProvider(new CollectionTreeLabelProvider(this, 3));

		composite.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				org.eclipse.swt.widgets.Tree tree = viewer.getTree();
				Rectangle bounds = ((org.eclipse.swt.widgets.Composite) e.getSource()).getBounds();
				int width = bounds.width - bounds.x;
				tree.getColumn(0).setWidth(width - COLUMN_WIDTH_LAST_COLUMNS - 10);
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

	}

	@Override
	protected void editorContextMenuAboutToShow(PopupMenu menu) {
		menu.addSeparator();
		contextMenu = new QueryContextMenu(this, srcQueryResult);
		contextMenu.addContextActions(menu, viewer.getStructuredSelection(), viewer.getTree());
	}

	protected void deactivateViewer() {
		// context menu is disposed when a new one is created
		unhookContextAwareListeners();
	}

	// //////////////////////////////////////////////////////////////
	// selection provider
	// //////////////////////////////////////////////////////////////

	/** delay selection events -> do not flood object inspector */
	private Listener proxy = new Listener() {
		boolean arrowKeyDown = false;
		int[] count = new int[1];

		public void handleEvent(final Event e) {
			switch (e.type) {
			case SWT.KeyDown:
				arrowKeyDown = ((e.keyCode == SWT.ARROW_UP) || (e.keyCode == SWT.ARROW_DOWN)
						|| (e.keyCode == SWT.ARROW_LEFT) || e.keyCode == SWT.ARROW_RIGHT) && e.stateMask == 0;
				//$FALL-THROUGH$
			case SWT.Selection:
				count[0]++;
				final int id = count[0];
				viewer.getControl().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (arrowKeyDown) {
							viewer.getControl().getDisplay().timerExec(250, new Runnable() {
								public void run() {
									if (id == count[0]) {
										forwardSelectionChangedEvent();
									}
								}
							});
						} else {
							forwardSelectionChangedEvent();
						}
					}
				});
				break;
			default:
				break;
			}
		}
	};

	protected void hookContextAwareListeners() {
		Control control = viewer.getControl();
		control.addListener(SWT.Selection, proxy);
		control.addListener(SWT.KeyDown, proxy);
	}

	protected void unhookContextAwareListeners() {
		Control control = viewer.getControl();
		control.removeListener(SWT.Selection, proxy);
		control.removeListener(SWT.KeyDown, proxy);
	}

	private void forwardSelectionChangedEvent() {
		List<ISelectionChangedListener> l = new ArrayList<ISelectionChangedListener>(listeners);
		for (ISelectionChangedListener listener : l)
			listener.selectionChanged(
					new SelectionChangedEvent(this, convertSelection(viewer.getStructuredSelection())));
	}

	public ISelection getSelection() {
		return viewer != null ? convertSelection(viewer.getStructuredSelection()) : StructuredSelection.EMPTY;
	}

	private ISelection convertSelection(IStructuredSelection selection) {
		if (!selection.isEmpty()) {
			List<IContextObject> menuContext = new ArrayList<IContextObject>();

			for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
				Object selected = iter.next();
				IContextObject ctx = getInspectorContextObject(selected);
				if (ctx != null)
					menuContext.add(ctx);
			}

			if (!menuContext.isEmpty())
				return new StructuredSelection(menuContext);
		}

		// return an empty selection
		return StructuredSelection.EMPTY;
	}

	private IContextObject getInspectorContextObject(Object subject) {
		if (subject instanceof String)
			return null;
		return srcQueryResult.getDefaultContextProvider().getContext(subject);
	}

	public void setSelection(ISelection selection) {
		// not supported (unable to convert the IContextObject
		// back to the original object)
		throw new UnsupportedOperationException();
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public String getTitle() {
		return "Collection Tree";
	}

	@Override
	public Image getTitleImage() {
		return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.COLLECTION_TREE);
	}

	@Override
	public void contributeToToolBar(IToolBarManager manager) {
		addGroupingOptions(manager);
		super.contributeToToolBar(manager);
	}

	private void addGroupingOptions(IToolBarManager manager) {
		Action groupingAction = new EasyToolBarDropDown("CollectionTree", //
				MemoryAnalyserPlugin.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.GROUPING), this) {

			@Override
			public void contribute(PopupMenu menu) {
				for (CollectionQuery.Grouping group : CollectionQuery.Grouping.values()) {
					Action action = new GroupingAction(group);
					action.setText(group.toString());
					action.setImageDescriptor(group.equals(CollectionQuery.Grouping.BY_CLASS)
							? MemoryAnalyserPlugin.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.CLASS)
							: MemoryAnalyserPlugin
									.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.EXPERT_SYSTEM));
					if (groupedBy == group) {
						action.setEnabled(false);
						action.setChecked(true);
					}
					menu.add(action);
				}
			}

		};

		manager.add(groupingAction);
	}

	private class GroupingAction extends Action {
		private CollectionQuery.Grouping target;

		public GroupingAction(CollectionQuery.Grouping group) {
			super(Messages.DominatorPane_Group, AS_CHECK_BOX);
			this.target = group;
		}

		public void run() {
			if (!isChecked())// do not run the same action twice - selection
				// was not changed
				return;
			/*
			 * if (viewer.getResult().hasActiveFilter()) { StringBuilder buf = new
			 * StringBuilder(); buf.append(Messages.DominatorPane_WholeTreeWillBeGrouped);
			 * 
			 * MessageBox msg = new MessageBox(viewer.getControl().getShell(), SWT.OK |
			 * SWT.CANCEL); msg.setText(Messages.DominatorPane_Info);
			 * msg.setMessage(buf.toString());
			 * 
			 * if (msg.open() != SWT.OK) return; }
			 */

			Job job = new AbstractPaneJob(getText(), CollectionPane.this) {
				protected IStatus doRun(IProgressMonitor monitor) {
					try {
						tree = null;

						ProgressMonitorWrapper listener = new ProgressMonitorWrapper(monitor);
						ISnapshot snapshot = (ISnapshot) getQueryContext().get(ISnapshot.class, null);
						switch (target) {
						case NONE:
							tree = CollectionQuery.Factory.create(snapshot, roots, listener);
							break;
						case BY_CLASS:
							tree = CollectionQuery.Factory.groupByClass(snapshot, roots, listener);
							break;

						}

						final QueryResult queryResult = new QueryResult(null, "collection_tree -groupBy " //$NON-NLS-1$
								+ target.name(), tree);

						top.getDisplay().asyncExec(new Runnable() {
							public void run() {
								// deactivateViewer();

								// groupedBy = target;

								// RefinedResultViewer v = createViewer(queryResult);

								// activateViewer(v);

								srcQueryResult = queryResult;
								// Tree collectiontree = ((Tree) srcQueryResult.getSubject());
								viewer.setInput(tree.getElements());
								groupedBy = tree.getGroupedBy();
								roots = tree.getRoots();
								smartExpandAll(viewer, tree);

							}

						});

						return Status.OK_STATUS;

					} catch (SnapshotException e) {
						return ErrorHelper.createErrorStatus(e);
					}
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}

	private void smartExpandAll(TreeViewer viewer, Tree tree) {
		ITreeContentProvider contentProvider = (ITreeContentProvider) viewer.getContentProvider();
		Object[] rootChildren = contentProvider.getElements(tree.getElements());

		// Find all children
		for (Object rootChild : rootChildren) {
			Object[] children = contentProvider.getChildren(rootChild);
			for (Object object : children) {
				// Find collection level ...
				Object child = object;
				Object expandChild = child;
				int level = 0;
				int notCollectionObjlevel = 0;
				double percentage = 0;
				do {
					level++;
					notCollectionObjlevel++;
					byte type = ((byte) tree.getColumnValue(child, -1));
					if (type == TYPE_LIST || type == TYPE_SET || type == TYPE_MAP) {
						notCollectionObjlevel = 0;
						expandChild = child;
					}
					Object[] subChildren = contentProvider.getChildren(child);
					if (subChildren != null && subChildren.length > 0) {
						child = subChildren[0];
					} else {
						child = null;
					}
					if (child != null) {
						percentage = ((double) tree.getColumnValue(child, 3));

					}
				} while (child != null && percentage > MAX_NODE_RETAINEDHEAP_PERCENTAGE);
				// level -= notCollectionObjlevel;

				// Expand to level
				// System.out.println(level + " - " + notCollectionObjlevel);
				// viewer.expandToLevel(rootChild, level);
				viewer.expandToLevel(expandChild, 1);

			}

		}
	}

}
