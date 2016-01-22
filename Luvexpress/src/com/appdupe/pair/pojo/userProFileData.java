package com.appdupe.pair.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class userProFileData {

	@SerializedName("errNum")
	@Expose
	private String errNum;
	@SerializedName("errFlag")
	@Expose
	private String errFlag;
	@SerializedName("errMsg")
	@Expose
	private String errMsg;
	@SerializedName("profilePic")
	@Expose
	private String profilePic;
	@SerializedName("age")
	@Expose
	private String age;
	@SerializedName("lati")
	@Expose
	private String lati;
	@SerializedName("long")
	@Expose
	private String _long;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("lastActive")
	@Expose
	private String lastActive;
	@SerializedName("persDesc")
	@Expose
	private String persDesc;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("images")
	@Expose
	private String[] images;

	/**
	* 
	* @return
	* The errNum
	*/
	public String getErrNum() {
	return errNum;
	}

	/**
	* 
	* @param errNum
	* The errNum
	*/
	public void setErrNum(String errNum) {
	this.errNum = errNum;
	}

	/**
	* 
	* @return
	* The errFlag
	*/
	public String getErrFlag() {
	return errFlag;
	}

	/**
	* 
	* @param errFlag
	* The errFlag
	*/
	public void setErrFlag(String errFlag) {
	this.errFlag = errFlag;
	}

	/**
	* 
	* @return
	* The errMsg
	*/
	public String getErrMsg() {
	return errMsg;
	}

	/**
	* 
	* @param errMsg
	* The errMsg
	*/
	public void setErrMsg(String errMsg) {
	this.errMsg = errMsg;
	}

	/**
	* 
	* @return
	* The profilePic
	*/
	public String getProfilePic() {
	return profilePic;
	}

	/**
	* 
	* @param profilePic
	* The profilePic
	*/
	public void setProfilePic(String profilePic) {
	this.profilePic = profilePic;
	}

	/**
	* 
	* @return
	* The age
	*/
	public String getAge() {
	return age;
	}

	/**
	* 
	* @param age
	* The age
	*/
	public void setAge(String age) {
	this.age = age;
	}

	/**
	* 
	* @return
	* The lati
	*/
	public String getLati() {
	return lati;
	}

	/**
	* 
	* @param lati
	* The lati
	*/
	public void setLati(String lati) {
	this.lati = lati;
	}

	/**
	* 
	* @return
	* The _long
	*/
	public String getLong() {
	return _long;
	}

	/**
	* 
	* @param _long
	* The long
	*/
	public void setLong(String _long) {
	this._long = _long;
	}

	/**
	* 
	* @return
	* The status
	*/
	public String getStatus() {
	return status;
	}

	/**
	* 
	* @param status
	* The status
	*/
	public void setStatus(String status) {
	this.status = status;
	}

	/**
	* 
	* @return
	* The lastActive
	*/
	public String getLastActive() {
	return lastActive;
	}

	/**
	* 
	* @param lastActive
	* The lastActive
	*/
	public void setLastActive(String lastActive) {
	this.lastActive = lastActive;
	}

	/**
	* 
	* @return
	* The persDesc
	*/
	public String getPersDesc() {
	return persDesc;
	}

	/**
	* 
	* @param persDesc
	* The persDesc
	*/
	public void setPersDesc(String persDesc) {
	this.persDesc = persDesc;
	}

	/**
	* 
	* @return
	* The firstName
	*/
	public String getFirstName() {
	return firstName;
	}

	/**
	* 
	* @param firstName
	* The firstName
	*/
	public void setFirstName(String firstName) {
	this.firstName = firstName;
	}
	/**
	 * 
	 * @return The image
	 */
	public String[] getImages() {
		return images;
	}

	/**
	 * 
	 * @param image
	 *            The image
	 */
	public void setImages(String[] image) {
		this.images = image;
	}
	
}
