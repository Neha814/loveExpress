package com.appdupe.pair.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserLikes 
{
	 @SerializedName("data")
	ArrayList<UserLikesData> userlikesDataList;

	public ArrayList<UserLikesData> getUserlikesDataList() {
		return userlikesDataList;
	}

	public void setUserlikesDataList(ArrayList<UserLikesData> userlikesDataList) {
		this.userlikesDataList = userlikesDataList;
	}
	 
   
}