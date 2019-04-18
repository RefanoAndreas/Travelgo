package com.example.travelgo;

public class HomeItem {
    private int mImageResources;
    private String mText1;
    private String mText2;

    public HomeItem(int imageResource, String text1, String text2){
        mImageResources = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public int getImageResources(){
        return mImageResources;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return  mText2;
    }
}
