package com.example.yotam707.feedmeapp.domain;

import android.graphics.Bitmap;
import android.net.Uri;

public class UploadImage {
    private Bitmap bitmap;
    private String imageName;
    private Uri uri;

    public UploadImage(){

    }

    public UploadImage(Bitmap bitmap, String imageName){
        this.bitmap = bitmap;
        this.imageName = imageName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
