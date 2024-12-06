package com.example.somnigraph;

import static com.example.somnigraph.DreamGraphView.DOT_SIZE_DP;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class DreamGraphView extends LinearLayout {
    private List<Dream> dreams;
    private String groupName;
    public static final int DOT_SIZE_DP = 40;
    public static final int SPACING_DP = 10;
    private Context context;

    public DreamGraphView(Context context) {
        super(context);
        init(context);
    }

    public DreamGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(VERTICAL);
        int padding = dpToPx(16);
        setPadding(padding, padding, padding, padding);

        setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setGravity(Gravity.CENTER_HORIZONTAL);
        setBackgroundResource(R.drawable.dream_group_background);
    }

    public void setData(String groupName, List<Dream> dreams) {
        this.groupName = groupName;
        this.dreams = dreams;
        setupView();
    }

    private void setupView() {
        removeAllViews();

        TextView titleView = new TextView(context);
        titleView.setText(DreamManager.getEmojiTag(groupName));
        titleView.setTextSize(20);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(getResources().getColor(R.color.dark_purple));
        titleView.setPadding(0, 0, 0, dpToPx(16));
        addView(titleView);

        LinearLayout dreamsContainer = new LinearLayout(context);
        dreamsContainer.setOrientation(VERTICAL);

        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dreamsContainer.setLayoutParams(containerParams);

        for (Dream dream : dreams) {
            DreamDotView dotView = new DreamDotView(context, dream);
            dreamsContainer.addView(dotView);
        }

        addView(dreamsContainer);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}

