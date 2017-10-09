package id.pptik.semutangkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile_ {

    @SerializedName("CheckPoint")
    @Expose
    private Integer checkPoint;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;

    public Integer getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(Integer checkPoint) {
        this.checkPoint = checkPoint;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}