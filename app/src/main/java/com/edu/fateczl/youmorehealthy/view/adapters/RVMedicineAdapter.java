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
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.view.listeners.OnCardClickListener;

import java.util.List;

public class RVMedicineAdapter extends RecyclerView.Adapter<RVMedicineAdapter.MedicineViewHolder> {

    private final List<Medicine> medicines;
    public final OnCardClickListener<Medicine> listener;

    public RVMedicineAdapter(List<Medicine> medicines, OnCardClickListener<Medicine> listener){
        this.medicines = medicines;
        this.listener = listener;
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final ImageView iconCardView;
        private final TextView titleCardView;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvTask);
            iconCardView = cardView.findViewById(R.id.iconCardView);
            titleCardView = cardView.findViewById(R.id.titleCardView);
        }
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_task, parent, false);
        return new MedicineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.iconCardView.setImageResource(R.drawable.baseline_medication_24);
        holder.titleCardView.setText(String.format("%s | %s", medicine.getName(), medicine.getDose()));
        holder.cardView.setOnClickListener(v -> listener.onCardClick(holder.cardView, medicine));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

}
