package com.mpages.parsing.geo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class LocationParserTest {
	LocationParser unit = new LocationParser();
	
	@Test
	public void shouldReturnInvalidLocationOnParsingXmlTroubleResult() throws Exception
	{
		Location result = parse("example_err.xml");
		Assert.assertSame(Location.INVALID, result);
	}
	
	@Test
	public void shouldReturnValidLocationOnParsingXmlOkResult() throws Exception
	{
		Location result = parse("example_one_result.xml");
		Assert.assertNotSame(Location.INVALID, result);
	}
	
	@Test
	public void shouldReturnValidLocationVals() throws Exception
	{
		Location result = parse("example_one_result.xml");
		Assert.assertEquals(50.4517389, result.getLatitude(), 0.00001);
		Assert.assertEquals(30.5265069, result.getLongitude(), 0.00001);
	}

	@Test
	public void whatToDoOnMultipleLocationsReturned() throws Exception
	{
		throw new RuntimeException("Think it over");
	}
	
	private Location parse(String exampleName) throws IOException {
		URL url = this.getClass().getResource(exampleName);
		String name = url.getFile();
		String content = Files.toString(new File(name), Charsets.UTF_8);
		Location result = unit.parse(content);
		return result;
	}
}
