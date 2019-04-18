package com.example.travelgo;

public class TourCreateItem_1 {
    private int mImageResources1;
    private int mImageResources2;
    private String mText1;

    public TourCreateItem_1(int imageResource1, int imageResource2, String text1){
        mImageResources1 = imageResource1;
        mImageResources2 = imageResource2;
        mText1 = text1;
    }

    public int getImageResources1(){
        return mImageResources1;
    }

    public int getImageResources2(){
        return mImageResources2;
    }

    public String getText1(){
        return mText1;
    }

}
