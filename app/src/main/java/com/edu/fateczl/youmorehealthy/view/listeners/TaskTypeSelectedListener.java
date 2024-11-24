package com.edu.fateczl.youmorehealthy.view.listeners;

import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentManager;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;

public class TaskTypeSelectedListener implements AdapterView.OnItemSelectedListener {

    private final FragmentManager fragmentManager;

    public TaskTypeSelectedListener(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ViewUtils.changeFragment(fragmentManager, R.id.taskTypeFragment, FragmentFactory.createTaskFragment((TaskType) parent.getItemAtPosition(position)), false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }
}
