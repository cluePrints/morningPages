package com.mpages.parsing.domain;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class RuleRecognizerTest {

	GreedyBoundaryGeneralizer unit = new GreedyBoundaryGeneralizer();
	
	@Test
	public void shouldRecognizeBoundariesBasedOnExamples()
	{
		String whole="notuseful<b>aaa</b><br>eee"
				+"blablabla<b>bbb</b><br>ggg"
				+"blablabla3<b>ccc</b>1111<br>fff";
		
		Chunk aChunk = ChunkFactory.fromString(whole, "aaa");
		Chunk cChunk = ChunkFactory.fromString(whole, "ccc");

		Boundary boundary = unit.predict(Arrays.asList(aChunk, cChunk));
		
		Assert.assertEquals("<b>", boundary.getLeftRegex());
		Assert.assertEquals("</b>", boundary.getRightRegex());
	}
	
	@Test
	public void shouldStopOnTextLimits()
	{
		String txt="b>111</b><b>222</b";
		Chunk aChunk = ChunkFactory.fromString(txt, "111");
		Chunk cChunk = ChunkFactory.fromString(txt, "222");
		
		Boundary boundary = unit.predict(Arrays.asList(aChunk, cChunk));
		
		Assert.assertEquals("b>", boundary.getLeftRegex());
		Assert.assertEquals("</b", boundary.getRightRegex());
	}
	
	@Test
	public void shouldReturnNullWhenRecognizingNotPossible()
	{
		String txt="111222";
		Chunk aChunk = ChunkFactory.fromString(txt, "111");
		Chunk cChunk = ChunkFactory.fromString(txt, "222");
		
		Boundary boundary = unit.predict(Arrays.asList(aChunk, cChunk));
		
		Assert.assertNull(boundary);
	}
	
	@Test
	public void shouldNotBotherWithRecognizingIfOnlyOneExampleIsPresent()
	{
		String txt="111222";
		Chunk aChunk = ChunkFactory.fromString(txt, "111");
		
		Boundary boundary = unit.predict(Arrays.asList(aChunk));
		
		Assert.assertNull(boundary);
	}
	
	@Test
	@Ignore("this is kind of todo :) ")
	public void shouldCompileRegexToMatchPatterns()
	{
		String txt="a111b222c";
		Chunk aChunk = ChunkFactory.fromString(txt, "111");
		Chunk cChunk = ChunkFactory.fromString(txt, "222");
		
		Boundary boundary = unit.predict(Arrays.asList(aChunk, cChunk));
		
		Assert.assertEquals("[a-b]", boundary.getLeftRegex());
		Assert.assertEquals("[b-c]", boundary.getRightRegex());
	}
}
