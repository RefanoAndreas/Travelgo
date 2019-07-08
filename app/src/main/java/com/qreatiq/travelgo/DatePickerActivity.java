package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Parcel;
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
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
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
    ArrayList<Long> dateSelected = new ArrayList<Long>();
    Date start_date,end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        set_toolbar();

//        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
//        long date = calendar.getTime().getTime();
        final Intent intent = getIntent();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        start_date = new Date(intent.getLongExtra("start_date",0));
        if(intent.getBooleanExtra("isReturn",false))
            end_date = new Date(intent.getLongExtra("end_date",0));

        submit = (MaterialButton) findViewById(R.id.submit);
        layout = (CoordinatorLayout) findViewById(R.id.layout);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)))
                .commit();

        if(intent.getBooleanExtra("isReturn",false)) {
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
            if(start_date.getTime() != end_date.getTime()) {
                calendarView.selectRange(
                        CalendarDay.from(start_date.getYear() + 1900, start_date.getMonth() + 1, start_date.getDate()),
                        CalendarDay.from(end_date.getYear() + 1900, end_date.getMonth() + 1, end_date.getDate())
                );
            }
        }
        else {
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
            calendarView.setSelectedDate(CalendarDay.from(start_date.getYear()+1900,start_date.getMonth()+1,start_date.getDate()));
        }

        calendarView.setCurrentDate(CalendarDay.from(start_date.getYear()+1900,start_date.getMonth()+1,start_date.getDate()));

        format = new SimpleDateFormat("d-M-yyyy");
        dateSelected.add(start_date.getTime());
        if(intent.getBooleanExtra("isReturn",false))
            dateSelected.add(end_date.getTime());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateSelected.clear();
                Date d = new Date(date.getYear()-1900,date.getMonth()-1,date.getDay());
                dateSelected.add(d.getTime());
            }
        });

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                dateSelected.clear();

                for(int x=0; x<dates.size(); x++) {
                    Date d = new Date(dates.get(x).getYear()-1900,dates.get(x).getMonth()-1,dates.get(x).getDay());
                    dateSelected.add(d.getTime());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelected.toString().equals("[]")){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.datepicker_error_date_empty_label), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(intent.getBooleanExtra("isReturn",false) && dateSelected.size() == 1) {
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.datepicker_error_date_return_label), Snackbar.LENGTH_SHORT);
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
