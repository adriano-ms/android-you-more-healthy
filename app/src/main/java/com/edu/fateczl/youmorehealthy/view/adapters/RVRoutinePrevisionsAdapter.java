package com.edu.fateczl.youmorehealthy.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.util.Date;
import java.util.List;

public class RVRoutinePrevisionsAdapter extends RecyclerView.Adapter<RVRoutinePrevisionsAdapter.RoutinePrevisionsViewHolder>{

    private final List<Date> dates;

    public RVRoutinePrevisionsAdapter(List<Date> dates){
        this.dates = dates;
    }

    public static class RoutinePrevisionsViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final TextView tvRoutinePrevisionDate;

        public RoutinePrevisionsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvRoutinePrevisionDate = cardView.findViewById(R.id.tvRoutinePrevisionDate);
            tvRoutinePrevisionDate.setClickable(false);
        }
    }

    @NonNull
    @Override
    public RoutinePrevisionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_routine_prevision, parent, false);
        return new RoutinePrevisionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinePrevisionsViewHolder holder, int position) {
        Date d = dates.get(position);
        holder.tvRoutinePrevisionDate.setText(ViewUtils.DATE_TIME_FORMAT.format(d));
    }


    @Override
    public int getItemCount() {
        return dates.size();
    }
}
