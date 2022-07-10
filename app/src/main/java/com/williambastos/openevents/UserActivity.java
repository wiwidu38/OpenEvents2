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
import com.williambastos.openevents.model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private ImageView imageUser;
    private TextView id_user;
    private String token;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences sh = getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        idUser = getIntent().getExtras().getInt("id");

        name = findViewById(R.id.fullName_user);
        email = findViewById(R.id.email_user);
        imageUser = findViewById(R.id.image_user);
        id_user = findViewById(R.id.id_user);

        LinearLayout attendButton = findViewById(R.id.add_user);
        findUserById(idUser);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are adding new friend");
        builder.setMessage("Do you want to add this person to your friendlist ?");
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
    }

    public void onclickBack(View view) {
        this.finish();
    }

    private void findUserById(int id) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        Call<ArrayList<User>> call = service.getUser("Bearer " + token, id);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200) {
                        ArrayList<User> users = response.body();
                        if (users.get(0) != null) {
                            User user = users.get(0);
                            try {
                                displayEvent(user);
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
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayEvent(User user) throws ParseException {
        name.setText(user.getName() + " " + user.getLast_name());
        email.setText(user.getEmail());
        id_user.setText(String.valueOf(user.getId()));

        String url = "";

        if (user.getImage() != null) {
            if (user.getImage().startsWith("http")) {
                url = user.getImage();
            }
        }
        Glide.with(getApplicationContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.drawable.test))
                .load(url)
                .into(imageUser);
    }
}