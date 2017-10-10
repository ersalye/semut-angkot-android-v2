package id.pptik.semutangkot.models.angkot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AngkotPost {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("PostBy")
    @Expose
    private PostBy postBy;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public PostBy getPostBy() {
        return postBy;
    }

    public void setPostBy(PostBy postBy) {
        this.postBy = postBy;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
