package com.example.somnigraph;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somnigraph.WordCloud.WordCloudActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    DreamManager dreamManager;
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

        Common.setupNavBar(this);
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
        tagTextView.setText(tag);
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
        tagLayout.addView(deleteButton);

        tagContainer.addView(tagLayout);

        dialog.dismiss();
    }

    private void createDream(View view) {
        TextView dreamTextView = (TextView) findViewById(R.id.loggingBox);
        String dreamDescription = dreamTextView.getText().toString().trim();

        EditText dreamTitleView = (EditText) findViewById(R.id.titleBox);
        String dreamTitle = dreamTextView.getText().toString().trim();

        LinearLayout tagContainer = findViewById(R.id.tagContainer);
        List<String> tags = new ArrayList<>();

        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            View tagLayout = tagContainer.getChildAt(i);
            if (tagLayout instanceof LinearLayout) {
                TextView tagTextView = (TextView) ((LinearLayout) tagLayout).getChildAt(0);
                tags.add(tagTextView.getText().toString());
            }
        }

        Dream dream = new Dream(tags, dreamDescription, dreamTitle);
        dreamManager.addDream(dream);

        dreamTextView.setText("");
        tagContainer.removeAllViews();
    }
}