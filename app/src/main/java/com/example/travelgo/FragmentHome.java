package com.example.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yinglan.shadowimageview.ShadowImageView;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

//    public void onViewCreated(View view ,Bundle savedInstanceState) {
//        super.onCreate(view, savedInstanceState);
//
//        ArrayList<HomeItem> homeList = new ArrayList<>();
//        homeList.add(new HomeItem(R.drawable.background2, "Kuta, Bali", "Bali is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background3, "Lombok, NTB", "Lombok is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background4, "Komodo, NTT", "Komodo is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background5, "Madura, East Java", "Madura is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background6, "Bawean, East java", "Bawean is an Indonesian island known for ite forested volcanis mountains, ..."));
//
//        mRecyclerView = view.findViewById(R.id.RV_Home);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
//        mAdapter = new HomeAdapter(homeList, getContext());
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<HomeItem> homeList = new ArrayList<>();
        homeList.add(new HomeItem(R.drawable.background2, "Kuta, Bali", "Bali is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background3, "Lombok, NTB", "Lombok is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background4, "Komodo, NTT", "Komodo is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background5, "Madura, East Java", "Madura is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background6, "Bawean, East java", "Bawean is an Indonesian island known for ite forested volcanis mountains, ..."));

        mRecyclerView = view.findViewById(R.id.RV_Home);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        mAdapter = new HomeAdapter(homeList, getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
