package com.mpages.parsing.crawler;

import java.util.regex.Matcher;

public class ToStringRegexpMapper extends RegexpDataMapper<String>{
	
	public ToStringRegexpMapper(String dataRegexp) {
		this.dataRegexp = dataRegexp;
	}
	
	protected String mapMatch(Matcher matcher) {
		return matcher.group(1);
	}
}
