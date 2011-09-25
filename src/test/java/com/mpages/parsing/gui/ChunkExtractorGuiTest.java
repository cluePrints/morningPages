package com.mpages.parsing.gui;

import org.junit.Test;

public class ChunkExtractorGuiTest {
	@Test
	public void test()
	{
		ChunkExtractorGui gui = new ChunkExtractorGui();
		gui.init();
		String defaultText = "<table>\n<tr><td>apple</td><td>14</td></tr></br>\n"
				+ "<tr><td>orange</td><td>11</td></tr></br>\n"
				+ "<tr><td>children</td><td>81</td></tr></br>\n"
				+ "<tr><td>air</td><td>$93.1</td></tr></br>\n</table>";
		gui.setAnalysedText(defaultText);
		gui.enterEventsDispatchLoop();
	}
}
