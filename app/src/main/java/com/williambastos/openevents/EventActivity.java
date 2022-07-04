package com.williambastos.openevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.model.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity {

    private TextView eventName;
    private TextView startDateEvent;
    private TextView endDateEvent;
    private TextView geoEvent;
    private TextView descriptionEvent;
    private ImageView imageEvent;
    private String token;
    private Event event;
    private int idEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        SharedPreferences sh = getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        idEvent = getIntent().getExtras().getInt("id");

        eventName = findViewById(R.id.title_event);
        imageEvent = findViewById(R.id.image_event);
        startDateEvent = findViewById(R.id.dateStart_event);
        endDateEvent = findViewById(R.id.dateEnd_event);
        geoEvent = findViewById(R.id.geo_event);
        descriptionEvent = findViewById(R.id.description_event);
        LinearLayout attendButton = findViewById(R.id.attend_event);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are attending this event");
        builder.setMessage("Do you want to add this event to your calendar ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });
        findEventById(idEvent);
    }

    public void onclickBack(View view) {
        this.finish();
    }

    private void findEventById(int id) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        Call<ArrayList<Event>> call = service.getEvent("Bearer " + token, id);
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200) {
                        ArrayList<Event> events = response.body();
                        if (events.get(0)!= null) {
                            event = events.get(0);
                            try {
                                displayEvent(events.get(0));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayEvent(Event event) throws ParseException {
        eventName.setText(event.getName());
        startDateEvent.setText(event.getStartDate());
        endDateEvent.setText(event.getEndDate());
        geoEvent.setText(event.getLocation());
        descriptionEvent.setText(event.getDescription());

        String url = "";

        if (this.event.getImage() != null) {
            if (this.event.getImage().startsWith("http")) {
                url = this.event.getImage();
            } else {
                url = "https://172.16.205.68/img/" + this.event.getImage();
            }
        }
        Glide.with(getApplicationContext())
                .load(url)
                .into(imageEvent);
    }
}