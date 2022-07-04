package com.williambastos.openevents.ui.my_events;

import android.content.Context;
import android.content.Intent;
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
import com.williambastos.openevents.NewEventActivity;
import com.williambastos.openevents.adapter.RecyclerViewCards;
import com.williambastos.openevents.databinding.FragmentMyEventsBinding;
import com.williambastos.openevents.model.Card;
import com.williambastos.openevents.model.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyEventsFragment extends Fragment {

    private String token;
    private int idUser;
    private FragmentMyEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        binding.addButton.setOnClickListener(new View.OnClickListener() { // used to callback onclick the + button
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), NewEventActivity.class);
                MyEventsFragment.this.startActivity(myIntent);
            }
        });
        View root = binding.getRoot();
        SharedPreferences sh = getActivity().getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        idUser = Integer.valueOf(sh.getString("id","1"));
        myEvents();
        return root;
    }

    private void myEvents() {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);

        Call<ArrayList<Event>> call = service.getEvents("Bearer " + token);
        call.enqueue(new Callback<ArrayList<Event>>() {

            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        ArrayList<Event> events = response.body();
                        ArrayList<Event> my_events = new ArrayList<>();
                        RecyclerView recyclerViewCards = binding.listCards;
                        recyclerViewCards.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerViewCards.setLayoutManager(llm);
                        selectMyEvent(events,my_events);
                        displayCards(recyclerViewCards, my_events);
                    }
                } else {
                    Toast.makeText(getContext(), "Incorrect data. Please try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(getContext(),"Connection error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void selectMyEvent(ArrayList<Event> events, ArrayList<Event> my_events) {
        for(Event event : events){
            if(event.getOwnerId() == idUser)
                my_events.add(event);
        }
    }

    private void displayCards(RecyclerView recyclerViewCards, ArrayList<Event> events) {
        ArrayList<Card> cards = new ArrayList<>();
        initializeDataCards(cards, events);
        RecyclerViewCards adapter = new RecyclerViewCards(cards);
        recyclerViewCards.setAdapter(adapter);
    }

    private void initializeDataCards(List<Card> cards, ArrayList<Event> events){
        for (Event event : events) {
            cards.add(new Card(event.getName(),event.getStartDate(), event.getLocation(), event.getImage(),event.getId()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}