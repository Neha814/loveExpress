package com.appdupe.pair.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FindMatchData {

	@SerializedName("matches")
	private ArrayList<MatchesData> matches;

	public ArrayList<MatchesData> getMatches() {
		return matches;
	}

	public void setMatches(ArrayList<MatchesData> matches) {
		this.matches = matches;
	}

	@SerializedName("errNum")
	@Expose
	private String errNum;
	@SerializedName("errFlag")
	@Expose
	private String errFlag;
	@SerializedName("errMsg")
	@Expose
	private String errMsg;

	public String getErorrMassage() {
		return errMsg;
	}

	public void setErorrMassage(String erorrMassage) {
		this.errMsg = erorrMassage;
	}

	public String getErrNum() {
		return errNum;
	}

	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}

	public String getErrFlag() {
		return errFlag;
	}

	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}

}
