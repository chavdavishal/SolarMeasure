package com.example.navigation;

public class AminoAcidModel
{
    String aminoAcidName,aminoAcideAbbreviation;
    int image;

    public AminoAcidModel(String aminoAcidName,String aminoAcideAbbreviation,int image){
        this.aminoAcideAbbreviation=aminoAcideAbbreviation;
        this.aminoAcidName=aminoAcidName;
        this.image=image;
    }

    public String getAminoAcidName() {
        return aminoAcidName;
    }

    public String getAminoAcideAbbreviation() {
        return aminoAcideAbbreviation;
    }

    public int getImage() {
        return image;
    }
}
