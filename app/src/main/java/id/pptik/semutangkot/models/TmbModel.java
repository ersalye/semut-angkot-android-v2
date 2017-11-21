package id.pptik.semutangkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.pptik.semutangkot.models.angkot.Location;

public class TmbModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("buscode")
    @Expose
    private String buscode;
    @SerializedName("koridor")
    @Expose
    private String koridor;
    @SerializedName("course")
    @Expose
    private Integer course;
    @SerializedName("gpsdatetime")
    @Expose
    private String gpsdatetime;
    @SerializedName("rawgpsdatetime")
    @Expose
    private String rawgpsdatetime;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



    public String getRawgpsdatetime() {
        return rawgpsdatetime;
    }

    public void setRawgpsdatetime(String rawgpsdatetime) {
        this.rawgpsdatetime = rawgpsdatetime;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuscode() {
        return buscode;
    }

    public void setBuscode(String buscode) {
        this.buscode = buscode;
    }

    public String getKoridor() {
        return koridor;
    }

    public void setKoridor(String koridor) {
        this.koridor = koridor;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public String getGpsdatetime() {
        return gpsdatetime;
    }

    public void setGpsdatetime(String gpsdatetime) {
        this.gpsdatetime = gpsdatetime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}