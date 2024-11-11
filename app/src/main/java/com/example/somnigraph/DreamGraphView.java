package com.example.somnigraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import java.util.*;

public class DreamGraphView extends View {
    private Map<String, List<Dream>> dreamGroups;
    private Map<String, RectF> groupBounds;
    private Map<Dream, PointF> dreamPositions;
    private int requiredWidth;
    private int requiredHeight;
    private Paint circlePaint;
    private Paint textPaint;
    private Paint dreamDotPaint;
    private float padding = 50f;
    private float circleRadius = 200f;
    private float dreamDotRadius = 10f;
    private CardView popupView;
    private ViewGroup parent;
    private Dream selectedDream;
    private boolean isFirstPopup = true;
    private FrameLayout overlayContainer;

    private int rows;
    private int cols;
    private float cellWidth;
    private float cellHeight;


    public DreamGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dreamGroups = new HashMap<>();
        groupBounds = new HashMap<>();
        dreamPositions = new HashMap<>();

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(0xFFE6E6FA); // Light purple
        circlePaint.setAntiAlias(true);

        dreamDotPaint = new Paint();
        dreamDotPaint.setStyle(Paint.Style.FILL);
        dreamDotPaint.setColor(0xFF000000); // Black
        dreamDotPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFF4B0082); // Dark purple
        textPaint.setTextSize(36f); // Slightly smaller text size
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    private void initializePopup() {
        if (overlayContainer == null) return;

        popupView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.dream_popup, overlayContainer, false);

        ImageButton closeButton = popupView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> hidePopup());

        // Add click listener to overlay container to dismiss popup
        overlayContainer.setOnClickListener(v -> hidePopup());

        // Make popup clicks not dismiss the overlay
        popupView.setOnClickListener(v -> {});
    }
    private void showPopup(Dream dream, float x, float y) {
        if (overlayContainer == null) return;

        selectedDream = dream;

        TextView dateView = popupView.findViewById(R.id.dreamDate);
        TextView titleView = popupView.findViewById(R.id.dreamTitle);
        TextView descriptionView = popupView.findViewById(R.id.dreamDescription);

        dateView.setText(dream.loggedDate.toString());
        titleView.setText("Dream");
        descriptionView.setText(dream.dreamDescription);

        // Remove popup if it's already in the overlay
        if (popupView.getParent() != null) {
            ((ViewGroup) popupView.getParent()).removeView(popupView);
        }

        if (isFirstPopup) {
            // For the first popup, wait until the view is fully laid out
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    positionPopup(x, y);
                    isFirstPopup = false;
                }
            });
        } else {
            positionPopup(x, y);
        }

        overlayContainer.setVisibility(VISIBLE);
    }

    private void positionPopup(float x, float y) {
        // Add popup to overlay
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        // Calculate position in overlay coordinates
        int[] graphLocation = new int[2];
        getLocationInWindow(graphLocation);

        // Measure popup to get its dimensions
        popupView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        // Adjust position to stay within screen bounds
        float popupX = Math.min(x + graphLocation[0],
                overlayContainer.getWidth() - popupWidth);
        float popupY = Math.min(y + graphLocation[1],
                overlayContainer.getHeight() - popupHeight);
        popupX = Math.max(popupX, 0);
        popupY = Math.max(popupY, 0);

        params.leftMargin = (int) popupX;
        params.topMargin = (int) popupY;

        overlayContainer.addView(popupView, params);
    }

    // This initializes the overlay container when the view is attached
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Wait for the view to be laid out before finding the overlay container
        post(() -> {
            View root = getRootView();
            overlayContainer = root.findViewById(R.id.popupOverlayContainer);
            initializePopup();
        });
    }

    private void hidePopup() {
        if (overlayContainer != null) {
            overlayContainer.setVisibility(GONE);
            overlayContainer.removeView(popupView);
        }
        selectedDream = null;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            // Check if touch is on a dream dot
            for (Map.Entry<Dream, PointF> entry : dreamPositions.entrySet()) {
                PointF point = entry.getValue();
                float dx = x - point.x;
                float dy = y - point.y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= dreamDotRadius) {
                    Dream dream = entry.getKey();
                    showPopup(dream, x, y);
                    return true;
                }
            }

            // If touch is not on a dot and popup is visible, hide it
            if (popupView.getVisibility() == VISIBLE) {
                hidePopup();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public void setDreamGroups(Map<String, List<Dream>> groups) {
        this.dreamGroups = groups;
        calculateGroupPositions();
        invalidate();
    }



    private void calculateDreamDotPositions(List<Dream> dreams, RectF bounds) {
        int dotsCount = dreams.size();
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();
        float innerRadius = circleRadius * 0.5f;

        for (int i = 0; i < dotsCount; i++) {
            Dream dream = dreams.get(i);
            float angle = (float) (2 * Math.PI * i / dotsCount);
            float x = centerX + innerRadius * (float) Math.cos(angle);
            float y = centerY + innerRadius * (float) Math.sin(angle);

            dreamPositions.put(dream, new PointF(x, y));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Map.Entry<String, RectF> entry : groupBounds.entrySet()) {
            String tag = entry.getKey();
            RectF bounds = entry.getValue();

            // Draw circle
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), circleRadius, circlePaint);

            // Draw tag name with text wrapping
            drawWrappedText(canvas, tag, bounds.centerX(), bounds.centerY() - circleRadius - 20);
        }

        // Draw dream dots
        for (Map.Entry<Dream, PointF> entry : dreamPositions.entrySet()) {
            Dream dream = entry.getKey();
            PointF point = entry.getValue();

            // Highlight selected dream
            if (dream == selectedDream) {
                dreamDotPaint.setColor(0xFF4B0082); // Dark purple
                canvas.drawCircle(point.x, point.y, dreamDotRadius * 1.5f, dreamDotPaint);
                dreamDotPaint.setColor(0xFF000000); // Reset to black
            } else {
                canvas.drawCircle(point.x, point.y, dreamDotRadius, dreamDotPaint);
            }
        }
    }

    private void drawWrappedText(Canvas canvas, String text, float centerX, float y) {
        // Calculate maximum width for text
        float maxWidth = cellWidth * 0.8f; // Use 80% of cell width

        // Split text into words
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineY = y;

        for (String word : words) {
            float currentWidth = textPaint.measureText(line + " " + word);
            if (currentWidth > maxWidth && line.length() > 0) {
                // Draw current line and start new one
                canvas.drawText(line.toString(), centerX, lineY, textPaint);
                line = new StringBuilder(word);
                lineY += textPaint.getTextSize() * 1.2f; // Add line spacing
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        // Draw final line
        if (line.length() > 0) {
            canvas.drawText(line.toString(), centerX, lineY, textPaint);
        }
    }
    private void calculateGridDimensions() {
        if (dreamGroups.isEmpty()) return;

        int groupCount = dreamGroups.size();
        // Calculate optimal number of columns based on available width
        float availableWidth = getWidth() - (padding * 2);
        float minSpaceNeeded = (circleRadius * 2) + padding;
        cols = Math.max(1, (int)(availableWidth / minSpaceNeeded));

        // Calculate number of rows needed
        rows = (int) Math.ceil((float) groupCount / cols);

        // Calculate cell dimensions with equal spacing
        cellWidth = (getWidth() - (padding * 2)) / cols;
        cellHeight = (getHeight() - (padding * 2)) / rows;

        // Adjust circle radius if needed to fit cells
        float maxRadius = Math.min(cellWidth, cellHeight) * 0.4f; // Use 40% of cell size
        circleRadius = Math.min(circleRadius, maxRadius);
    }

    private void calculateGroupPositions() {
        groupBounds.clear();
        dreamPositions.clear();
        if (dreamGroups.isEmpty()) return;

        calculateGridDimensions();

        int index = 0;
        for (Map.Entry<String, List<Dream>> entry : dreamGroups.entrySet()) {
            String tag = entry.getKey();
            List<Dream> dreams = entry.getValue();

            // Calculate row and column for this group
            int row = index / cols;
            int col = index % cols;

            // Calculate center position for this circle
            float centerX = padding + (cellWidth * col) + (cellWidth / 2);
            float centerY = padding + (cellHeight * row) + (cellHeight / 2);

            RectF bounds = new RectF(
                    centerX - circleRadius,
                    centerY - circleRadius,
                    centerX + circleRadius,
                    centerY + circleRadius
            );
            groupBounds.put(tag, bounds);

            // Calculate positions for dream dots
            calculateDreamDotPositions(dreams, bounds);
            index++;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateGroupPositions();
    }



    private static class PointF {
        float x, y;
        PointF(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}