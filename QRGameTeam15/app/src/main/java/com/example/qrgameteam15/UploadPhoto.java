package com.example.qrgameteam15;

public class UploadPhoto {

    private String name;
    private String imageUri;

    public UploadPhoto() {
        //empty constructor needed.
    }

    public UploadPhoto(String name, String imageUri){
        if(name.trim().equals("")){
            name = "NoName";
        }

        this.name = name;
        this.imageUri = imageUri;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImageUri(){
        return this.imageUri;
    }

    public void setImageUri(String imageUri){
        this.imageUri = imageUri;
    }
}
