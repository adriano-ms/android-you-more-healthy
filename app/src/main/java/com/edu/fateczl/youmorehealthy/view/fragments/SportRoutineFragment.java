package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.SportRoutineController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SportRoutineFragment extends Fragment implements IEntityFragment<SportRoutine> {

    private View view;
    private EditText etDescription;
    private EditText etTime;
    private EditText etCycle;
    private EditText etRepetitions;
    private Button btSave;
    private Button btDelete;
    private Button btTrainings;
    private SportRoutine sportRoutine;
    private TimePickerFragment timePicker;
    private Calendar calendar;
    private SportRoutineController controller;

    public SportRoutineFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport, container, false);
        etDescription = view.findViewById(R.id.etDescriptionSport);
        etTime = view.findViewById(R.id.etTimeSport);
        etCycle = view.findViewById(R.id.etCycleSport);
        etRepetitions = view.findViewById(R.id.etRepetitionsSport);
        btSave = view.findViewById(R.id.btSaveSport);
        btDelete = view.findViewById(R.id.btDeleteSport);
        btTrainings = view.findViewById(R.id.btTrainings);
        controller = new SportRoutineController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        btTrainings.setOnClickListener(b -> trainingsAction());
        calendar = Calendar.getInstance();
        timePicker = new TimePickerFragment(etTime, calendar);
        etTime.setFocusable(false);
        etTime.setOnClickListener(l -> timePicker.show(requireActivity().getSupportFragmentManager(), null));
        populateFields();
        return view;
    }

    public void saveAction(){
        try {
            controller.saveRoutine(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_insert_update_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        } catch (FormException e){
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    private void deleteAction() {
        try {
            controller.deleteRoutine(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        } catch (FormException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    private void trainingsAction(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.taskFragment, FragmentFactory.createTrainingsFragment(buildEntity()), true);
    }

    @Override
    public void setEntity(SportRoutine sportRoutine) {
        this.sportRoutine = sportRoutine;
    }

    @Override
    public SportRoutine buildEntity() {
        sportRoutine.setDescription(etDescription.getText().toString());
        sportRoutine.getFrequency().setHour(calendar.get(Calendar.HOUR_OF_DAY));
        sportRoutine.getFrequency().setMinute(calendar.get(Calendar.MINUTE));
        try {
            sportRoutine.getFrequency().setCycleInHours(Integer.parseInt(etCycle.getText().toString()));
            sportRoutine.getFrequency().setRepetitions(Integer.parseInt(etRepetitions.getText().toString()));
        } catch (NumberFormatException e) {
            throw new FormException();
        }
        return sportRoutine;
    }

    @Override
    public void populateFields() {
        if(sportRoutine != null && sportRoutine.getFrequency() != null){
            etDescription.setText(sportRoutine.getDescription());
            calendar.set(Calendar.HOUR_OF_DAY, sportRoutine.getFrequency().getHour());
            calendar.set(Calendar.MINUTE, sportRoutine.getFrequency().getMinute());
            etTime.setText(ViewUtils.TIME_FORMAT.format(calendar.getTime()));
            etCycle.setText(String.valueOf(sportRoutine.getFrequency().getCycleInHours()));
            etRepetitions.setText(String.valueOf(sportRoutine.getFrequency().getRepetitions()));
        } else {
            btTrainings.setVisibility(View.GONE);
            sportRoutine = new SportRoutine();
            sportRoutine.setFrequency(new Frequency());
        }
    }
}