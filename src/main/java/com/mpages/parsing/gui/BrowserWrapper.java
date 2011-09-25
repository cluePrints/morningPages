package com.mpages.parsing.gui;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BrowserWrapper extends Composite {
	private Browser browser;
	private Text errText;

	public BrowserWrapper(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());

		try {
			browser = new Browser(this, SWT.NONE);
		} catch (SWTError err) {
			errText = new Text(this, SWT.MULTI);
			errText.setText("Error while creating Browser component: \n"+
			err.getMessage()+
			"\n\nSee https://help.ubuntu.com/community/WebKit for more info on tackling that." +
			"\nI never had it working on *nix yet:)"+
			"\n\nDetails: " +trace(err));
		}
	}

	public String trace(SWTError err) {
		StringWriter wr = new StringWriter();
		err.printStackTrace(new PrintWriter(wr));
		return wr.toString();
	}
	
	public void gotoUrl(String url)
	{
		if (browser != null) {
			browser.setUrl(url);
		}			
	}
}
