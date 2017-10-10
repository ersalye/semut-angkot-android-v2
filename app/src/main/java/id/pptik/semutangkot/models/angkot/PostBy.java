package id.pptik.semutangkot.models.angkot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostBy {

    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("Name")
    @Expose
    private String name;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}