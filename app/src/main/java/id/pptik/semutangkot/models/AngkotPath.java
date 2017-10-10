package id.pptik.semutangkot.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AngkotPath {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("TrayekName")
    @Expose
    private String trayekName;
    @SerializedName("TrayekID")
    @Expose
    private String trayekID;
    @SerializedName("TrayekRoute")
    @Expose
    private String trayekRoute;
    @SerializedName("TrayekDistance")
    @Expose
    private String trayekDistance;
    @SerializedName("PathToDestination")
    @Expose
    private String pathToDestination;
    @SerializedName("PathToOrigin")
    @Expose
    private String pathToOrigin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrayekName() {
        return trayekName;
    }

    public void setTrayekName(String trayekName) {
        this.trayekName = trayekName;
    }

    public String getTrayekID() {
        return trayekID;
    }

    public void setTrayekID(String trayekID) {
        this.trayekID = trayekID;
    }

    public String getTrayekRoute() {
        return trayekRoute;
    }

    public void setTrayekRoute(String trayekRoute) {
        this.trayekRoute = trayekRoute;
    }

    public String getTrayekDistance() {
        return trayekDistance;
    }

    public void setTrayekDistance(String trayekDistance) {
        this.trayekDistance = trayekDistance;
    }

    public String getPathToDestination() {
        return pathToDestination;
    }

    public void setPathToDestination(String pathToDestination) {
        this.pathToDestination = pathToDestination;
    }

    public String getPathToOrigin() {
        return pathToOrigin;
    }

    public void setPathToOrigin(String pathToOrigin) {
        this.pathToOrigin = pathToOrigin;
    }

}