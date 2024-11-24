package com.edu.fateczl.youmorehealthy.view.listeners;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;
import com.edu.fateczl.youmorehealthy.model.Task;

public class TaskCardClickListener implements OnCardClickListener<Task> {

    private final FragmentManager fragmentManager;

    public TaskCardClickListener(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCardClick(CardView cardView, Task obj) {
        ViewUtils.clickAnimation(cardView, () -> {
            Fragment f = FragmentFactory.createTaskFragment(obj);
            ViewUtils.changeFragment(fragmentManager, R.id.taskFragment, f, true);
        });
    }
}
