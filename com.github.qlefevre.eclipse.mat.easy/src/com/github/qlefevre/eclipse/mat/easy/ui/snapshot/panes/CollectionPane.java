/*******************************************************************************
 * Copyright (c) 2008, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API and implementation
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.query.IResultTree;
import org.eclipse.mat.query.registry.QueryDescriptor;
import org.eclipse.mat.query.registry.QueryRegistry;
import org.eclipse.mat.query.registry.QueryResult;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.ui.MemoryAnalyserPlugin;
import org.eclipse.mat.ui.Messages;
import org.eclipse.mat.ui.editor.AbstractPaneJob;
import org.eclipse.mat.ui.internal.viewer.RefinedResultViewer;
import org.eclipse.mat.ui.util.EasyToolBarDropDown;
import org.eclipse.mat.ui.util.ErrorHelper;
import org.eclipse.mat.ui.util.PopupMenu;
import org.eclipse.mat.ui.util.ProgressMonitorWrapper;
import org.eclipse.mat.util.VoidProgressListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.github.qlefevre.eclipse.mat.easy.inspections.CollectionQuery;

@SuppressWarnings("restriction")
public class CollectionPane extends QueryResultPane
{
    private CollectionQuery.Grouping groupedBy;
    private int[] roots;

    @Override
    public void initWithArgument(Object argument)
    {
        try
        {
            if (argument == null)
            {
                ISnapshot snapshot = (ISnapshot) getQueryContext().get(ISnapshot.class, null);
                CollectionQuery.Tree tree = CollectionQuery.Factory.create(snapshot, new int[] { -1 },
                                new VoidProgressListener());

                
				QueryDescriptor query = QueryRegistry.instance().getQuery(CollectionQuery.class);
                argument = new QueryResult(query, query.getName(), tree);

                groupedBy = tree.getGroupedBy();
                roots = tree.getRoots();
            }
            else
            {
                QueryResult queryResult = (QueryResult) argument;
                CollectionQuery.Tree tree = (CollectionQuery.Tree) queryResult.getSubject();
                groupedBy = tree.getGroupedBy();
                roots = tree.getRoots();

            }

            super.initWithArgument(argument);
        }
        catch (SnapshotException e)
        {
            ErrorHelper.logThrowableAndShowMessage(e);
        }
    }

    @Override
    public void contributeToToolBar(IToolBarManager manager)
    {
        addGroupingOptions(manager);
        super.contributeToToolBar(manager);
    }

    private void addGroupingOptions(IToolBarManager manager)
    {
        Action groupingAction = new EasyToolBarDropDown("Quentin", //
                        MemoryAnalyserPlugin.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.GROUPING), this)
        {

            @Override
            public void contribute(PopupMenu menu)
            {
                for (CollectionQuery.Grouping group : CollectionQuery.Grouping.values())
                {
                    Action action = new GroupingAction(group);
                    action.setText(group.toString());
                    action.setImageDescriptor( MemoryAnalyserPlugin.getImageDescriptor(MemoryAnalyserPlugin.ISharedImages.GROUPING));
                    if (groupedBy == group)
                    {
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
            if (viewer.getResult().hasActiveFilter())
            {
                StringBuilder buf = new StringBuilder();
                buf.append(Messages.DominatorPane_WholeTreeWillBeGrouped);

                MessageBox msg = new MessageBox(viewer.getControl().getShell(), SWT.OK | SWT.CANCEL);
                msg.setText(Messages.DominatorPane_Info);
                msg.setMessage(buf.toString());

                if (msg.open() != SWT.OK)
                    return;
            }

            Job job = new AbstractPaneJob(getText(), CollectionPane.this)
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
                            case BY_CLASSLOADER:
                                tree = CollectionQuery.Factory.groupByClassLoader(snapshot, roots, listener);
                                break;
                            case BY_PACKAGE:
                                tree = CollectionQuery.Factory.groupByPackage(snapshot, roots, listener);
                                break;
                        }

                        final QueryResult queryResult = new QueryResult(null, "dominator_tree -groupBy " //$NON-NLS-1$
                                        + target.name(), tree);

                        top.getDisplay().asyncExec(new Runnable()
                        {
                            public void run()
                            {
                                deactivateViewer();

                                groupedBy = target;

                                RefinedResultViewer v = createViewer(queryResult);

                                activateViewer(v);
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