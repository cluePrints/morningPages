package com.mpages.parsing.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Boundary {
	private String leftRegex;
	private String rightRegex;
	
	public Boundary(String leftRegex, String rightRegex) {
		super();
		this.leftRegex = leftRegex;
		this.rightRegex = rightRegex;
	}
	
	public String getLeftRegex() {
		return leftRegex;
	}
	
	public String getRightRegex() {
		return rightRegex;
	}
	
	public List<Chunk> split(String text)
	{
		Pattern lPattern = Pattern.compile(leftRegex);
		Pattern rPattern = Pattern.compile(rightRegex);
		
		Matcher lMatcher = lPattern.matcher(text);
		Matcher rMatcher = rPattern.matcher(text);
		
		LinkedList<Chunk> result = new LinkedList<Chunk>();
		
		int lPos = 0;
		int rPos = 0;
		boolean lFound;
		boolean rFound = false;
		do {
			lFound = lMatcher.find(lPos);
			if (lFound) {
				lPos = lMatcher.end();
				rPos = lMatcher.end()+1; 
				rFound = rMatcher.find(rPos);
				if (rFound) {
					Chunk chunk = new Chunk(text, lMatcher.end(), rMatcher.start());
					lPos = rMatcher.end();
					result.add(chunk);
				}
			}
		} while (lFound && rFound);
		return result;
	}
	
	public String asRegex()
	{
		return leftRegex + ".*" + rightRegex;
	}
	
	@Override
	public String toString() {
		return "[" + leftRegex + "]...["
				+ rightRegex + "]";
	}
	
}
