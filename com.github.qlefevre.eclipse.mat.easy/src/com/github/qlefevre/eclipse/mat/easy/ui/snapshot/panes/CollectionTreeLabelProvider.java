package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.mat.query.BytesFormat;
import org.eclipse.mat.ui.MemoryAnalyserPlugin;
import org.eclipse.swt.graphics.Image;
import static com.github.qlefevre.eclipse.mat.easy.inspections.CollectionHeapResolverRegistry.*;
import com.github.qlefevre.eclipse.mat.easy.EclipseMatEasyPlugin;

@SuppressWarnings("restriction")
public class CollectionTreeLabelProvider extends ColumnLabelProvider {

	private static final DecimalFormat DF_PERCENT = new DecimalFormat("0.00%");
	private int column;

	private CollectionPane2 pane;

	public CollectionTreeLabelProvider(CollectionPane2 pane, int column) {
		this.pane = pane;
		this.column = column;
	}

	@Override
	public String getText(Object element) {
		String text = "";
		switch (column) {
		case 0:
			text = pane.getTree().getColumnValue(element, column).toString();
			break;
		case 1:
			int size = (int) pane.getTree().getColumnValue(element, column);
			text = size >= 0 ? Integer.toString(size) : text;
			break;
		case 2:
			long heap = (long) pane.getTree().getColumnValue(element, column);
			text = BytesFormat.getInstance().format(heap);
			break;
		case 3:
			double percent = (double) pane.getTree().getColumnValue(element, column);
			text = DF_PERCENT.format(percent);
			break;
		default:
			text = pane.getTree().getColumnValue(element, column).toString();
		}
		return text;
	}
	
	
	@Override
	public Image getImage(Object element) {
		if (column == 0) {
			switch ((byte) pane.getTree().getColumnValue(element, -1)) {
			case TYPE_LIST:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.LIST);
			case TYPE_SET:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.SET);
			case TYPE_MAP:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.MAP);
			case TYPE_ARRAY:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.ARRAY);
			case TYPE_STRING:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.STRING);
			case TYPE_NUMBER:
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.NUMBER);
			}
			return MemoryAnalyserPlugin.getImage(MemoryAnalyserPlugin.ISharedImages.CLASS);
		} else {
			return null;
		}
	}

}
