package com.mpages.parsing.geo;

import org.junit.Assert;
import org.junit.Test;

public class LocationSvcIntegrationTest {
	@Test
	public void shouldReturnSomething()
	{
		LocationSvcReader svc = new LocationSvcReader();
		String res = svc.getByAddress("1600+Amphitheatre+Parkway,+Mountain+View,+CA");
		Assert.assertTrue(res.contains("status>OK</status"));
	}
}
