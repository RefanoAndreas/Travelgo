package com.example.travelgo;

public class TourListItem {
    private int mImageResources;
    private String mText1;
    private String mText2;
    private String mText3;

    public TourListItem(int imageResource, String text1, String text2, String text3){
        mImageResources = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
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

    public String getText3(){
        return mText3;
    }
}
