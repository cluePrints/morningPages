package com.mpages.parsing.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChunkFactory {
	public static Chunk fromString(String whole, String regexp)
	{
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(whole);
		
		if (!matcher.find())
			throw new IllegalStateException(whole+" has no matches of "+regexp);
		
		Chunk chunk = new Chunk(whole, matcher.start(), matcher.end());
		
		if (matcher.find())
			throw new IllegalStateException(whole+" has multiple matches of "+regexp);
		
		return chunk; 
	}
}
