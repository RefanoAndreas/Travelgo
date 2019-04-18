package com.example.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentTour extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tour_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<TourListItem> tourListPackagesList = new ArrayList<>();
        tourListPackagesList.add(new TourListItem(R.drawable.background2, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background3, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background4, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background5, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background6, "Rodex Tour", "11/04/2019", "12/04/2019"));

        mRecyclerView = view.findViewById(R.id.RV_tourListPackages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TourListAdapter(tourListPackagesList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
