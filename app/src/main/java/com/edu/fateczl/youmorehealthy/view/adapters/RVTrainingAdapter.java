package com.edu.fateczl.youmorehealthy.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.listeners.OnCardClickListener;

import java.util.List;

public class RVTrainingAdapter extends RecyclerView.Adapter<RVTrainingAdapter.TrainingViewHolder> {

    private final List<Training> trainings;
    public final OnCardClickListener<Training> listener;

    public RVTrainingAdapter(List<Training> trainings, OnCardClickListener<Training> listener){
        this.trainings = trainings;
        this.listener = listener;
    }

    public static class TrainingViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final ImageView iconCardView;
        private final TextView titleCardView;

        public TrainingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvTask);
            iconCardView = cardView.findViewById(R.id.iconCardView);
            titleCardView = cardView.findViewById(R.id.titleCardView);
        }
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_task, parent, false);
        return new TrainingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
        Training training = trainings.get(position);
        holder.iconCardView.setImageResource(R.drawable.baseline_event_available_24);
        holder.titleCardView.setText(ViewUtils.DATE_TIME_FORMAT.format(training.getDate()));
        holder.cardView.setOnClickListener(v -> listener.onCardClick(holder.cardView, training));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

}
