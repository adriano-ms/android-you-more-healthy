package com.edu.fateczl.youmorehealthy.view.listeners;

import androidx.cardview.widget.CardView;

public interface OnCardClickListener<T> {

    void onCardClick(CardView cardView, T obj);
}
