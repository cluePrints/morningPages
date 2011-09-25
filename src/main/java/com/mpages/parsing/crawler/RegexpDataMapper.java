package com.mpages.parsing.crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class RegexpDataMapper<T> {
	protected String dataRegexp;	

	public RegexpDataMapper() {
		// TODO: check if this thing is still required to satisfy compiler on windows
	}
	
	public List<T> parseData(String content) {
		Pattern pattern = Pattern.compile(dataRegexp, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		if (matcher.groupCount() == 0)
			throw new IllegalStateException(
					"Group count should be more then 0 - they point to data to be captured.");

		List<T> results = new LinkedList<T>();
		while (matcher.find()) {
			T result = mapMatch(matcher);
			results.add(result);
		}
		return results;
	}
	
	protected abstract T mapMatch(Matcher matcher);
}
