package ru.scratty.action.model;

import com.google.gson.annotations.SerializedName;

public class OwnerPhoto {

    @SerializedName("server")
    private int server;

    @SerializedName("photo")
    private String photo;

    @SerializedName("mid")
    private int mid;

    @SerializedName("hash")
    private String hash;

    @SerializedName("message_code")
    private int messageCode;

    @SerializedName("profile_aid")
    private int profileAid;

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public int getProfileAid() {
        return profileAid;
    }

    public void setProfileAid(int profileAid) {
        this.profileAid = profileAid;
    }
}
