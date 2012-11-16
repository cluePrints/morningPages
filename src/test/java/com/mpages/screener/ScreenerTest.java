package com.mpages.screener;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ScreenerTest 
{
	private Screener unit = new Screener();
	
	@Test
	public void shouldReplaceEachPiece()
	{
		List<String> in = Arrays.asList("/John & Marry/");
		List<String> replacement = Arrays.asList("berry", "bear", "moon");
		Iterator<String> result = unit.screen(in, replacement);
		String item = result.next();
		Assert.assertEquals("/berry & bear/", item);
	}
	
	@Test
	public void shouldNotTouchNumbers()
	{
		List<String> in = Arrays.asList("/John 333/");
		List<String> replacement = Arrays.asList("berry", "bear", "moon");
		Iterator<String> result = unit.screen(in, replacement);
		String item = result.next();
		Assert.assertEquals("/berry 333/", item);
	}
}
