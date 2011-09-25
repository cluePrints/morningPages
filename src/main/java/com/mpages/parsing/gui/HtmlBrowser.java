package com.mpages.parsing.gui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.io.CharStreams;
import com.mpages.parsing.domain.Chunk;

public class HtmlBrowser extends Composite {
	private static final int VK_ENTER = 13;
	
	private BrowserWrapper browser;	
	private Text addressEditor;
	private TabFolder tabFolder;
	private StyledText analysedTextEditor;
	
	private ChunkIdentifiedListener chunkListener;
	
	public HtmlBrowser(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		
		Composite rootShell = this;
		Button fetchSite = new Button(rootShell, SWT.PUSH);
		fetchSite.setText("Fetch site");
		{
			FormData layoutData = new FormData();
			layoutData.right = new FormAttachment(100, 0);
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
		Layout.fillAllDimensionsWith(tabFolder)
			.toTheBottomOf(addressEditor)
			.toTheTopOf(allowEdit);		
		
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
		
		onClickDoFetchSite(fetchSite);
		onClickChangeEditability(allowEdit, analysedTextEditor);
		
		analysedTextEditor.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == VK_ENTER) {					
					Chunk chunk = getSelectedChunk(analysedTextEditor);					
					chunkListener.identified(chunk);
				}
			}

			public void keyPressed(KeyEvent arg0) {

			}
		});	
	}
	
	public void onChunkIdentified(ChunkIdentifiedListener l)
	{
		this.chunkListener = l;
	}
	
	public void setText(String text)
	{
		analysedTextEditor.setText(text);
	}

	private Chunk getSelectedChunk(final StyledText editor) {
		Point selection = editor.getSelection();
		return new Chunk(editor.getText(), selection.x, selection.y);
	}
	
	private String readUrlContent(String urlStr) {
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
}
