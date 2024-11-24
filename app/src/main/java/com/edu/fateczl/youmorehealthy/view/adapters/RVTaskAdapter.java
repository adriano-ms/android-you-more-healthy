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
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.listeners.OnCardClickListener;

import java.util.List;

public class RVTaskAdapter extends RecyclerView.Adapter<RVTaskAdapter.TaskViewHolder> {

    private final List<Task> tasks;
    public final OnCardClickListener<Task> listener;

    public RVTaskAdapter(List<Task> tasks, OnCardClickListener<Task> listener){
        this.tasks = tasks;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final ImageView iconCardView;
        private final TextView titleCardView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvTask);
            iconCardView = cardView.findViewById(R.id.iconCardView);
            titleCardView = cardView.findViewById(R.id.titleCardView);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.iconCardView.setImageResource(ViewUtils.chooseTaskIcon(task.getType()));
        holder.titleCardView.setText(task.getDescription());
        holder.cardView.setOnClickListener(v -> listener.onCardClick(holder.cardView, task));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

}
