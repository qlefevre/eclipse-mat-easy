package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.query.IResultTree;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.github.qlefevre.eclipse.mat.easy.EclipseMatEasyPlugin;
import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery;
import com.github.qlefevre.eclipse.mat.easy.inspections.impl.CollectionQuery.Tree;

@SuppressWarnings("restriction")
public class CollectionPane2 extends AbstractEditorPane {

	private static final int COLUMN_WIDTH_SIZE = 60;
	private static final int COLUMN_WIDTH_HEAP = 100;
	private static final int COLUMN_WIDTH_PERCENTAGE = 80;
	private static final int COLUMN_WIDTH_LAST_COLUMNS = COLUMN_WIDTH_SIZE + COLUMN_WIDTH_HEAP
			+ COLUMN_WIDTH_PERCENTAGE;

	protected Composite top;
	protected QueryResult srcQueryResult;
	protected TreeViewer viewer;
	protected Tree tree;

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

		/*
		 * RefinedResultViewer viewer = createViewer(srcQueryResult);
		 * 
		 * activateViewer(viewer);
		 */

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
					action.setImageDescriptor(
							MemoryAnalyserPlugin.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.GROUPING));
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

    private class GroupingAction extends Action
    {
        private CollectionQuery.Grouping target;

        public GroupingAction(CollectionQuery.Grouping group)
        {
            super(Messages.DominatorPane_Group, AS_CHECK_BOX);
            this.target = group;
        }

        public void run()
        {
            if (!isChecked())// do not run the same action twice - selection
                // was not changed
                return;
            /*
            if (viewer.getResult().hasActiveFilter())
            {
                StringBuilder buf = new StringBuilder();
                buf.append(Messages.DominatorPane_WholeTreeWillBeGrouped);

                MessageBox msg = new MessageBox(viewer.getControl().getShell(), SWT.OK | SWT.CANCEL);
                msg.setText(Messages.DominatorPane_Info);
                msg.setMessage(buf.toString());

                if (msg.open() != SWT.OK)
                    return;
            }*/

            Job job = new AbstractPaneJob(getText(), CollectionPane2.this)
            {
                protected IStatus doRun(IProgressMonitor monitor)
                {
                    try
                    {
                        IResultTree tree = null;

                        ProgressMonitorWrapper listener = new ProgressMonitorWrapper(monitor);
                        ISnapshot snapshot = (ISnapshot) getQueryContext().get(ISnapshot.class, null);
                        switch (target)
                        {
                            case NONE:
                                tree = CollectionQuery.Factory.create(snapshot, roots, listener);
                                break;
                            case BY_CLASS:
                                tree = CollectionQuery.Factory.groupByClass(snapshot, roots, listener);
                                break;
                            
                
                        }

                        final QueryResult queryResult = new QueryResult(null, "collection_tree -groupBy " //$NON-NLS-1$
                                        + target.name(), tree);

                        top.getDisplay().asyncExec(new Runnable()
                        {
                            public void run()
                            {
                                //deactivateViewer();

                                //groupedBy = target;

                                //RefinedResultViewer v = createViewer(queryResult);

                                //activateViewer(v);
                                
                                srcQueryResult = queryResult;
                    			Tree collectiontree = ((Tree) srcQueryResult.getSubject());
                    			viewer.setInput(collectiontree.getElements());
                    			groupedBy = collectiontree.getGroupedBy();
                    			roots = collectiontree.getRoots();

                            }

                        });

                        return Status.OK_STATUS;

                    }
                    catch (SnapshotException e)
                    {
                        return ErrorHelper.createErrorStatus(e);
                    }
                }
            };
            job.setUser(true);
            job.schedule();
        }
    }
}
