package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.MedicAppointmentController;
import com.edu.fateczl.youmorehealthy.model.MedicAppointment;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class MedicAppointmentDoneFragment extends Fragment implements IEntityFragment<MedicAppointment> {

    private View view;
    private TextView tvMedicAppointmentTitle;
    private TextView tvDoctorName;
    private TextView tvDoctorSpeciality;
    private TextView tvAppointmentDate;
    private EditText etDiagnosticDone;
    private Button btSaveMedicAppointmentDone;
    private MedicAppointment medicAppointment;
    private MedicAppointmentController controller;

    public MedicAppointmentDoneFragment(int id) {
        medicAppointment = new MedicAppointment();
        medicAppointment.setId(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medic_appointment_done, container, false);
        tvMedicAppointmentTitle = view.findViewById(R.id.tvMedicAppointmentTitle);
        tvDoctorName = view.findViewById(R.id.tvDoctorName);
        tvDoctorSpeciality = view.findViewById(R.id.tvDoctorSpeciality);
        tvAppointmentDate = view.findViewById(R.id.tvAppointmentDate);
        etDiagnosticDone = view.findViewById(R.id.etDiagnosticDone);
        btSaveMedicAppointmentDone = view.findViewById(R.id.btSaveMedicAppointmentDone);
        controller = new MedicAppointmentController(view.getContext());
        btSaveMedicAppointmentDone.setOnClickListener(b -> saveAction());
        populateFields();
        return view;
    }

    private void saveAction(){
        try {
            controller.doneMedicAppointment(buildEntity());
            ViewUtils.showToast(view.getContext(), R.string.toast_insert_update_positive);
            backToTasksManage();
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
        }
    }

    private void backToTasksManage(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.taskFragment, new ManageTasksFragment(), false);
    }

    @Override
    public void setEntity(MedicAppointment medicAppointment) {
        this.medicAppointment = medicAppointment;
    }

    @Override
    public MedicAppointment buildEntity() {
        medicAppointment.setDiagnostic(etDiagnosticDone.getText().toString());
        return medicAppointment;
    }

    @Override
    public void populateFields() {
        try {
            medicAppointment = controller.findMedicAppointment(medicAppointment.getId());
            tvMedicAppointmentTitle.setText(medicAppointment.getDescription());
            tvAppointmentDate.setText(ViewUtils.DATE_TIME_FORMAT.format(medicAppointment.getDate()));
            tvDoctorName.setText(medicAppointment.getDoctor().getName());
            tvDoctorSpeciality.setText(medicAppointment.getDoctor().getSpeciality());
        } catch (DatabaseException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
            backToTasksManage();
        } catch (ResourceNotFoundException e) {
            ViewUtils.showErrorDialog(view.getContext(), e.getMsgId());
            backToTasksManage();
        }
    }
}