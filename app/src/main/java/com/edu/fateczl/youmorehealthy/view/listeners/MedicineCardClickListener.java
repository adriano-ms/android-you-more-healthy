package com.edu.fateczl.youmorehealthy.view.listeners;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;

public class MedicineCardClickListener implements OnCardClickListener<Medicine> {

    private final FragmentManager fragmentManager;

    public MedicineCardClickListener(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCardClick(CardView cardView, Medicine obj) {
        ViewUtils.clickAnimation(cardView, () -> {
            Fragment f = FragmentFactory.createMedicineFragment(obj);
            ViewUtils.changeFragment(fragmentManager, R.id.tabFragment, f, true);
        });
    }
}
