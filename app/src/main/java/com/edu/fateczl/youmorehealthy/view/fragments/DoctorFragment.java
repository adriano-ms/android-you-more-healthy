package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.DoctorController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class DoctorFragment extends Fragment implements IEntityFragment<Doctor> {

    private View view;
    private EditText etName;
    private EditText etSpeciality;
    private Button btSave;
    private Button btDelete;
    private Doctor doctor;
    private DoctorController controller;

    public DoctorFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor, container, false);
        etName = view.findViewById(R.id.etNameDoctor);
        etSpeciality = view.findViewById(R.id.etSpecialityDoctor);
        btSave = view.findViewById(R.id.btSaveDoctor);
        btDelete = view.findViewById(R.id.btDeleteDoctor);
        controller = new DoctorController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        populateFields();
        return view;
    }

    private void saveAction(){
        try {
            controller.saveDoctor(buildEntity());
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
            controller.deleteDoctor(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public Doctor buildEntity() {
        doctor.setName(etName.getText().toString());
        doctor.setSpeciality(etSpeciality.getText().toString());
        return doctor;
    }

    @Override
    public void populateFields() {
        if(doctor != null){
            etName.setText(doctor.getName());
            etSpeciality.setText(doctor.getSpeciality());
        } else {
            doctor = new Doctor();
        }
    }
}