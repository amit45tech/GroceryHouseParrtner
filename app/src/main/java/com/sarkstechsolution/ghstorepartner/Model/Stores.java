package com.sarkstechsolution.ghstorepartner.Model;

public class Stores {

    private String storeid, password;

    public Stores() {
    }

    public Stores(String storeid, String password) {
        this.storeid = storeid;
        this.password = password;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
