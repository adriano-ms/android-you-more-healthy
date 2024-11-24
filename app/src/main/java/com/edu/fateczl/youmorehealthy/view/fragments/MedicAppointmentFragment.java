package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.MedicAppointmentController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.model.MedicAppointment;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicAppointmentFragment extends Fragment implements IEntityFragment<MedicAppointment> {

    private View view;
    private EditText etDescription;
    private EditText etDate;
    private EditText etTime;
    private EditText etDiagnostic;
    private Spinner spDoctor;
    private Button btSave;
    private Button btDelete;
    private MedicAppointment medicAppointment;
    private DatePickerFragment datePicker;
    private TimePickerFragment timePicker;
    private Calendar calendar;
    private MedicAppointmentController controller;

    public MedicAppointmentFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medic_appointment, container, false);
        etDescription = view.findViewById(R.id.etDescriptionMedic);
        etDate = view.findViewById(R.id.etDateMedic);
        etTime = view.findViewById(R.id.etTimeMedic);
        btSave = view.findViewById(R.id.btSaveMedic);
        btDelete = view.findViewById(R.id.btDeleteMedic);
        etDiagnostic = view.findViewById(R.id.etDiagnostic);
        spDoctor = view.findViewById(R.id.spDoctor);
        controller = new MedicAppointmentController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        calendar = Calendar.getInstance();
        datePicker = new DatePickerFragment(etDate, calendar);
        timePicker = new TimePickerFragment(etTime, calendar);
        spDoctor.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, controller.listDoctors()));
        etDate.setFocusable(false);
        etDate.setOnClickListener(l -> datePicker.show(requireActivity().getSupportFragmentManager(), null));
        etTime.setFocusable(false);
        etTime.setOnClickListener(l -> timePicker.show(requireActivity().getSupportFragmentManager(), null));
        populateFields();
        return view;
    }

    public void saveAction(){
        try {
            controller.saveMedicAppointment(buildEntity());
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
            controller.deleteMedicAppointment(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(MedicAppointment medicAppointment) {
        this.medicAppointment = medicAppointment;
    }

    @Override
    public MedicAppointment buildEntity() {
        medicAppointment.setDescription(etDescription.getText().toString());
        medicAppointment.setDate(calendar.getTime());
        medicAppointment.setDiagnostic(etDiagnostic.getText().toString());
        medicAppointment.setDoctor((Doctor) spDoctor.getSelectedItem());
        return medicAppointment;
    }

    @Override
    public void populateFields() {
        if(medicAppointment != null){
            etDescription.setText(medicAppointment.getDescription());
            calendar.setTime(medicAppointment.getDate());
            etDate.setText(ViewUtils.DATE_FORMAT.format(medicAppointment.getDate()));
            etTime.setText(ViewUtils.TIME_FORMAT.format(medicAppointment.getDate()));
            calendar.setTime(medicAppointment.getDate());
            etDiagnostic.setText(medicAppointment.getDiagnostic());
            int doctors = spDoctor.getAdapter().getCount();
            for(int i = 0; i < doctors; i++){
                Doctor d = (Doctor) spDoctor.getAdapter().getItem(i);
                if(d.getId() == medicAppointment.getDoctor().getId())
                    spDoctor.setSelection(i);
            }
        } else {
            etDiagnostic.setVisibility(View.GONE);
            medicAppointment = new MedicAppointment();
        }
    }
}