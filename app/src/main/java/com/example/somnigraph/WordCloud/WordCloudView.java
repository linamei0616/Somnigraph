package com.example.somnigraph.WordCloud;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import java.util.*;

class WordCloudView extends View {
    private List<WordCloudItem> words = new ArrayList<>();
    private Paint textPaint;
    private Random random;
    private boolean isLayoutValid = false;
    private List<RectF> occupiedSpaces = new ArrayList<>();

    public WordCloudView(Context context) {
        super(context);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        random = new Random();
    }

    public void setWords(List<WordCloudItem> newWords) {
        words = newWords;
        isLayoutValid = false;
        occupiedSpaces.clear();
        requestLayout();
        invalidate();
    }

    public void clearWords() {
        words.clear();
        occupiedSpaces.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isLayoutValid) {
            layoutWords();
            isLayoutValid = true;
        }

        for (int i = 0; i < words.size() && i < occupiedSpaces.size(); i++) {
            WordCloudItem item = words.get(i);
            RectF bounds = occupiedSpaces.get(i);

            textPaint.setColor(item.color);
            textPaint.setTextSize(item.textSize);

            canvas.drawText(item.word,
                    bounds.left,
                    bounds.top + bounds.height(),
                    textPaint);
        }
    }

    private void layoutWords() {
        occupiedSpaces.clear();
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Sort words by size (larger words first)
        Collections.sort(words, (a, b) -> Float.compare(b.textSize, a.textSize));

        for (WordCloudItem item : words) {
            textPaint.setTextSize(item.textSize);
            Rect textBounds = new Rect();
            textPaint.getTextBounds(item.word, 0, item.word.length(), textBounds);

            // Try to find a spot for the word
            boolean placed = false;
            float angle = 0;
            float radius = 0;

            while (!placed && radius < Math.max(width, height)) {
                float x = centerX + (float)(radius * Math.cos(angle));
                float y = centerY + (float)(radius * Math.sin(angle));

                RectF wordRect = new RectF(
                        x - textBounds.width() / 2,
                        y - textBounds.height() / 2,
                        x + textBounds.width() / 2,
                        y + textBounds.height() / 2
                );

                if (canPlaceWord(wordRect)) {
                    occupiedSpaces.add(wordRect);
                    placed = true;
                }

                angle += Math.PI / 8;
                if (angle >= Math.PI * 2) {
                    angle = 0;
                    radius += textBounds.height();
                }
            }
        }
    }

    private boolean canPlaceWord(RectF newWord) {
        if (newWord.left < 0 || newWord.right > getWidth() ||
                newWord.top < 0 || newWord.bottom > getHeight()) {
            return false;
        }

        for (RectF existing : occupiedSpaces) {
            if (RectF.intersects(existing, newWord)) {
                return false;
            }
        }
        return true;
    }
}