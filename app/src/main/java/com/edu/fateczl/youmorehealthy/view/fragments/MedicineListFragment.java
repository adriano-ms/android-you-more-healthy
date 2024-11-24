package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.MedicineController;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;
import com.edu.fateczl.youmorehealthy.view.listeners.MedicineCardClickListener;
import com.edu.fateczl.youmorehealthy.view.adapters.RVMedicineAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MedicineListFragment extends Fragment {

    private View view;
    private FloatingActionButton btNewMedicine;
    private RecyclerView rcMedicine;
    private RVMedicineAdapter adapter;
    private MedicineController controller;

    public MedicineListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        btNewMedicine = view.findViewById(R.id.btNewMedicine);
        rcMedicine = view.findViewById(R.id.rcMedicine);
        btNewMedicine.setOnClickListener(b -> newMedicineAction());
        controller = new MedicineController(view.getContext());
        adapter = new RVMedicineAdapter(controller.listAll(), new MedicineCardClickListener(requireActivity().getSupportFragmentManager()));
        rcMedicine.setAdapter(adapter);
        rcMedicine.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private void newMedicineAction(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.tabFragment, FragmentFactory.createMedicineFragment(), true);
    }
}