package com.appdupe.pair.model;

import java.util.ArrayList;

public class PreferenceModel {
	private int prefLowerAge, prefRadius, prefSex, prefUpperAge, sex, errFlag,
			searchon;
	private String prLat, preLong, loginLat, loginLong;
	private ArrayList<Latlong_Data> data = new ArrayList<Latlong_Data>();

	public int getErrFlag() {
		return errFlag;
	}

	public void setErrFlag(int errFlag) {
		this.errFlag = errFlag;
	}

	public int getPrefLowerAge() {
		return prefLowerAge;
	}

	public void setPrefLowerAge(int prefLowerAge) {
		this.prefLowerAge = prefLowerAge;
	}

	public int getPrefRadius() {
		return prefRadius;
	}

	public void setPrefRadius(int prefRadius) {
		this.prefRadius = prefRadius;
	}

	public int getPrefSex() {
		return prefSex;
	}

	public void setPrefSex(int prefSex) {
		this.prefSex = prefSex;
	}

	public int getPrefUpperAge() {
		return prefUpperAge;
	}

	public void setPrefUpperAge(int prefUpperAge) {
		this.prefUpperAge = prefUpperAge;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSearchon() {
		return searchon;
	}

	public void setSearchon(int searchon) {
		this.searchon = searchon;
	}

	public ArrayList<Latlong_Data> getData() {
		return data;
	}

	public void setData(ArrayList<Latlong_Data> data) {
		this.data = data;
	}

	public String getPrLat() {
		return prLat;
	}

	public void setPrLat(String prLat) {
		this.prLat = prLat;
	}

	public String getPreLong() {
		return preLong;
	}

	public void setPreLong(String preLong) {
		this.preLong = preLong;
	}

	public String getLoginLat() {
		return loginLat;
	}

	public void setLoginLat(String loginLat) {
		this.loginLat = loginLat;
	}

	public String getLoginLong() {
		return loginLong;
	}

	public void setLoginLong(String loginLong) {
		this.loginLong = loginLong;
	}
}
