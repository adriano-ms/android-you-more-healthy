package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.view.listeners.DataTabSelectedListener;
import com.google.android.material.tabs.TabLayout;

public class ManageDataFragment extends Fragment {

    private View view;

    public ManageDataFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_data, container, false);
        TabLayout tlTasksData = view.findViewById(R.id.tlTasksData);
        TabLayout.Tab tbDoctor = tlTasksData.newTab().setText(R.string.tb_doctor).setId(0);
        TabLayout.Tab tbMedicine = tlTasksData.newTab().setText(R.string.cb_medicine_task).setId(1);
        tlTasksData.addTab(tbDoctor);
        tlTasksData.addTab(tbMedicine);
        TabLayout.OnTabSelectedListener listener = new DataTabSelectedListener(requireActivity().getSupportFragmentManager());
        tlTasksData.addOnTabSelectedListener(listener);
        listener.onTabSelected(tbDoctor);
        return view;
    }
}