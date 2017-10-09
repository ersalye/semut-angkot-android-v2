package id.pptik.semutangkot.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Version")
    @Expose
    private Integer version;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Strategies")
    @Expose
    private List<String> strategies = null;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("Profile")
    @Expose
    private Profile_ profile;
    @SerializedName("Token")
    @Expose
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<String> strategies) {
        this.strategies = strategies;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Profile_ getProfile() {
        return profile;
    }

    public void setProfile(Profile_ profile) {
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}