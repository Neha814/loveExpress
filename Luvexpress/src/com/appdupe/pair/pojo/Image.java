package com.appdupe.pair.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

@SerializedName("image")
@Expose
private String image;

/**
* 
* @return
* The image
*/
public String getImage() {
return image;
}

/**
* 
* @param image
* The image
*/
public void setImage(String image) {
this.image = image;
}

}
