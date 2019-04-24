package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentTour extends Fragment {

    private RecyclerView mRecyclerView;
    private TourAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<TourItem> tourList = new ArrayList<>();
    int[] sampleImages1 = {R.drawable.background2, R.drawable.background3, R.drawable.background4};
    int[] sampleImages2 = {R.drawable.background3, R.drawable.background4, R.drawable.background5};
    int[] sampleImages3 = {R.drawable.background4, R.drawable.background5, R.drawable.background6};
    int[] sampleImages4 = {R.drawable.background5, R.drawable.background6, R.drawable.background2};
    int[] sampleImages5 = {R.drawable.background6, R.drawable.background2, R.drawable.background3};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tour, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        tourList.add(new TourItem(sampleImages1, "Kuta Bali Tour", "Rp 2.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages2, "Lombok NTB Tour", "Rp 3.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages3, "Komodo NTT Tour", "Rp 4.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages4, "Madura East Java Tour", "Rp 5.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages5, "Bawean East Java Tour", "Rp 6.500.000", getResources().getString(R.string.cityDetail_Detail)));

        mRecyclerView = view.findViewById(R.id.tour_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new TourAdapter(tourList, getActivity());
//        mAdapter = new TourAdapter(tourList, this);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



//        ArrayList<TourListItem> tourListPackagesList = new ArrayList<>();
//        tourListPackagesList.add(new TourListItem(R.drawable.background2, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background3, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background4, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background5, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background6, "Rodex Tour", "11/04/2019", "12/04/2019"));
//
//        mRecyclerView = view.findViewById(R.id.RV_tourListPackages);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mAdapter = new TourListAdapter(tourListPackagesList);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
    }
}
