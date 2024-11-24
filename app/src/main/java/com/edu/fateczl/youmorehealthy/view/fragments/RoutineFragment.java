package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.RoutineController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RoutineFragment extends Fragment implements IEntityFragment<Routine> {

    private View view;
    private EditText etDescription;
    private EditText etTime;
    private EditText etCycle;
    private EditText etRepetitions;
    private Button btSave;
    private Button btDelete;
    private Routine routine;
    private TimePickerFragment timePicker;
    private Calendar calendar;
    private RoutineController controller;

    public RoutineFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_routine, container, false);
        etDescription = view.findViewById(R.id.etDescriptionRoutine);
        etTime = view.findViewById(R.id.etTimeRoutine);
        etCycle = view.findViewById(R.id.etCycleRoutine);
        etRepetitions = view.findViewById(R.id.etRepetitionsRoutine);
        btSave = view.findViewById(R.id.btSaveRoutine);
        btDelete = view.findViewById(R.id.btDeleteRoutine);
        controller = new RoutineController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
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

    @Override
    public void setEntity(Routine routine) {
        this.routine = routine;
    }

    @Override
    public Routine buildEntity() {
        routine.setDescription(etDescription.getText().toString());
        routine.getFrequency().setHour(calendar.get(Calendar.HOUR_OF_DAY));
        routine.getFrequency().setMinute(calendar.get(Calendar.MINUTE));
        try {
            routine.getFrequency().setCycleInHours(Integer.parseInt(etCycle.getText().toString()));
            routine.getFrequency().setRepetitions(Integer.parseInt(etRepetitions.getText().toString()));
        } catch (NumberFormatException e) {
            throw new FormException();
        }
        return routine;
    }

    @Override
    public void populateFields() {
        if(routine != null && routine.getFrequency() != null){
            etDescription.setText(routine.getDescription());
            calendar.set(Calendar.HOUR_OF_DAY, routine.getFrequency().getHour());
            calendar.set(Calendar.MINUTE, routine.getFrequency().getMinute());
            etTime.setText(ViewUtils.TIME_FORMAT.format(calendar.getTime()));
            etCycle.setText(String.valueOf(routine.getFrequency().getCycleInHours()));
            etRepetitions.setText(String.valueOf(routine.getFrequency().getRepetitions()));
        } else {
            routine = new Routine();
            routine.setFrequency(new Frequency());
        }
    }
}