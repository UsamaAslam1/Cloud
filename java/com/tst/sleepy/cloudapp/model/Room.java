package com.tst.sleepy.cloudapp.model;

import android.net.Uri;

import java.util.ArrayList;

public class Room extends ArrayList<Room> {
    int ac;
    int tv;
    int wifi;
    int mattress;
    int price;
    String status;
    ArrayList<String> images;
    public  Room()
    {}

    public Room(int ac, int tv, int wifi, int mattress, int price, String status) {
        this.ac = ac;
        this.tv = tv;
        this.wifi = wifi;
        this.mattress = mattress;
        this.price = price;
        this.status = status;
    }
    public Room(int ac, int tv, int wifi, int mattress, int price, String status,ArrayList<String> images) {
        this.ac = ac;
        this.tv = tv;
        this.wifi = wifi;
        this.mattress = mattress;
        this.price = price;
        this.status = status;
        this.images=images;
    }
    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getTv() {
        return tv;
    }

    public void setTv(int tv) {
        this.tv = tv;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getMattress() {
        return mattress;
    }

    public void setMattress(int mattress) {
        this.mattress = mattress;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
