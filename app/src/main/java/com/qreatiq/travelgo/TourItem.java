package com.qreatiq.travelgo;

public class TourItem {
    private int[] mImageCarousel;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mID;

    public TourItem(int[] imageCarousel, String text1, String text2, String text3, String id){
        mImageCarousel = imageCarousel;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mID = id;
    }

    public int[] getImageCarousel() {
        return mImageCarousel;
    }
    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
    public String getText3(){
        return mText3;
    }
    public String getID(){
        return mID;
    }
}
