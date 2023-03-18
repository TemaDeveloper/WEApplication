package com_we.java_we.weapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donation {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("uid")
    @Expose
    private int uid;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("amount")
    @Expose
    private int value;

    public Donation(int id, int uid, String date, int value) {
        this.date = date;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

}
