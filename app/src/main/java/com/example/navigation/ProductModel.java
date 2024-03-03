package com.example.navigation;

import android.graphics.Bitmap;

public class ProductModel {
    private String cid,pname,pprice,pwatts;

    private Bitmap image;


    public ProductModel(String cid, String pname, String pprice, String pwatts, Bitmap image) {
        this.cid = cid;
        this.pname = pname;
        this.pprice = pprice;
        this.pwatts = pwatts;
        this.image = image;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPwatts() {
        return pwatts;
    }

    public void setPwatts(String pwatts) {
        this.pwatts = pwatts;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
