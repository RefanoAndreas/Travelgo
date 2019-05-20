package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantListHolder> {

    ArrayList<JSONObject> participantList;
    Context context;
    ClickListener clickListner;

    public ParticipantListAdapter(ArrayList<JSONObject> participantList, Context context){
        this.participantList = participantList;
        this.context = context;
    }

    public class ParticipantListHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public ConstraintLayout layout;

        public ParticipantListHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TV_participant_name);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public ParticipantListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.participant_item, viewGroup,false);
        return new ParticipantListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantListHolder participantListHolder, final int i) {
        final JSONObject jsonObject = participantList.get(i);
        try {
//            participantListHolder.mTextView1.setText(jsonObject.getString("title")+" "+jsonObject.getString("name"));
            participantListHolder.mTextView1.setText(jsonObject.getString("title")+" "+jsonObject.getString("name"));

            participantListHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListner.onItemClick(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClickListener clickListner){
        this.clickListner= clickListner;
    }

}
