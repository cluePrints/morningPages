package com.mpages.parsing.gui;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

public class Layout {
	private FormData layout;
	private Control component;

	public static Layout fillAllDimensionsWith(Control component)
	{
		return of(component).fillAllDimensions();
	}
	
	public static Layout fillHorizontal(Control component)
	{
		Layout builder = of(component);
		builder.layout.left = new FormAttachment(0,0);
		builder.layout.right = new FormAttachment(100,0);
		return builder.fillAllDimensions();
	}
	
	private static Layout of(Control component)
	{
		Layout result = new Layout();
		result.layout = new FormData();
		result.component = component;
		component.setLayoutData(result.layout);
		return result;
	}

	private Layout fillAllDimensions() {
		layout.left = new FormAttachment(0, 0);
		layout.right = new FormAttachment(100, 0);
		layout.top = new FormAttachment(0, 0);
		layout.bottom = new FormAttachment(100, 0);
		return this;
	}

	public Layout horizontal(int percentage) {
		layout.right = new FormAttachment(percentage, 0);
		return this;
	}
	
	public Layout vertical(int percentage) {
		layout.bottom = new FormAttachment(percentage, 0);
		return this;
	}

	public Layout toTheRightOf(Control leftNeighbour) {
		checkNotAttachingToItself(leftNeighbour);
		layout.left = new FormAttachment(leftNeighbour, 0);
		return this;
	}
	
	public Layout toTheBottomOf(Control neighbour) {
		checkNotAttachingToItself(neighbour);
		layout.top = new FormAttachment(neighbour, 0);
		return this;
	}
	
	public Layout toTheTopOf(Control neighbour) {
		checkNotAttachingToItself(neighbour);
		layout.bottom = new FormAttachment(neighbour, 0);
		return this;
	}

	private void checkNotAttachingToItself(Control leftNeighbour) {
		if (leftNeighbour == component)
			throw new IllegalArgumentException("Attaching component to itself is not a good idea.");
	}
}
