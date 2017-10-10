package id.pptik.semutangkot.models.angkot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trayek {

    @SerializedName("Nama")
    @Expose
    private String nama;
    @SerializedName("TrayekID")
    @Expose
    private Integer trayekID;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getTrayekID() {
        return trayekID;
    }

    public void setTrayekID(Integer trayekID) {
        this.trayekID = trayekID;
    }

}
