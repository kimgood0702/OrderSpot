package com.example.orderspot_general.RecommandD;

public class Recommandlist {

    private String reImage;   // Image
    private String reName;  // name
    private String reScore;   // score

    public Recommandlist(String reImage, String reName, String reScore) {
        this.reImage = reImage;
        this.reName = reName;
        this.reScore = reScore;
    }

    public String getReImage() {
        return reImage;
    }

    public void setReImage(String reImage) {
        this.reImage = reImage;
    }

    public String getReName() {
        return reName;
    }

    public void setReName(String reName) {
        this.reName = reName;
    }

    public String getReScore() {
        return reScore;
    }

    public void setReScore(String reScore) {
        this.reScore = reScore;
    }
}
