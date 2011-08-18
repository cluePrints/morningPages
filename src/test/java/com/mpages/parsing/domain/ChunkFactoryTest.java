package com.mpages.parsing.domain;

import org.junit.Assert;
import org.junit.Test;

public class ChunkFactoryTest {
	String table="notuseful<b>aaa</b><br>eee"
				+"blablabla<b>bbb</b><br>ggg"
				+"blablabla3<b>ccc</b>1111<br>fff";
	@Test
	public void shouldRecognizeUniqueChunk()
	{
		Assert.assertEquals("aaa", ChunkFactory.fromString(table, "aaa").getText());
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldFailOnMultipleChunksMatching()
	{
		ChunkFactory.fromString(table, "bla");
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldFailOnNoneChunksMatching()
	{
		ChunkFactory.fromString(table, "----");
	}
}
