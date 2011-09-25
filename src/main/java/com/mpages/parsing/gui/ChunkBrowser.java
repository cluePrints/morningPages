package com.mpages.parsing.gui;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.mpages.parsing.domain.Boundary;
import com.mpages.parsing.domain.Chunk;
import com.mpages.parsing.domain.GreedyBoundaryGeneralizer;

public class ChunkBrowser extends Composite {
	private java.util.List<Chunk> chunksListModel = new LinkedList<Chunk>();

	private List chunkListDisplay;
	private Text predictedBoundaryDisplay;

	public ChunkBrowser(Composite parent) {
		super(parent, SWT.NONE);

		setLayout(new FormLayout());
		Composite rootShell = this;
		predictedBoundaryDisplay = new Text(rootShell, SWT.BORDER);
		Layout.fillAllDimensionsWith(predictedBoundaryDisplay).leaveFreeAtTheBottom(10);

		chunkListDisplay = new List(rootShell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		Layout.fillAllDimensionsWith(chunkListDisplay).toTheBottomOf(predictedBoundaryDisplay);

		onDoubleClickRemoveItem(chunkListDisplay);
	}

	public void add(Chunk chunk) {
		chunksListModel.add(chunk);
		chunkListDisplay.add(chunk.toString());
		recalcDisplayedRegex();
	}

	public void removeById(int i) {
		if (i < 0)
			return;

		chunkListDisplay.remove(i);
		chunksListModel.remove(i);
		recalcDisplayedRegex();
	}

	public void clear() {
		chunksListModel.clear();
		chunkListDisplay.removeAll();
		recalcDisplayedRegex();
	}

	private void recalcDisplayedRegex() {
		Boundary predictions = new GreedyBoundaryGeneralizer()
				.predict(chunksListModel);
		String regexp = "";
		if (predictions != null) {
			regexp = predictions.asHumanReadableRegex();
		}
		predictedBoundaryDisplay.setText(regexp);
	}

	private void onDoubleClickRemoveItem(final List component) {
		component.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				int i = component.getSelectionIndex();
				removeById(i);
			}
		});
	}
}
