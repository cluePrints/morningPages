package com.mpages.parsing.gui;

import org.junit.Test;

public class ChunkExtractorGuiTest {
	@Test
	public void shouldInitializeWithoutCrashes() throws Exception
	{
		ChunkExtractorGui gui = new ChunkExtractorGui();
		gui.init();
		String defaultText = "a,a,a\nb,b,b\n";
		gui.setAnalysedText(defaultText);
	}
}
