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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.mpages.parsing.domain.Boundary;
import com.mpages.parsing.domain.Chunk;
import com.mpages.parsing.domain.GreedyBoundaryGeneralizer;

public class ChunkExtractorGui {
	private static final int VK_ENTER = 13;

	private java.util.List<Chunk> chunksListModel = new LinkedList<Chunk>();

	private List chunkListDisplay;
	private Text predictedBoundaryDisplay;	
	private Text addressEditor;
	
	private BrowserWrapper browser;
	private StyledText analysedTextEditor;
	private TabFolder tabFolder;

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

		Button fetchSite = new Button(rootShell, SWT.PUSH);
		fetchSite.setText("Fetch site");
		{
			FormData layoutData = new FormData();
			layoutData.right = new FormAttachment(50, 0);
			fetchSite.setLayoutData(layoutData);
		}		

		Label addressLabel = new Label(rootShell, SWT.NONE);
		addressLabel.setText("Page address:");

		addressEditor = new Text(rootShell, SWT.SINGLE | SWT.BORDER);
		addressEditor.setSize(400, 40);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(addressLabel, 5);
		layoutData.right = new FormAttachment(fetchSite, 0);
		addressEditor.setLayoutData(layoutData);

		addressEditor.setText("http://www.svdevelopment.com/ru/offer/ad/10/kiev/page/1/");

		Button allowEdit = new Button(rootShell, SWT.CHECK);
		allowEdit.setText("Allow changing HTML. Select text and hit enter to mark it as matched token.");
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(100, 0);
			allowEdit.setLayoutData(data);
		}
		
		tabFolder = new TabFolder(rootShell, SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(addressEditor, 5);
		layoutData.left = new FormAttachment(0, 0);
		layoutData.right = new FormAttachment(80, 0);
		layoutData.bottom = new FormAttachment(allowEdit, 0);
		tabFolder.setLayoutData(layoutData);
		
		
		analysedTextEditor = new StyledText(tabFolder, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		analysedTextEditor.setEditable(false);
		
		browser = new BrowserWrapper(tabFolder);		
		
		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setControl(analysedTextEditor);
		tab1.setText("HTML");
		
		TabItem tab2 = new TabItem(tabFolder, SWT.NONE);
		tab2.setControl(browser);
		tab2.setText("Browser");		
		

		predictedBoundaryDisplay = new Text(rootShell, SWT.BORDER);
		predictedBoundaryDisplay.setBounds(650, 400, 220, 100);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(tabFolder, 2);
		layoutData.right = new FormAttachment(100, 0);
		layoutData.top = new FormAttachment(0, 0);
		predictedBoundaryDisplay.setLayoutData(layoutData);

		chunkListDisplay = new List(rootShell, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(tabFolder, 5);
		layoutData.top = new FormAttachment(predictedBoundaryDisplay, 0);
		layoutData.right = new FormAttachment(100, 0);
		layoutData.bottom = new FormAttachment(100, 0);
		chunkListDisplay.setLayoutData(layoutData);

		chunkListDisplay.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				int i = chunkListDisplay.getSelectionIndex();
				if (i < 0)
					return;
				chunkListDisplay.remove(i);
				chunksListModel.remove(i);
				recalcDisplayedRegex();

			}
		});

		analysedTextEditor.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == VK_ENTER) {
					Chunk c = getSelectedChunk(analysedTextEditor);
					chunkListDisplay.add(c.toString());

					recalcDisplayedRegex();
				}
			}

			public void keyPressed(KeyEvent arg0) {

			}
		});
		
		onClickDoFetchSite(fetchSite);
		onClickChangeEditability(allowEdit, analysedTextEditor);
	}

	private void onClickDoFetchSite(Button button) {
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				String urlStr = addressEditor.getText();
				String content = readUrlContent(urlStr);
				analysedTextEditor.setText(content);
				browser.gotoUrl(urlStr);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	}
	
	private void onClickChangeEditability(Button button, final StyledText text) {
		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				text.setEditable(!text.getEditable());
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	}

	public String readUrlContent(String urlStr) {
		try {
			URL url = new URL(urlStr);
			InputStream stream = url.openConnection().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
			String res = CharStreams.toString(r);
			return res;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setAnalysedText(String defaultText) {
		analysedTextEditor.setText(defaultText);
		chunksListModel.clear();
		chunkListDisplay.removeAll();
		recalcDisplayedRegex();
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

	public void recalcDisplayedRegex() {
		Boundary predictions = new GreedyBoundaryGeneralizer().predict(chunksListModel);
		String regexp = "";
		if (predictions != null) {
			regexp = predictions.asRegex();
		}
		predictedBoundaryDisplay.setText(regexp);
	}

	public Chunk getSelectedChunk(final StyledText editor) {
		Point selection = editor.getSelection();
		Chunk c = new Chunk(editor.getText(), selection.x, selection.y);
		chunksListModel.add(c);
		return c;
	}

}