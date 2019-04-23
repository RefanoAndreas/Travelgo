package com.qreatiq.travelgo;

public class CityDetailItem {
    private int mImageResources;
    private String mText1;

    public CityDetailItem(int imageResource, String text1){
        mImageResources = imageResource;
        mText1 = text1;
    }

    public int getImageResources(){
        return mImageResources;
    }

    public String getText1(){
        return mText1;
    }
}
