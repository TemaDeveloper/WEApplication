package com_we.java_we.weapplication.models;

public class Donation {

    private String date;
    private int value;

    public Donation(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

}
