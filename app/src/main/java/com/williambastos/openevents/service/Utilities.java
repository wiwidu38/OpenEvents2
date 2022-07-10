package com.williambastos.openevents.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.adapter.RecyclerViewCards;
import com.williambastos.openevents.databinding.FragmentHomeBinding;
import com.williambastos.openevents.model.Card;
import com.williambastos.openevents.model.Event;
import com.williambastos.openevents.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Utilities {
    private String token;

    public Utilities(String token) {
        this.token = token;
    }

    public ArrayList<String> getCategories(Context context) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        ArrayList<String> categories = new ArrayList<>();

        Call<ArrayList<Event>> call = service.getEvents("Bearer " + token);
        call.enqueue(new Callback<ArrayList<Event>>() {

            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        ArrayList<Event> events = response.body();
                        process(events);
                    }
                } else {
                    Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            private void process(ArrayList<Event> events) {
                for (Event event : events) {
                    if (!categories.contains(event.getType())) {
                        categories.add(event.getType());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(context,"Connection error",Toast.LENGTH_SHORT).show();

            }
        });
        return categories;
    }
}
