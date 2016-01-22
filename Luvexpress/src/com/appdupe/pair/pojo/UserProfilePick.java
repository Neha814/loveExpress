package com.appdupe.pair.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserProfilePick 
{ 
	@SerializedName("data")
    private ArrayList<PickUrl> data;

	public ArrayList<PickUrl> getData() {
		return data;
	}

	public void setData(ArrayList<PickUrl> data) {
		this.data = data;
	}

	
	
	
}
