package com.mpages.parsing.gui;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

import com.mpages.hackish.Classloaders;

public class ChunkExtractorGuiTest {
	@Test
	public void shouldInitializeWithoutCrashes() throws Exception
	{
		Classloaders.appendToLibraryPathsList("");
		ChunkExtractorGui gui = new ChunkExtractorGui();
		gui.init();
		
		String defaultText = "a,a,a\nb,b,b\n";
		gui.setAnalysedText(defaultText);
	}
}
