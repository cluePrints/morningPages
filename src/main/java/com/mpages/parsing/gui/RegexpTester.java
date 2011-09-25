package com.mpages.parsing.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class RegexpTester extends Composite{
	private Text regexp;
	private List matches;
	private String textToMatchAgainst;
	
	public RegexpTester(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		
		Composite rootShell = this;
		regexp = new Text(rootShell, SWT.BORDER);
		Layout.fillAllDimensionsWith(regexp).vertical(10);
		
		matches = new List(this, SWT.BORDER);
		Layout.fillAllDimensionsWith(matches).toTheBottomOf(regexp);
		
		regexp.addModifyListener(new ModifyListener() {			
			public void modifyText(ModifyEvent arg0) {
				String regexpStr = regexp.getText();
				if (regexpStr.length() <3)
					return;
				
				matches.removeAll();
				
				Pattern pattern;
				try {
					pattern = Pattern.compile(regexpStr);
				} catch (PatternSyntaxException ex) {
					return;
				}
				Matcher matcher = pattern.matcher(textToMatchAgainst);
				while (matcher.find()) {
					String match;
					if (matcher.groupCount()>0) {
						match = matcher.group(1);
					} else {
						match = matcher.group();
					}						
					matches.add(match);
				}
			}
		});
	}
	
	public void setTextToMatchAgainst(String textToMatchAgainst) {
		this.textToMatchAgainst = textToMatchAgainst;
	}
}
