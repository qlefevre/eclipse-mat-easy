/*******************************************************************************
 * Copyright (c) 2018 Quentin Lef�vre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.ui.snapshot.panes;

import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_ARRAY;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_LIST;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_MAP;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_NUMBER;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_SET;
import static com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver.TYPE_STRING;

import java.text.DecimalFormat;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.mat.query.BytesFormat;
import org.eclipse.mat.ui.MemoryAnalyserPlugin;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.github.qlefevre.eclipse.mat.easy.EclipseMatEasyPlugin;

@SuppressWarnings("restriction")
public class CollectionTreeLabelProvider extends ColumnLabelProvider {

	private static final DecimalFormat DF_PERCENT = new DecimalFormat("0.00%");
	private int column;

	private CollectionPane pane;

	public CollectionTreeLabelProvider(CollectionPane pane, int column) {
		this.pane = pane;
		this.column = column;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof String) {
			return column == 0 ? (String) element : "";
		}
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
			if (element instanceof String) {
				return EclipseMatEasyPlugin.getImage(EclipseMatEasyPlugin.SUM);
			}
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
		}
		return null;
	}

	@Override
	public Font getFont(Object element) {
		Font font = super.getFont(element);
		if (element instanceof String) {
			font = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
		}
		return font;
	}

}
