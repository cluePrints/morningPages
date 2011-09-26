package com.mpages.parsing.geo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import com.google.common.io.CharStreams;

public class LocationSvcReader {
	public String getByAddress(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String query = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + encodedAddress+"&sensor=false";
			URL url = new URL(query);
			InputStream inputStream = url.openConnection().getInputStream();
			String content = CharStreams.toString(new BufferedReader(new InputStreamReader(inputStream)));
			return String.valueOf(content);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
