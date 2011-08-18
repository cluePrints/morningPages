package com.mpages.parsing.domain;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BoundaryTest {
	Boundary unit = new Boundary("<b>", "</b>");
	
	@Test
	public void shouldSkipIncompleteMatch() {		
		List<Chunk> chunks = unit.split("b>aaabbb</b>");
		Assert.assertEquals(0, chunks.size());	
	}
	
	@Test
	public void shouldFindOverlappingMatchProperly() {		
		List<Chunk> chunks = unit.split("<b>aaa<b>bbb</b>");
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals("aaa<b>bbb", chunks.get(0).getText());
	}
	
	@Test
	public void shouldFindTwoMatches() {		
		List<Chunk> chunks = unit.split("<b>aaa</b><b>bbb</b>");
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("aaa", chunks.get(0).getText());
		Assert.assertEquals("bbb", chunks.get(1).getText());
	}
	
	@Test
	public void shouldFindSimpleSingleMatch() {		
		List<Chunk> chunks = unit.split("<b>abc</b>");
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals("abc", chunks.get(0).getText());
	}
	
	@Test
	public void shouldSurviveNoMatches() {		
		List<Chunk> chunks = unit.split("");
		Assert.assertEquals(0, chunks.size());
	}
}
