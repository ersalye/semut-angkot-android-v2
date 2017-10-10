package id.pptik.semutangkot.models.angkot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Angkot_ {

    @SerializedName("LastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("PlatNomor")
    @Expose
    private String platNomor;
    @SerializedName("Trayek")
    @Expose
    private Trayek trayek;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("JumlahPenumpang")
    @Expose
    private Integer jumlahPenumpang;

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public Trayek getTrayek() {
        return trayek;
    }

    public void setTrayek(Trayek trayek) {
        this.trayek = trayek;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public void setJumlahPenumpang(Integer jumlahPenumpang) {
        this.jumlahPenumpang = jumlahPenumpang;
    }

}