package com.edu.fateczl.youmorehealthy.view.listeners;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;

public class TrainingCardClickListener implements OnCardClickListener<Training> {

    private final FragmentManager fragmentManager;
    private final SportRoutine sportRoutine;

    public TrainingCardClickListener(FragmentManager fragmentManager, SportRoutine sportRoutine){
        this.fragmentManager = fragmentManager;
        this.sportRoutine = sportRoutine;
    }

    @Override
    public void onCardClick(CardView cardView, Training obj) {
        ViewUtils.clickAnimation(cardView, () -> {
            ViewUtils.changeFragment(fragmentManager, R.id.taskFragment, FragmentFactory.createTrainingFragment(sportRoutine, obj), true);
        });
    }
}
