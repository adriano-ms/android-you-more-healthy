package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.SportRoutineController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;

import java.util.Calendar;

public class TrainingFragment extends Fragment implements IEntityFragment<Training> {

    private View view;
    private ImageView iconTraining;
    private TextView tvSportTitle;
    private EditText etTrainingDate;
    private EditText etTrainingTime;
    private EditText etResult;
    private Button btSave;
    private Button btDelete;
    private SportRoutine sportRoutine;
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;
    private Training training;
    private SportRoutineController controller;
    private Calendar calendar;

    public TrainingFragment(int sportRoutineId) {
        sportRoutine = new SportRoutine();
        sportRoutine.setId(sportRoutineId);
        training = new Training();
    }

    public TrainingFragment(SportRoutine sportRoutine) {
        this.sportRoutine = sportRoutine;
        training = new Training();
    }

    public TrainingFragment(SportRoutine sportRoutine, Training training){
        this.sportRoutine = sportRoutine;
        this.training = training;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_training, container, false);
        iconTraining = view.findViewById(R.id.iconTraining);
        tvSportTitle = view.findViewById(R.id.tvSportTitle);
        etTrainingDate = view.findViewById(R.id.etTrainingDate);
        etTrainingTime = view.findViewById(R.id.etTrainingTime);
        etResult = view.findViewById(R.id.etResult);
        btSave = view.findViewById(R.id.btSaveTraining);
        btDelete = view.findViewById(R.id.btDeleteTraining);
        iconTraining.setImageResource(ViewUtils.chooseTaskIcon(TaskType.SPORT));
        controller = new SportRoutineController(view.getContext());
        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment(etTrainingDate, calendar);
        timePickerFragment = new TimePickerFragment(etTrainingTime, calendar);
        etTrainingDate.setFocusable(false);
        etTrainingTime.setFocusable(false);
        etTrainingDate.setOnClickListener(b -> datePickerFragment.show(requireActivity().getSupportFragmentManager(), null));
        etTrainingTime.setOnClickListener(b -> timePickerFragment.show(requireActivity().getSupportFragmentManager(), null));
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        populateFields();
        return view;
    }

    private void saveAction(){
        try {
            controller.addTraining(sportRoutine.getId(), buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_insert_update_positive);
            ViewUtils.changeFragment(requireActivity().getSupportFragmentManager() , R.id.taskFragment, FragmentFactory.createTrainingsFragment(sportRoutine.getId()), false);
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        } catch (FormException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    private void deleteAction(){
        try {
            controller.deleteTraining(buildEntity().getId());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            ViewUtils.changeFragment(requireActivity().getSupportFragmentManager() , R.id.taskFragment, FragmentFactory.createTrainingsFragment(sportRoutine.getId()), false);
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(Training training) {
        this.training = training;
    }

    @Override
    public Training buildEntity() {
        training.setDate(calendar.getTime());
        training.setResult(etResult.getText().toString());
        return training;
    }

    @Override
    public void populateFields() {
        if(sportRoutine.getDescription() == null)
            sportRoutine = controller.findRoutine(sportRoutine.getId());
        if(training.getDate() == null) {
            etTrainingDate.setText(ViewUtils.DATE_FORMAT.format(calendar.getTime()));
            etTrainingTime.setText(ViewUtils.TIME_FORMAT.format(calendar.getTime()));
        } else {
            etTrainingDate.setText(ViewUtils.DATE_FORMAT.format(training.getDate()));
            etTrainingTime.setText(ViewUtils.TIME_FORMAT.format(training.getDate()));
        }
        tvSportTitle.setText(sportRoutine.getDescription());
        etResult.setText(training.getResult());
    }
}