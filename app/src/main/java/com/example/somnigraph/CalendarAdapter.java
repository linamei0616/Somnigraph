package com.example.somnigraph;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MonthViewHolder> {
    private Context context;
    private List<Calendar> months;

    public CalendarAdapter(Context context, List<Calendar> months) {
        this.context = context;
        this.months = months;
    }

    public Calendar getMonthAtPosition(int position) {
        return months.get(position);
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        Calendar month = months.get(position);

        holder.monthTitle.setText(getMonthYearString(month));

        holder.setupDaysGrid(month);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView monthTitle;
        GridLayout daysGrid;

        MonthViewHolder(View itemView) {
            super(itemView);
            monthTitle = itemView.findViewById(R.id.monthTitle);
            daysGrid = itemView.findViewById(R.id.daysGrid);
        }

        void setupDaysGrid(Calendar month) {
            daysGrid.removeAllViews();

            int daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH);
            int firstDayOfWeek = month.get(Calendar.DAY_OF_WEEK) - 1;

            for (int i = 0; i < firstDayOfWeek; i++) {
                TextView emptyView = new TextView(context);
                GridLayout.LayoutParams emptyParams = new GridLayout.LayoutParams();
                emptyParams.width = 0;
                emptyParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                emptyParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                emptyView.setLayoutParams(emptyParams);
                daysGrid.addView(emptyView);
            }

            for (int i = 1; i <= daysInMonth; i++) {
                TextView dayView = new TextView(context);
                dayView.setText(String.valueOf(i));
                dayView.setGravity(Gravity.CENTER);
                dayView.setTextSize(14);
                dayView.setPadding(8, 16, 8, 75);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                dayView.setLayoutParams(params);
                daysGrid.addView(dayView);
            }
        }




    }

    private String getMonthYearString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
