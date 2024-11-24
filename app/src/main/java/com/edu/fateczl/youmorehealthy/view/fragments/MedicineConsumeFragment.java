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
import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.controller.MedicineConsumeController;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.model.MedicineConsume;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicineConsumeFragment extends Fragment implements IEntityFragment<MedicineConsume> {

    private View view;
    private EditText etDescription;
    private EditText etTime;
    private EditText etCycle;
    private EditText etRepetitions;
    private Spinner spMedicine;
    private Button btSave;
    private Button btDelete;
    private MedicineConsume medicineConsume;
    private TimePickerFragment timePicker;
    private Calendar calendar;
    private MedicineConsumeController controller;

    public MedicineConsumeFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine_consume, container, false);
        etDescription = view.findViewById(R.id.etDescriptionMedicine);
        etTime = view.findViewById(R.id.etTimeMedicine);
        etCycle = view.findViewById(R.id.etCycleMedicine);
        etRepetitions = view.findViewById(R.id.etRepetitionsMedicine);
        spMedicine = view.findViewById(R.id.spMedicine);
        btSave = view.findViewById(R.id.btSaveMedicineConsume);
        btDelete = view.findViewById(R.id.btDeleteMedicineConsume);
        controller = new MedicineConsumeController(view.getContext());
        spMedicine.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, controller.listMedicines()));
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
            controller.saveMedicineConsume(buildEntity());
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
            controller.deleteMedicineConsume(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_delete_positive);
            requireActivity().getSupportFragmentManager().popBackStack();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        } catch (FormException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    @Override
    public void setEntity(MedicineConsume medicineConsume) {
        this.medicineConsume = medicineConsume;
    }

    @Override
    public MedicineConsume buildEntity() {
        medicineConsume.setDescription(etDescription.getText().toString());
        medicineConsume.getFrequency().setHour(calendar.get(Calendar.HOUR_OF_DAY));
        medicineConsume.getFrequency().setMinute(calendar.get(Calendar.MINUTE));
        try {
            medicineConsume.getFrequency().setCycleInHours(Integer.parseInt(etCycle.getText().toString()));
            medicineConsume.getFrequency().setRepetitions(Integer.parseInt(etRepetitions.getText().toString()));
        } catch (NumberFormatException e) {
            throw new FormException();
        }
        medicineConsume.setMedicine((Medicine) spMedicine.getSelectedItem());
        return medicineConsume;
    }

    @Override
    public void populateFields() {
        if(medicineConsume != null && medicineConsume.getFrequency() != null){
            etDescription.setText(medicineConsume.getDescription());
            calendar.set(Calendar.HOUR_OF_DAY, medicineConsume.getFrequency().getHour());
            calendar.set(Calendar.MINUTE, medicineConsume.getFrequency().getMinute());
            etTime.setText(ViewUtils.TIME_FORMAT.format(calendar.getTime()));
            etCycle.setText(String.valueOf(medicineConsume.getFrequency().getCycleInHours()));
            etRepetitions.setText(String.valueOf(medicineConsume.getFrequency().getRepetitions()));
            int medicines = spMedicine.getAdapter().getCount();
            for(int i = 0; i < medicines; i++){
                Medicine m = (Medicine) spMedicine.getAdapter().getItem(i);
                if(m.getId() == medicineConsume.getMedicine().getId())
                    spMedicine.setSelection(i);
            }
        } else {
            medicineConsume = new MedicineConsume();
            medicineConsume.setFrequency(new Frequency());
        }
    }
}