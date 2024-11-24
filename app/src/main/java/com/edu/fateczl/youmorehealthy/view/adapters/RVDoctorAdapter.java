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
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.view.listeners.OnCardClickListener;

import java.util.List;

public class RVDoctorAdapter extends RecyclerView.Adapter<RVDoctorAdapter.DoctorViewHolder> {

    private final List<Doctor> doctors;
    public final OnCardClickListener<Doctor> listener;

    public RVDoctorAdapter(List<Doctor> doctors, OnCardClickListener<Doctor> listener){
        this.doctors = doctors;
        this.listener = listener;
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final ImageView iconCardView;
        private final TextView titleCardView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvTask);
            iconCardView = cardView.findViewById(R.id.iconCardView);
            titleCardView = cardView.findViewById(R.id.titleCardView);
        }
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_task, parent, false);
        return new DoctorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.iconCardView.setImageResource(R.drawable.baseline_person_24);
        holder.titleCardView.setText(String.format("%s | %s", doctor.getName(), doctor.getSpeciality()));
        holder.cardView.setOnClickListener(v -> listener.onCardClick(holder.cardView, doctor));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

}
