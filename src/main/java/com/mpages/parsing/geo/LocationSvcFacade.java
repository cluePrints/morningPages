package com.mpages.parsing.geo;

public class LocationSvcFacade {
	LocationParser parser = new LocationParser();
	LocationSvcReader reader = new LocationSvcReader();
	
	public Location get(String address)
	{
		String content = reader.getByAddress(address);
		return parser.parse(content);
	}
}
