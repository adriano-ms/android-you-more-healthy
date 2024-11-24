package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.MedicineController;
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class MedicineFragment extends Fragment implements IEntityFragment<Medicine> {

    private View view;
    private EditText etName;
    private EditText etDose;
    private Button btSave;
    private Button btDelete;
    private Medicine medicine;
    private MedicineController controller;

    public MedicineFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine, container, false);
        etName = view.findViewById(R.id.etNameMedicine);
        etDose = view.findViewById(R.id.etDoseMedicine);
        btSave = view.findViewById(R.id.btSaveMedicine);
        btDelete = view.findViewById(R.id.btDeleteMedicine);
        controller = new MedicineController(view.getContext());
        btSave.setOnClickListener(b -> saveAction());
        btDelete.setOnClickListener(b -> deleteAction());
        populateFields();
        return view;
    }

    private void saveAction(){
        try {
            controller.saveMedicine(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_insert_update_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        } catch (FormException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    private void deleteAction(){
        try {
            controller.deleteMedicine(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public Medicine buildEntity() {
        medicine.setName(etName.getText().toString());
        medicine.setDose(etDose.getText().toString());
        return medicine;
    }

    @Override
    public void populateFields() {
        if(medicine != null){
            etName.setText(medicine.getName());
            etDose.setText(medicine.getDose());
        } else {
            medicine = new Medicine();
        }
    }
}