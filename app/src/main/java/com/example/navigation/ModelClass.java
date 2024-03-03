package com.example.navigation;

import android.graphics.Bitmap;

public class ModelClass {
    private String imageName,Contact,Email,Address;
    private Bitmap image;

    int images;

    public ModelClass(String imageName, String Contact, String Email, String Address, Bitmap image) {
        this.imageName = imageName;
        this.Contact = Contact;
        this.Email = Email;
        this.Address = Address;
        this.image = image;
    }
public ModelClass(int images){
        this.images=images;
}

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
