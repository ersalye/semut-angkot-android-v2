package id.pptik.semutangkot.models.trayek;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trayek {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("TrayekID")
    @Expose
    private Integer trayekID;
    @SerializedName("TrayekName")
    @Expose
    private String trayekName;
    @SerializedName("Arah")
    @Expose
    private List<Arah> arah = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTrayekID() {
        return trayekID;
    }

    public void setTrayekID(Integer trayekID) {
        this.trayekID = trayekID;
    }

    public String getTrayekName() {
        return trayekName;
    }

    public void setTrayekName(String trayekName) {
        this.trayekName = trayekName;
    }

    public List<Arah> getArah() {
        return arah;
    }

    public void setArah(List<Arah> arah) {
        this.arah = arah;
    }

}