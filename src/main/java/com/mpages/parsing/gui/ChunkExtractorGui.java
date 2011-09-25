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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mpages.parsing.domain.Chunk;

public class ChunkExtractorGui {
	private HtmlBrowser browser;
	private ChunkBrowser chunkAnalyser;
	private RegexpTester regexpTester;
	
	private Display rootDisplay;
	private Shell rootShell;

	public void init() {
		if (rootDisplay != null) {
			throw new IllegalStateException(
					"Component seem to be initialized already");
		}

		rootDisplay = new Display();
		rootShell = new Shell(rootDisplay);
		rootShell.setLayout(new FormLayout());

		rootShell.setText("Hello, world!");		
		
		browser = new HtmlBrowser(rootShell);		
		Layout.fillAllDimensionsWith(browser).horizontal(50);

		chunkAnalyser = new ChunkBrowser(rootShell);
		Layout.fillAllDimensionsWith(chunkAnalyser).vertical(50).toTheRightOf(browser);
		
		regexpTester = new RegexpTester(rootShell);
		Layout.fillAllDimensionsWith(regexpTester).toTheBottomOf(chunkAnalyser).toTheRightOf(browser);

		browser.onChunkIdentified(new ChunkIdentifiedListener() {			
			public void identified(Chunk chunk) {
				chunkAnalyser.add(chunk);
			}
		});
		
		browser.onSourceChanged(new ChunkSourceListener() {			
			public void changedTo(String newSource) {
				regexpTester.setTextToMatchAgainst(newSource);
				chunkAnalyser.clear();
			}
		});
	}

	public void setAnalysedText(String defaultText) {
		browser.setText(defaultText);
		chunkAnalyser.clear();
	}

	public void enterEventsDispatchLoop() {
		rootShell.open();

		while (!rootShell.isDisposed()) {
			if (!rootDisplay.readAndDispatch()) {
				// If no more entries in event queue
				rootDisplay.sleep();
			}
		}

		rootDisplay.dispose();
	}

}