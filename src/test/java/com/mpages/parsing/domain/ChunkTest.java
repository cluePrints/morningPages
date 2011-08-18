package com.mpages.parsing.domain;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ChunkTest {
	String str = "|aa||bb||cc|";
	
	@Test
	public void chunksSortLexicographicallyByStart()
	{
		Boundary b = new Boundary("\\|","\\|");
		List<Chunk> chunks = b.split(str);
		
		Collections.swap(chunks, 1, 2);
		Assert.assertEquals("cc", chunks.get(1).getText());
		Assert.assertEquals("bb", chunks.get(2).getText());
		
		Collections.sort(chunks);
		
		Assert.assertEquals("bb", chunks.get(1).getText());
		Assert.assertEquals("cc", chunks.get(2).getText());
	}
}
