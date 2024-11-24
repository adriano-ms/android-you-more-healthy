package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.EventController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Event;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventFragment extends Fragment implements IEntityFragment<Event> {

    private View view;
    private EditText etDescription;
    private EditText etDate;
    private EditText etTime;
    private Button btSave;
    private Button btDelete;
    private Event event;
    private DatePickerFragment datePicker;
    private TimePickerFragment timePicker;
    private Calendar calendar;
    private EventController controller;

    public EventFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);
        etDescription = view.findViewById(R.id.etDescriptionEvent);
        etDate = view.findViewById(R.id.etDateEvent);
        etTime = view.findViewById(R.id.etTimeEvent);
        btSave = view.findViewById(R.id.btSaveEvent);
        btDelete = view.findViewById(R.id.btDeleteEvent);
        controller = new EventController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        calendar = Calendar.getInstance();
        datePicker = new DatePickerFragment(etDate, calendar);
        timePicker = new TimePickerFragment(etTime, calendar);
        etDate.setFocusable(false);
        etDate.setOnClickListener(l -> datePicker.show(requireActivity().getSupportFragmentManager(), null));
        etTime.setFocusable(false);
        etTime.setOnClickListener(l -> timePicker.show(requireActivity().getSupportFragmentManager(), null));
        populateFields();
        return view;
    }

    public void saveAction(){
        try {
            controller.saveEvent(buildEntity());
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
            controller.deleteEvent(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(Event event) {
        this.event = event;
    }

    @Override
    public Event buildEntity() {
        event.setDescription(etDescription.getText().toString());
        event.setDate(calendar.getTime());
        return event;
    }

    @Override
    public void populateFields() {
        if(event != null){
            etDescription.setText(event.getDescription());
            calendar.setTime(event.getDate());
            etDate.setText(ViewUtils.DATE_FORMAT.format(event.getDate()));
            etTime.setText(ViewUtils.TIME_FORMAT.format(event.getDate()));
            calendar.setTime(event.getDate());
        } else {
            event = new Event();
        }
    }
}