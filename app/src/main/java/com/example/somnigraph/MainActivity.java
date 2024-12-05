package com.example.somnigraph;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity
{
    DreamManager dreamManager;
    private int dreamIntensity = 3;
    private final int circleCount = 5;
    private List<View> circles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button tagAddBtn = (Button) findViewById(R.id.addTagButton);
        tagAddBtn.setOnClickListener(this::onTagPlusBtnClick);

        Button logBtn = (Button) findViewById(R.id.logBtn);
        logBtn.setOnClickListener(this::createDream);

        ImageButton micBtn = (ImageButton) findViewById(R.id.micBtn);
        micBtn.setOnClickListener(this::micBtnPress);

        Common.setupNavBar(this);
        setupIntensityButtons();
        setCircle();
    }

    private int getColorByName(String baseName, int number) {
        // Construct the full resource name, like "color1", "color2", etc.
        String resourceName = baseName + number;

        // Get the resource identifier dynamically
        int colorResId = getResources().getIdentifier(resourceName, "color", getPackageName());

        // Check if the resource was found, if not, return a default color
        if (colorResId != 0) {
            return getResources().getColor(colorResId, null);
        } else {
            return getResources().getColor(android.R.color.darker_gray, null); // Default color
        }
    }

    private void setCircle()
    {
        int index = 1;
        for(View circle : circles)
        {
            String resourceName = "circle_background" + index;
            int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            circle.setBackground(ContextCompat.getDrawable(this, resId));
            index += 1;
        }
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.selected_circle_background);
        if (layerDrawable != null) {
            Drawable innerDrawable = layerDrawable.getDrawable(0);
            if (innerDrawable instanceof GradientDrawable) {
                ((GradientDrawable) innerDrawable).setColor(getColorByName("circle", dreamIntensity));
            }
        }
        circles.get(dreamIntensity - 1).setBackground(ContextCompat.getDrawable(this, R.drawable.selected_circle_background));




    }

    private void setupIntensityButtons()
    {
        for(int circleIndex = 1; circleIndex <= circleCount; ++circleIndex)
        {
            String resourceName = "circle" + circleIndex;
            int resId = getResources().getIdentifier(resourceName, "id", getPackageName());
            View circleView = findViewById(resId);
            circles.add(circleView);
            circleView.setClickable(true);
            int finalCircleIndex = circleIndex;
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dreamIntensity = finalCircleIndex;
                    setCircle();
                }
            });
        }
    }

    private void onTagPlusBtnClick(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        Button confirmBtn = (Button) dialogView.findViewById(R.id.confirmTag);
        Button closeBtn = (Button) dialogView.findViewById(R.id.closeTag);

        TextInputEditText tagInputEditText = dialogView.findViewById(R.id.tagText);
        confirmBtn.setOnClickListener(v -> onTagSubmit(tagInputEditText, dialog));
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void onTagSubmit(TextInputEditText tagInputEditText, AlertDialog dialog) {
        android.text.Editable rawTag = tagInputEditText.getText();
        if (rawTag == null || rawTag.toString().trim().isEmpty()) {
            dialog.dismiss();
            return;
        }
        String tag = rawTag.toString().trim();

        LinearLayout tagContainer = findViewById(R.id.tagContainer);

        tagContainer.setGravity(Gravity.CENTER);
        LinearLayout tagLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,10,0);
        tagLayout.setLayoutParams(params);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setBackgroundResource(R.drawable.individual_tag_background);
        tagLayout.setGravity(Gravity.CENTER);
        tagLayout.setPadding(20, 5, 10, 5);


        TextView tagTextView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(0, 0, 20, 0);
        tagTextView.setLayoutParams(textParams);
        Optional<String> tagEmoji = dreamManager.getEmojiFromTag(tag);
        if(tagEmoji.isPresent())
        {
            tagTextView.setText(String.format("%s %s", tag, tagEmoji.get()));
        }
        else
        {
            tagTextView.setText(tag);
        }
        tagTextView.setTag(tag);
//        tagTextView.setText(tag);
        tagTextView.setTextSize(20);
        tagTextView.setTextColor(Color.BLACK);

        Space spaceFiller = new Space(this);
        spaceFiller.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        TextView deleteButton = new TextView(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        deleteButton.setText("X");
        deleteButton.setTextSize(20);
        deleteButton.setPadding(0, 0, 0, 0);
        deleteButton.setTextColor(Color.RED);
        deleteButton.setBackgroundColor(Color.TRANSPARENT);

        deleteButton.setOnClickListener(v -> {
            ((ViewGroup) tagLayout.getParent()).removeView(tagLayout);
        });


        tagLayout.addView(tagTextView);
        tagLayout.addView(spaceFiller);
        tagLayout.addView(deleteButton);

        tagContainer.addView(tagLayout);

        dialog.dismiss();
    }

    private void micBtnPress(View view)
    {
        TextView dreamTextView = (TextView) findViewById(R.id.loggingBox);
        dreamTextView.setText("I was trapped in the basement with all my friends and family. We played board games :).");

    }

    private void createDream(View view) {
        TextView dreamTextView = (TextView) findViewById(R.id.loggingBox);
        String dreamDescription = dreamTextView.getText().toString().trim();

        EditText dreamTitleView = (EditText) findViewById(R.id.titleBox);
        String dreamTitle = dreamTitleView.getText().toString().trim();

        if(dreamTitle.isEmpty())
        {
            Toast.makeText((Context) this, (CharSequence) "You must have a title", (int) 1f).show();
            return;
        }
        if(dreamDescription.isEmpty() )
        {
            Toast.makeText((Context) this, (CharSequence) "You must have a description", (int) 1f).show();
            return;
        }
        LinearLayout tagContainer = findViewById(R.id.tagContainer);
        List<String> tags = new ArrayList<>();

        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            View tagLayout = tagContainer.getChildAt(i);
            if (tagLayout instanceof LinearLayout) {
                TextView tagTextView = (TextView) ((LinearLayout) tagLayout).getChildAt(0);
                tags.add(tagTextView.getTag().toString());
            }
        }

        Dream dream = new Dream(tags, dreamDescription, dreamTitle, dreamIntensity);
        dreamManager.addDream(dream);

        dreamTitleView.setText("");
        dreamTextView.setText("");
        tagContainer.removeAllViews();
        Toast.makeText((Context) this, (CharSequence) "Dream Logged", (int) 1f).show();

    }
}