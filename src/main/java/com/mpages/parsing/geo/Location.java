package com.mpages.parsing.geo;

public class Location {
	private double latitude;
	private double longitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public static final Location INVALID = new Location(){
		
	};
	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
}
