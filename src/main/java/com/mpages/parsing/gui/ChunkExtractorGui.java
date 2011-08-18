package com.mpages.parsing.gui;

/**
 * Stuff in the right hand side of the rule is chunk. 
 * In most common case we've got chunk limited by 
 * other chunks on the left and right.
 * 
 * We select bunch of examples and then try to generalize that based on:
 * 1.  common fragments limiting the data:
 *     [start]data[end]
 * 2. or user might enter regexp 
 */

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mpages.parsing.domain.Chunk;
import com.mpages.parsing.domain.GreedyBoundaryGeneralizer;

public class ChunkExtractorGui {
	Text boundary;
	java.util.List<Chunk> chunks = new LinkedList<Chunk>();
  public void display() {
    Display display = new Display();
    Shell shell = new Shell(display);
    
    shell.setText("Hello, world!");
    final StyledText editor = new StyledText(shell, SWT.BORDER);
    editor.setSize(600, 400);
    editor.setText("<table>\n<tr><td>apple</td><td>14</td></tr></br>\n" +
    		"<tr><td>orange</td><td>11</td></tr></br>\n" +
    		"<tr><td>children</td><td>81</td></tr></br>\n" +
    		"<tr><td>air</td><td>$93.1</td></tr></br>\n</table>");
    editor.setEditable(false);
    
    
    boundary = new Text(shell, SWT.BORDER);
    boundary.setBounds(650, 400, 220, 100);
    
    final List list = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    list.setBounds(600, 0, 400, 400);
    list.addMouseListener(new MouseListener() {
		
		public void mouseUp(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public void mouseDown(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public void mouseDoubleClick(MouseEvent arg0) {
			int i = list.getSelectionIndex();
			list.remove(i);
			chunks.remove(i);
			refresh();
			
		}
	});
    shell.open();

    
    
    editor.addKeyListener(new KeyListener() {
		
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == 13) {
				Point selection = editor.getSelection();
				Chunk c = new Chunk(editor.getText(), selection.x, selection.y);
				chunks.add(c);
				editor.setSelection(-1);
				list.add(c.toString());
				
				refresh();
			}
		}
		
		public void keyPressed(KeyEvent arg0) {
			
		}
	});

    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in event queue
        display.sleep();
      }
    }

    display.dispose();
  }
  

	public void refresh() {
		boundary.setText(new GreedyBoundaryGeneralizer().predict(chunks).toString());
	}

}