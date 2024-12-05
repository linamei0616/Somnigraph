package com.example.somnigraph;

import static com.example.somnigraph.DreamGraphView.DOT_SIZE_DP;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DreamDotView extends LinearLayout {
    private Dream dream;
    private Context context;
    private View dotView;

    public DreamDotView(Context context, Dream dream) {
        super(context);
        this.context = context;
        this.dream = dream;
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        setGravity(Gravity.CENTER_VERTICAL);

        dotView = new View(context);
        int dotSize = dpToPx(40);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotSize, dotSize);
        dotView.setLayoutParams(dotParams);
        switch(dream.dreamIntensity)
        {
            default:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background1));
            case 1:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background1));
                break;
            case 2:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background2));
                break;
            case 3:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background3));
                break;
            case 4:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background4));
                break;
            case 5:
                dotView.setBackground(getResources().getDrawable(R.drawable.circle_background5));
                break;
        }

        TextView dateView = new TextView(context);
        dateView.setText(dream.getTitle());
        dateView.setPadding(dpToPx(8), 0, 0, 0);

        addView(dotView);
        addView(dateView);

        setOnClickListener(v -> showDreamPopup());
    }

    private void showDreamPopup() {
        View popupView = LayoutInflater.from(context).inflate(R.layout.dream_popup, null);

        TextView titleView = popupView.findViewById(R.id.dreamTitle);
        TextView contentView = popupView.findViewById(R.id.dreamContent);
        TextView dateView = popupView.findViewById(R.id.dreamDate);
        TextView dreamTags = popupView.findViewById(R.id.dreamTags);

        titleView.setText(dream.getTitle());
        contentView.setText(dream.getContent());
        dateView.setText(dream.getDate());
        dreamTags.setText(String.format(dream.getTagsAsString()));

        PopupWindow popup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popup.setOutsideTouchable(true);

        popup.setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        int[] location = new int[2];
        dotView.getLocationOnScreen(location);

        int x = location[0] - (popup.getWidth() / 2) + (dotView.getWidth() / 2);

        int y = location[1] - popup.getHeight();

        if (y < 0) {
            y = location[1] + dotView.getHeight();
        }

        popup.showAtLocation(dotView, Gravity.NO_GRAVITY, x, y);

        popupView.startAnimation(createPopupAnimation());
    }

    private Animation createPopupAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f,
                0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        scaleAnimation.setDuration(200);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        return animationSet;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}
