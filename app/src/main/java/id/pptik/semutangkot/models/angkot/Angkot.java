package id.pptik.semutangkot.models.angkot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Angkot {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("CountryCode")
    @Expose
    private Integer countryCode;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Gender")
    @Expose
    private Integer gender;
    @SerializedName("Birthday")
    @Expose
    private String birthday;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Joindate")
    @Expose
    private String joindate;
    @SerializedName("Poin")
    @Expose
    private Integer poin;
    @SerializedName("PoinLevel")
    @Expose
    private Integer poinLevel;
    @SerializedName("AvatarID")
    @Expose
    private Integer avatarID;
    @SerializedName("facebookID")
    @Expose
    private String facebookID;
    @SerializedName("Verified")
    @Expose
    private Integer verified;
    @SerializedName("VerifiedNumber")
    @Expose
    private String verifiedNumber;
    @SerializedName("Visibility")
    @Expose
    private Integer visibility;
    @SerializedName("Reputation")
    @Expose
    private Integer reputation;
    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("Barcode")
    @Expose
    private String barcode;
    @SerializedName("deposit")
    @Expose
    private Integer deposit;
    @SerializedName("ID_role")
    @Expose
    private Integer iDRole;
    @SerializedName("Plat_motor")
    @Expose
    private String platMotor;
    @SerializedName("ID_ktp")
    @Expose
    private String iDKtp;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("PushID")
    @Expose
    private String pushID;
    @SerializedName("Status_online")
    @Expose
    private Integer statusOnline;
    @SerializedName("Path_foto")
    @Expose
    private String pathFoto;
    @SerializedName("Nama_foto")
    @Expose
    private String namaFoto;
    @SerializedName("Path_ktp")
    @Expose
    private String pathKtp;
    @SerializedName("Nama_ktp")
    @Expose
    private String namaKtp;
    @SerializedName("Angkot")
    @Expose
    private Angkot_ angkot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public Integer getPoin() {
        return poin;
    }

    public void setPoin(Integer poin) {
        this.poin = poin;
    }

    public Integer getPoinLevel() {
        return poinLevel;
    }

    public void setPoinLevel(Integer poinLevel) {
        this.poinLevel = poinLevel;
    }

    public Integer getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(Integer avatarID) {
        this.avatarID = avatarID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getVerifiedNumber() {
        return verifiedNumber;
    }

    public void setVerifiedNumber(String verifiedNumber) {
        this.verifiedNumber = verifiedNumber;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public Integer getIDRole() {
        return iDRole;
    }

    public void setIDRole(Integer iDRole) {
        this.iDRole = iDRole;
    }

    public String getPlatMotor() {
        return platMotor;
    }

    public void setPlatMotor(String platMotor) {
        this.platMotor = platMotor;
    }

    public String getIDKtp() {
        return iDKtp;
    }

    public void setIDKtp(String iDKtp) {
        this.iDKtp = iDKtp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    public Integer getStatusOnline() {
        return statusOnline;
    }

    public void setStatusOnline(Integer statusOnline) {
        this.statusOnline = statusOnline;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public String getNamaFoto() {
        return namaFoto;
    }

    public void setNamaFoto(String namaFoto) {
        this.namaFoto = namaFoto;
    }

    public String getPathKtp() {
        return pathKtp;
    }

    public void setPathKtp(String pathKtp) {
        this.pathKtp = pathKtp;
    }

    public String getNamaKtp() {
        return namaKtp;
    }

    public void setNamaKtp(String namaKtp) {
        this.namaKtp = namaKtp;
    }

    public Angkot_ getAngkot() {
        return angkot;
    }

    public void setAngkot(Angkot_ angkot) {
        this.angkot = angkot;
    }

}
