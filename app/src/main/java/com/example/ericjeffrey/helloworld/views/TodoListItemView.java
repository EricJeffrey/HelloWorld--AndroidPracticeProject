package com.example.ericjeffrey.helloworld.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ericjeffrey.helloworld.R;

public class TodoListItemView extends LinearLayout {
    private static final String TAG = "TodoListItemView";

    private static final int DURATION = 400;
    private static final int MAX_HEIGHT = 500;

    private boolean contentExpanded;

    private TextView titleView;
    private TextView contentView;
    private ImageButton showContentBtn;
    private ScrollView contentHolderView;

    public TodoListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ll_todo_list_item_view, this);

        contentExpanded = false;
        titleView = findViewById(R.id.tv_todo_item_title);
        contentView = findViewById(R.id.tv_todo_item_content);
        showContentBtn = findViewById(R.id.img_btn_todo_item);
        contentHolderView = findViewById(R.id.scr_todo_item_content_holder);

        showContentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = contentExpanded ? ValueAnimator.ofInt(MAX_HEIGHT, 0) : ValueAnimator.ofInt(0, MAX_HEIGHT);
                valueAnimator.setDuration(DURATION);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        contentHolderView.getLayoutParams().height = (int) animation.getAnimatedValue();
                        contentHolderView.requestLayout();
                    }
                });
                valueAnimator.start();
                contentExpanded = !contentExpanded;
            }
        });
    }

    public void setTitleAndContent(String title, String content) {
        titleView.setText(title);
        contentView.setText(content);
    }

    public void setImaBtnVisible(boolean visible) {
        if (visible)
            showContentBtn.setVisibility(VISIBLE);
        else
            showContentBtn.setVisibility(GONE);
    }
}
