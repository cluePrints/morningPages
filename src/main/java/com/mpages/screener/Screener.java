package com.mpages.screener;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Screener 
{
	public Iterator<String> screen(Iterable<String> data, Iterable<String> replacements)
	{
		return screen(data.iterator(), replacements.iterator());
	}
	
	public Iterator<String> screen(Iterator<String> data, final Iterator<String> replacementTerms)
	{
		Function<String, String> screenFunction = new Function<String, String>()
		{
			// TODO: [IS] for larger files we'd need some throttling here - mb use guava cache
			Map<String, String> replacements = Maps.newHashMap();
			
			public String apply(String line) 
			{
				Set<String> parts = split(line);
				return screen(replacementTerms, replacements, line, parts);
			}
		};
		
		return Iterators.transform(data, screenFunction);
	}

	private String screen(Iterator<String> replacementTerms, Map<String, String> replacements,
			String line, Set<String> parts)  
	{
		for (String part : parts)
		{
			if (part.length() < 3)
				continue;
			
			String repl = getReplacement(replacementTerms, replacements, part);
			if (part.equals(repl))
				continue;
				
			int i=0;
			while (line.indexOf(part) >= 0)
			{
				if (i++>10)
					break;
				line = line.replace(part, repl);
			}
		}
		return line;
	}

	private String getReplacement(Iterator<String> replacementTerms,
			Map<String, String> replacements, String part) 
	{
		if (part.matches("\\d+"))
			return part;
		
		if (part.matches("\\D\\d+"))
			return part;
		
		String repl = replacements.get(part);
		
		while (repl == null || repl.contains(part))
		{
			repl = replacementTerms.next();
			replacements.put(part, repl);
		}
		return repl;
	}

	private Set<String> split(String line)
	{
		return split(Collections.singletonList(line));
	}
	
	private Set<String> split(List<String> lines) 
	{
		Set<String> parts = split(lines, "/");
		parts = split(parts, "\\s");
		parts = split(parts, "_");
		parts = split(parts, "\\.");
		parts = split(parts, "-");
		parts = split(parts, "\\$");
		parts = split(parts, "\\(");
		parts = split(parts, "\\)");
		parts = split(parts, "&");
		parts = split(parts, "#");
		parts = split(parts, "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"); // CamelCase
		parts = split(parts, "(?<=\\d)(?=\\p{L})|(?<=\\p{L})(?=\\d)"); // 29nov20 dates
		return parts;
	}

	private Set<String> split(Collection<String> readLines, String pattern) 
	{
		Set<String> parts = Sets.newTreeSet();
		for (String line : readLines)
		{
			Collections.addAll(parts,  line.split(pattern));
		}
		return parts;
	}
}
