package com.mpages.parsing.domain;

import junit.framework.Assert;

import org.junit.Test;

public class RegexEscaperTest {
	private RegexEscaper unit = new RegexEscaper();
	
	@Test
	public void shouldMergeSimilarSymbols() {
		String regexp = "\t\t\t\t\t\t\t";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals("\\t{7}", result);
	}
	
	@Test
	public void shouldNotMergeNotSimilarSymbols() {
		String regexp = "AbC";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals(regexp, result);
	}
	
	@Test
	public void shouldWorkOkWithEmptyStrings() {
		String regexp = "";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals(regexp, result);
	}
	
	@Test
	public void shouldWorkOkWithOneCharString() {
		String regexp = "a";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals(regexp, result);
	}
	
	@Test
	public void shouldWorkOkWithTwoCharString() {
		String regexp = "a2";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals(regexp, result);
	}
	
	@Test
	public void shouldHandleMixedCase() {
		String regexp = "\n\n\nABC\t\t\t";
		String result = unit.toHumanReadable(regexp);
		Assert.assertEquals("\\n{3}ABC\\t{3}", result);
	}

}
