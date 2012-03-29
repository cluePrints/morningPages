package com.mpages.treededup;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.StringInputStream;


public class HashCalculatorTest {
	@Test
	public void shouldCalculateDigestForAPart() throws Exception {
		String full = "xxxx-12345-yyyy";
		String expected = DigestUtils.md5Hex("12345");
		StringInputStream is = new StringInputStream(full);
		FilePart fp = new FilePart(null, "", 6, 5);
		
		HashCalculator unit = new HashCalculator();
		String actual = unit.partDigest(fp, is);
		Assert.assertEquals(expected, actual);
	}
}
