package com.edu.fateczl.youmorehealthy.view.listeners;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.DoctorListFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicineListFragment;
import com.google.android.material.tabs.TabLayout;

public class DataTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private final FragmentManager fragmentManager;

    public DataTabSelectedListener(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getId() == 0)
            loadFragment(new DoctorListFragment());
        else
            loadFragment(new MedicineListFragment());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void loadFragment(Fragment f){
        ViewUtils.changeFragment(fragmentManager, R.id.tabFragment, f, true);
    }
}
