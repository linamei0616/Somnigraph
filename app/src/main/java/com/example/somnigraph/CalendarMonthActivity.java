package com.example.somnigraph;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

public class CalendarMonthActivity extends Activity {
    DreamManager dreamManager;

    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_monthview);
        Common.setupNavBar(this);
        Spinner tagSpinner = (Spinner) findViewById(R.id.sort_by_spinner);



    }
}
