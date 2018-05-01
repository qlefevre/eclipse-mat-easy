package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;



import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.mat.query.registry.QueryResult;
import org.eclipse.mat.ui.editor.AbstractEditorPane;
import org.eclipse.mat.ui.editor.MultiPaneEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.github.qlefevre.eclipse.mat.easy.inspections.CollectionQuery.Tree;
@SuppressWarnings("restriction")
public class CollectionPane2 extends AbstractEditorPane {
	
	private static final int COLUMN_WIDTH_SIZE = 60;
	private static final int COLUMN_WIDTH_HEAP = 90;
	private static final int COLUMN_WIDTH_PERCENTAGE = 80;
	private static final int COLUMN_WIDTH_LAST_COLUMNS =COLUMN_WIDTH_SIZE+ COLUMN_WIDTH_HEAP + COLUMN_WIDTH_PERCENTAGE;
	
	protected QueryResult srcQueryResult;
	protected TreeViewer viewer;
	protected Tree tree;
	public Tree getTree() {
		return tree;
	}


	protected CollectionTreeContentProvider treeContentProvider;
	
	@Override
	public void initWithArgument(Object argument) {
		srcQueryResult = (QueryResult) argument;
		tree = ((Tree)srcQueryResult.getSubject());
		viewer.setInput(tree.getElements());
		
		/*
		 * RefinedResultViewer viewer = createViewer(srcQueryResult);
		 * 
		 * activateViewer(viewer);
		 */

		firePropertyChange(IWorkbenchPart.PROP_TITLE);
		firePropertyChange(MultiPaneEditor.PROP_FOLDER_IMAGE);
	}

	@Override
	public void createPartControl(Composite arg0) {
		viewer = new TreeViewer(arg0, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		viewer.setContentProvider(new CollectionTreeContentProvider(this));
		viewer.getTree().setHeaderVisible(true);
		
		TreeViewerColumn viewerColumnName = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumnName.getColumn().setText("Name");
		viewerColumnName.setLabelProvider(new CollectionTreeLabelProvider(this,0));
        
		TreeViewerColumn viewerColumnSize = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumnSize.getColumn().setWidth(COLUMN_WIDTH_SIZE);
        viewerColumnSize.getColumn().setText("Size");
        viewerColumnSize.setLabelProvider(new CollectionTreeLabelProvider(this,1));
        
        TreeViewerColumn viewerColumnRetainedHeap = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumnRetainedHeap.getColumn().setWidth(COLUMN_WIDTH_HEAP);
        viewerColumnRetainedHeap.getColumn().setText("Retained Heap");
        viewerColumnRetainedHeap.setLabelProvider(new CollectionTreeLabelProvider(this,2));
        
        TreeViewerColumn viewerColumnPercentage = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumnPercentage.getColumn().setWidth(COLUMN_WIDTH_PERCENTAGE);
        viewerColumnPercentage.getColumn().setText("Percentage");
        viewerColumnPercentage.setLabelProvider(new CollectionTreeLabelProvider(this,3));

      
        arg0.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				org.eclipse.swt.widgets.Tree tree = viewer.getTree();
				Rectangle bounds = ((org.eclipse.swt.widgets.Composite)e.getSource()).getBounds();
				int width = bounds.width-bounds.x;
				tree.getColumn(0).setWidth(width-COLUMN_WIDTH_LAST_COLUMNS-5);
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
	
	

}
