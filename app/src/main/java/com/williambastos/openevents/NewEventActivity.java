package com.williambastos.openevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Calendar calendarStartDate = Calendar.getInstance();
    TextView textStartDateTime, textEndDateTime;
    int day, month, year, hour, minute;
    int myDay, myMonth, myYear, myHour, myMinute;
    int IS_END;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ImageView startDate = findViewById(R.id.imageStartDateEvent);
        ImageView endDate = findViewById(R.id.imageEndDateEvent);
        textStartDateTime = findViewById(R.id.textStartDateTime);
        textEndDateTime = findViewById(R.id.textEndDateTime);
        IS_END = 0;
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendarStartDate.get(Calendar.YEAR);
                month = calendarStartDate.get(Calendar.MONTH);
                day = calendarStartDate.get(Calendar.DAY_OF_MONTH);
                IS_END = 0;
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, (DatePickerDialog.OnDateSetListener) NewEventActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendarStartDate.get(Calendar.YEAR);
                month = calendarStartDate.get(Calendar.MONTH);
                day = calendarStartDate.get(Calendar.DAY_OF_MONTH);
                IS_END = 1;
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, (DatePickerDialog.OnDateSetListener) NewEventActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myDay = dayOfMonth;
        myMonth = month+1;
        hour = calendarStartDate.get(Calendar.HOUR);
        minute = calendarStartDate.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventActivity.this, (TimePickerDialog.OnTimeSetListener) NewEventActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        String txt = myDay + "/" + myMonth +"/" + myYear + " - " + myHour + ":" + myMinute;
        if(IS_END == 0){
            textStartDateTime.setText(txt);
        }else{
            textEndDateTime.setText(txt);
        }
    }

    public void onClickAddNewEvent(View view) {
        //implements call API and verifications
        this.finish();
    }

    public void onclickBack(View view) {
        this.finish();
    }
}