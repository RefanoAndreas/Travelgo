package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatePickerActivity extends BaseActivity {

    MaterialCalendarView calendarView;
    MaterialButton submit;
    CoordinatorLayout layout;
    Calendar calendar = Calendar.getInstance();
    boolean flag=true;
    ArrayList<String> dateSelected = new ArrayList<String>();
    Date start_date,end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        set_toolbar();

//        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
//        long date = calendar.getTime().getTime();
        Intent intent = getIntent();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            start_date = format.parse(intent.getStringExtra("start_date"));
            end_date = format.parse(intent.getStringExtra("end_date"));
            Log.d("date", String.valueOf(start_date.getDate()));
            Log.d("date", String.valueOf(start_date.getMonth()));
            Log.d("date", String.valueOf(start_date.getYear()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        submit = (MaterialButton) findViewById(R.id.submit);
        layout = (CoordinatorLayout) findViewById(R.id.layout);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)))
                .commit();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);

        calendarView.selectRange(
                CalendarDay.from(start_date.getYear(),start_date.getMonth()+1,start_date.getDate()),
                CalendarDay.from(end_date.getYear(),end_date.getMonth()+1,end_date.getDate()));
        calendarView.setCurrentDate(CalendarDay.from(start_date.getYear(),start_date.getMonth()+1,start_date.getDate()));

        format = new SimpleDateFormat("d-M-yyyy");
        dateSelected.add(format.format(start_date));
        dateSelected.add(format.format(end_date));

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                dateSelected.clear();
                for(int x=0; x<dates.size(); x++)
                    dateSelected.add(dates.get(x).getDay()+"-"+dates.get(x).getMonth()+"-"+dates.get(x).getYear());
//                Log.d("data",dateSelected.toString());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelected.toString().equals("[]")){
                    Snackbar snackbar = Snackbar.make(layout,"Date is not in range", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else{
                    Intent in = new Intent();
                    in.putExtra("date",dateSelected.toString());
                    setResult(RESULT_OK,in);
                    finish();
                }
            }
        });
    }
}
