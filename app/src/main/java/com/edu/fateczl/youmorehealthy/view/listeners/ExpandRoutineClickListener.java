package com.edu.fateczl.youmorehealthy.view.listeners;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ExpandRoutineClickListener implements View.OnClickListener {

    private final RecyclerView recyclerView;

    public ExpandRoutineClickListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        ImageView arrow = (ImageView) v;
        if(recyclerView.getVisibility() == View.VISIBLE){
            arrow.animate().rotation(-180f).setDuration(100).withEndAction(() -> recyclerView.setVisibility(View.GONE));
        } else {
            arrow.animate().rotation(0).setDuration(100).withEndAction(() -> recyclerView.setVisibility(View.VISIBLE));
        }
    }
}
