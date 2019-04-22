package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomePackagesViewHolder> {

    private ArrayList<HomeItem> mHomeList;
    Context context;

    Display display;

    public static class HomePackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ConstraintLayout layout;
//        public SwipeLayout swipeLayout;

        public HomePackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_Home_RV);
            mTextView1 = itemView.findViewById(R.id.itemRV_Home_TV1);
            mTextView2 = itemView.findViewById(R.id.itemRV_Home_TV2);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);

//            swipeLayout = itemView.findViewById(R.id.Swipe_TourListPackages);
//
//            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottom_wrapper));
        }
    }

    public HomeAdapter(ArrayList<HomeItem> homelist, Context context){
        mHomeList = homelist;
        this.context = context;
    }

    @Override
    public HomePackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homelist_item, viewGroup,false);
        HomePackagesViewHolder homevh = new HomePackagesViewHolder(v);
        return homevh;
    }

    @Override
    public void onBindViewHolder(HomePackagesViewHolder homePackagesViewHolder, int i) {
        HomeItem currentItem = mHomeList.get(i);
//        homePackagesViewHolder.mRoundedImageView.setImageResource(currentItem.getImageResources());
        Picasso.get().load(currentItem.getImageResources()).placeholder(R.mipmap.ic_launcher).into(homePackagesViewHolder.mRoundedImageView);
        homePackagesViewHolder.mTextView1.setText(currentItem.getText1());
        homePackagesViewHolder.mTextView2.setText(currentItem.getText2());

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, convertpx(10), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(0), 0, convertpx(16), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(0), 0, convertpx(10), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
        }


        //set show mode.
//        homePackagesViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//
//        homePackagesViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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

    public int convertpx(int dp){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }
}
