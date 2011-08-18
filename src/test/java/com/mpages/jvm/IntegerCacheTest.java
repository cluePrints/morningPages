package com.mpages.jvm;

import org.junit.Assert;
import org.junit.Test;

public class IntegerCacheTest {
	@Test
	// -Djava.lang.Integer.IntegerCache.high
	// -XX:AutoBoxCacheMax
	public void testIntegersCacheIsResizable() {
		Integer a1 = 1;
		Integer a2 = 1;
		Assert.assertTrue(a1 == a2);
		
		Integer b1 = 130;
		Integer b2 = 130;
		Assert.assertFalse(b1 == b2);
	}
}
