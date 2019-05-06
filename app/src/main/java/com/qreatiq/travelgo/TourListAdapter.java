package com.qreatiq.travelgo;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.TourListPackagesViewHolder> {

    private ArrayList<TourListItem> mTourListPackagesList;

    public static class TourListPackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public SwipeLayout swipeLayout;
        ConstraintLayout view_foreground;
        RelativeLayout view_background;

        public TourListPackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_tourListPackages);
            mTextView1 = itemView.findViewById(R.id.itemRV_TV_title_tourListPackages);
            mTextView2 = itemView.findViewById(R.id.itemRV_TV_dateFrom_tourListPackages);
            mTextView3 = itemView.findViewById(R.id.itemRV_TV_dateTo_tourListPackages);

            view_foreground = itemView.findViewById(R.id.view_foreground);
            view_background = itemView.findViewById(R.id.view_background);

//            swipeLayout = itemView.findViewById(R.id.Swipe_TourListPackages);

            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottom_wrapper));
        }
    }

    public TourListAdapter(ArrayList<TourListItem> tourListPackageslist){
        mTourListPackagesList = tourListPackageslist;
    }

    @Override
    public TourListPackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tourlist_itemswipe, viewGroup,false);
        TourListPackagesViewHolder tlpvh = new TourListPackagesViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(TourListPackagesViewHolder tourListPackagesViewHolder, int i) {
        TourListItem currentItem = mTourListPackagesList.get(i);

        tourListPackagesViewHolder.mRoundedImageView.setImageResource(currentItem.getImageResources());
        tourListPackagesViewHolder.mTextView1.setText(currentItem.getText1());
        tourListPackagesViewHolder.mTextView2.setText(currentItem.getText2());
        tourListPackagesViewHolder.mTextView3.setText(currentItem.getText3());

        //set show mode.
//        tourListPackagesViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//
//        tourListPackagesViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onClose(SwipeLayout layout) {
//                //when the SurfaceView totally cover the BottomView.
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//                //you are swiping.
//            }
//
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                //when the BottomView totally show.
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//                //when user's hand released.
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mTourListPackagesList.size();
    }
}
