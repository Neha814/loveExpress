package com.appdupe.pair.model;

import java.io.Serializable;

public class Latlong_Data implements Serializable {
	private static final long serialVersionUID = 6294846507519797305L;
	private String city_name;
	private String Lat;
	private String Long;
	private int locationId;

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getLat() {
		return Lat;
	}

	public void setLat(String lat) {
		Lat = lat;
	}

	public String getLong() {
		return Long;
	}

	public void setLong(String l) {
		Long = l;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

}
