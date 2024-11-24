package com.edu.fateczl.youmorehealthy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.ScheduleController;
import com.edu.fateczl.youmorehealthy.model.Event;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.listeners.ExpandRoutineClickListener;
import com.edu.fateczl.youmorehealthy.view.listeners.OnCardClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RVNextTaskAdapter extends RecyclerView.Adapter<RVNextTaskAdapter.NextTaskViewHolder> {

    private final List<Task> tasks;
    public final OnCardClickListener<Task> listener;
    private final ScheduleController scheduleController;

    public RVNextTaskAdapter(List<Task> tasks, OnCardClickListener<Task> listener, ScheduleController scheduleController){
        this.tasks = tasks;
        this.listener = listener;
        this.scheduleController = scheduleController;
    }

    public static class NextTaskViewHolder extends RecyclerView.ViewHolder{

        private final CardView cardView;
        private final ConstraintLayout layoutNextTaskCardView;
        private final ImageView iconCardView;
        private final TextView titleCardView;
        private final TextView tvCardDate;
        private final ImageView ivArrow;
        private final RecyclerView rvRoutinePrevisions;
        private final ScheduleController scheduleController;

        public NextTaskViewHolder(@NonNull View itemView, ScheduleController scheduleController) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvNextTask);
            layoutNextTaskCardView = itemView.findViewById(R.id.layoutNextTaskCardView);
            iconCardView = cardView.findViewById(R.id.iconNextCardView);
            titleCardView = cardView.findViewById(R.id.titleNextCardView);
            tvCardDate = cardView.findViewById(R.id.tvNextCardDate);
            ivArrow = cardView.findViewById(R.id.ivArrow);
            rvRoutinePrevisions = cardView.findViewById(R.id.rvRoutinePrevisions);
            this.scheduleController = scheduleController;
        }
    }

    @NonNull
    @Override
    public NextTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_next_task, parent, false);
        return new NextTaskViewHolder(view, scheduleController);
    }

    @Override
    public void onBindViewHolder(@NonNull NextTaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.iconCardView.setImageResource(ViewUtils.chooseTaskIcon(task.getType()));
        holder.titleCardView.setText(task.getDescription());
        holder.layoutNextTaskCardView.setOnClickListener(v -> listener.onCardClick(holder.cardView, task));
        if(task.getType().equals(TaskType.ROUTINE) || task.getType().equals(TaskType.SPORT) || task.getType().equals(TaskType.MEDICINE)){
            holder.tvCardDate.setVisibility(View.GONE);
            holder.ivArrow.setVisibility(View.VISIBLE);
            holder.rvRoutinePrevisions.setAdapter(new RVRoutinePrevisionsAdapter(listRoutineDates((Routine) task, scheduleController)));
            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.cardView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            holder.rvRoutinePrevisions.setLayoutManager(layoutManager);
            holder.ivArrow.setOnClickListener(new ExpandRoutineClickListener(holder.rvRoutinePrevisions));
        } else {
            holder.tvCardDate.setText(ViewUtils.DATE_TIME_FORMAT.format(((Event) task).getDate()));
        }
    }

    private List<Date> listRoutineDates(Routine routine, ScheduleController scheduleController){
        Frequency frequency = routine.getFrequency();
        int n = frequency.getRepetitions();
        List<Date> dates = new ArrayList<>(n);
        Calendar c = Calendar.getInstance();
        c.setTime(scheduleController.getRoutineFirstDate(routine, c.getTime()));
        for(int i = 0; i < n; i++){
            dates.add(c.getTime());
            c.add(Calendar.HOUR_OF_DAY, frequency.getCycleInHours());
        }
        return dates;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

}
