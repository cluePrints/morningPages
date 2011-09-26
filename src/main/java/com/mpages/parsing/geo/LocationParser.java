package com.mpages.parsing.geo;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class LocationParser {
	public Location parse(String xml) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			String requestType = xpath.evaluate("/GeocodeResponse/status", src(xml));
			if ("REQUEST_DENIED".equalsIgnoreCase(requestType)) {
				return Location.INVALID;
			}
			String lat = xpath.evaluate("/GeocodeResponse/result/geometry/location/lat", src(xml));
			String lng = xpath.evaluate("/GeocodeResponse/result/geometry/location/lng", src(xml));
			Location result = new Location();
			result.setLatitude(Double.valueOf(lat));
			result.setLongitude(Double.valueOf(lng));
			return result;
		} catch (XPathException ex) {
			throw new RuntimeException(ex);
		}
	}

	public InputSource src(String xml) {
		return new InputSource(new StringReader(xml));
	}
}
