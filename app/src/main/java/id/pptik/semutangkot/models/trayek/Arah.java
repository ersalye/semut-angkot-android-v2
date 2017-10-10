package id.pptik.semutangkot.models.trayek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Arah {

    @SerializedName("Flag")
    @Expose
    private Integer flag;
    @SerializedName("Nama")
    @Expose
    private String nama;
    @SerializedName("JumlahPenunggu")
    @Expose
    private Integer jumlahPenunggu;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getJumlahPenunggu() {
        return jumlahPenunggu;
    }

    public void setJumlahPenunggu(Integer jumlahPenunggu) {
        this.jumlahPenunggu = jumlahPenunggu;
    }

}
