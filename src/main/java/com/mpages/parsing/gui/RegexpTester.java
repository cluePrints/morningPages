package com.mpages.parsing.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
				recalcMatches();
			}
		});
		
		onClickUpdateHint(matches);
	}
	
	private void onClickUpdateHint(final List component) {
		component.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				int i = component.getSelectionIndex();
				if (i<0)
					return;
				component.setToolTipText(component.getSelection()[0]);
			}
		});
	}
	
	private void recalcMatches() {
		String regexpStr = regexp.getText();
		if (regexpStr.length() <3)
			return;
		
		matches.removeAll();
		
		Pattern pattern;
		try {
			pattern = Pattern.compile(regexpStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		} catch (PatternSyntaxException ex) {
			return;
		}
		Matcher matcher = pattern.matcher(textToMatchAgainst);
		while (matcher.find()) {
			String match;
			if (matcher.groupCount()>0) {
				StringBuilder b = new StringBuilder();
				b.append(escape(matcher.group(1)));
				for (int i=2; i<=matcher.groupCount(); i++) {
					b.append("\n");
					b.append(escape(matcher.group(i)));
				}
				match = b.toString();
			} else {
				match = escape(matcher.group());
			}			
			matches.add(match);
		}
	}

	private String escape(String match) {
		match = match.replace("\n", "\\n").replace("\t", "\\t");
		return match;
	}
	
	public void setTextToMatchAgainst(String textToMatchAgainst) {
		this.textToMatchAgainst = textToMatchAgainst;
	}
}
