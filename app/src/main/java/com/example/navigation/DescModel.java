package com.example.navigation;

public class DescModel {
    int images;
    String desc;



    public DescModel(int images, String desc)
    {
        this.images=images;
        this.desc=desc;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
