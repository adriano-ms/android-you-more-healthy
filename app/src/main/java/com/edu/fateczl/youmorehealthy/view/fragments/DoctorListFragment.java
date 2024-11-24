package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.DoctorController;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;
import com.edu.fateczl.youmorehealthy.view.listeners.DoctorCardClickListener;
import com.edu.fateczl.youmorehealthy.view.adapters.RVDoctorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DoctorListFragment extends Fragment {

    private View view;
    private FloatingActionButton btNewDoctor;
    private RecyclerView rcDoctor;
    private RVDoctorAdapter adapter;
    private DoctorController controller;

    public DoctorListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
        btNewDoctor = view.findViewById(R.id.btNewDoctor);
        rcDoctor = view.findViewById(R.id.rcDoctor);
        controller = new DoctorController(view.getContext());
        btNewDoctor.setOnClickListener(b -> newDoctorAction());
        adapter = new RVDoctorAdapter(controller.listAll(), new DoctorCardClickListener(requireActivity().getSupportFragmentManager()));
        rcDoctor.setAdapter(adapter);
        rcDoctor.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private void newDoctorAction(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.tabFragment, FragmentFactory.createDoctorFragment(), true);
    }
}