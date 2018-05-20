package com.example.yotam707.feedmeapp.domain;

import java.net.URL;

public class UploadImageParams {
    private URL url;
    private String imageName;
public UploadImageParams(){

}
    public UploadImageParams(URL url, String imageName){
    this.url = url;
    this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
