package com.mpages.parsing.domain;

public class RegexEscaper {
	public String toHumanReadable(String regexp) {
		String regexp2 = dedup(regexp);
		return regexp2.replace("\n", "\\n").replace("\t", "\\t");
	}

	private String dedup(String input) {
		if (input.length()<2)
			return input;
		
		StringBuilder b = new StringBuilder();
		
		int dups = 1;				
		int i = 0;
		
		while (i<input.length()-1) {
			char nextChar = input.charAt(i+1);
			char currChar = input.charAt(i);
			boolean charsAreSimilar = nextChar == currChar;
			boolean stringEnded = i == input.length()-2;
			boolean charsWereSimilar = dups > 1;
			
			if (charsAreSimilar) {
				if (!charsWereSimilar) {
					dups = 2; 
				} else {
					dups ++;
				}
			}
						
			if (!charsAreSimilar || stringEnded) {
				b.append(currChar);				
				if (charsWereSimilar) {
					b.append("{").append(dups).append("}");
					dups=1;
				}
			}
			
			if (stringEnded && !charsAreSimilar) {
				b.append(nextChar);
			}
			i++;
		}
		
		String regexp2 = b.toString();
		return regexp2;
	}
}
