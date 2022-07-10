package com.williambastos.openevents;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.model.Event;
import com.williambastos.openevents.service.Utilities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Calendar calendarDate;
    TextView textStartDateTime, textEndDateTime;
    EditText title, description, eventPicture, location, categories;
    Button buttonAddEvent;
    String token;
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
        title = findViewById(R.id.editTextTitleEvent);
        location = findViewById(R.id.editTextLocationEvent);
        description = findViewById(R.id.editTextDescriptionEvent);
        eventPicture = findViewById(R.id.editTextPhotoEvent);
        buttonAddEvent = findViewById(R.id.buttonNewEvent);
        categories = findViewById(R.id.editTextCategoryEvent);

        calendarDate = Calendar.getInstance();

//        Utilities util = new Utilities(token);
//        ArrayList<String> categoriesList = util.getCategories(getApplicationContext());
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, categoriesList);
//        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_item);
//
//        categories.setAdapter(spinnerArrayAdapter);

        SharedPreferences sh = getApplicationContext().getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        IS_END = 0;

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendarDate.get(Calendar.YEAR);
                month = calendarDate.get(Calendar.MONTH);
                day = calendarDate.get(Calendar.DAY_OF_MONTH);
                IS_END = 0;
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, (DatePickerDialog.OnDateSetListener) NewEventActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendarDate.get(Calendar.YEAR);
                month = calendarDate.get(Calendar.MONTH);
                day = calendarDate.get(Calendar.DAY_OF_MONTH);
                IS_END = 1;
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, (DatePickerDialog.OnDateSetListener) NewEventActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textStartDateTime.getText().toString().equals("") &&
                        !textEndDateTime.getText().toString().equals("") &&
                        !title.getText().toString().equals("") &&
                        !description.getText().toString().equals("") &&
                        !location.getText().toString().equals("") &&
                        !categories.getText().toString().equals("") &&
                        !eventPicture.getText().toString().equals("")){
                    addEvent(title.getText().toString(), eventPicture.getText().toString(), location.getText().toString(),
                            description.getText().toString(), textStartDateTime.getText().toString(), textEndDateTime.getText().toString(),
                            categories.getText().toString(),"0");
                }else{
                    Toast.makeText(NewEventActivity.this, "You must fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myDay = dayOfMonth;
        myMonth = month+1;
        hour = calendarDate.get(Calendar.HOUR);
        minute = calendarDate.get(Calendar.MINUTE);
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

        this.finish();
    }

    public void onclickBack(View view) {
        this.finish();
    }

    public void addEvent(String name, String image, String location, String description, String eventStart_date,
                            String eventEnd_date, String type, String capacity) {

        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);

        Call<Event> call = service.createEvent(
                "Bearer "+ token,name,image,location,description,eventStart_date,eventEnd_date,Integer.parseInt(capacity),type);

        call.enqueue(new retrofit2.Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        Toast.makeText(getApplicationContext(), "New event was created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                        startActivity(intent);
                    }
                    else if (response.code() == 400){
                        Toast.makeText(getApplicationContext(),"Data is incorrect! Please enter another information", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        System.out.println(response.errorBody().toString());
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}