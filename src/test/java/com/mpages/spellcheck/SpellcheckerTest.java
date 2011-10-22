package com.mpages.spellcheck;

import org.junit.Assert;
import org.junit.Test;

public class SpellcheckerTest {
	Spellchecker unit = new Spellchecker();
	@Test
	public void shouldDetectFailureForSimpleTypo() throws Exception
	{
		Assert.assertEquals(false, unit.isOk("appple"));
	}
}
