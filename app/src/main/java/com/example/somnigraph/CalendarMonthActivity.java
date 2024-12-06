package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.util.Log;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarMonthActivity extends Activity {
    DreamManager dreamManager;
    private TextView monthTitle;
    private RecyclerView calendarRecyclerView;
    private LinearLayoutManager layoutManager;
    private CalendarAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_monthview);

        Common.setupNavBar(this);
        setupSpinner();

        monthTitle = findViewById(R.id.monthTitleTextView);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        calendarRecyclerView.setLayoutManager(layoutManager);

        List<Calendar> months = generateMonths();
        adapter = new CalendarAdapter(this, months, dreamManager);
        calendarRecyclerView.setAdapter(adapter);

        calendarRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                updateMonthTitle(firstVisibleItemPosition);
            }
        });
    }

    private List<Calendar> generateMonths() {
        List<Calendar> months = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        current.set(Calendar.DAY_OF_MONTH, 1);

        for (int i = 0; i < 60; i++) {
            Calendar month = (Calendar) current.clone();
            months.add(month);
            current.add(Calendar.MONTH, -1);
        }
        return months;
    }

    private void updateMonthTitle(int position) {
        Calendar currentMonth = adapter.getMonthAtPosition(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        String monthName = sdf.format(currentMonth.getTime());
        monthTitle.setText(monthName);
    }

    private void setupSpinner() {
        Spinner sortBySpinner = findViewById(R.id.sort_by_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_by_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setSelection(1);

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Week view
                        Intent weekIntent = new Intent(CalendarMonthActivity.this, CalendarWeekActivity.class);
                        startActivity(weekIntent);
                        finish();
                        break;
                    case 1: // Month view
                        if (!(CalendarMonthActivity.this instanceof CalendarMonthActivity)) {
                            Intent monthIntent = new Intent(CalendarMonthActivity.this, CalendarMonthActivity.class);
                            startActivity(monthIntent);
                            finish();
                        }
                        break;
                    case 2: // Timeline view
                        Intent timelineIntent = new Intent(CalendarMonthActivity.this, CalendarTimeLineActivity.class);
                        startActivity(timelineIntent);
                        finish();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
