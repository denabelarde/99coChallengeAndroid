package com.ninetyninecochallenge.places.dto;

public class StoreDto {
	String place_id;
	String name;
	double lat;
	double longhi;
	String icon;

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLonghi() {
		return longhi;
	}

	public void setLonghi(double longhi) {
		this.longhi = longhi;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
