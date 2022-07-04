package com.williambastos.openevents.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.R;
import com.williambastos.openevents.adapter.RecyclerViewCards;
import com.williambastos.openevents.adapter.UsersAdapter;
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

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;
private String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences sh = getActivity().getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        listEvents();
        getIdUser();
        return root;
    }

    private void listEvents() {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);

        Call<ArrayList<Event>> call = service.getEvents("Bearer " + token);
        call.enqueue(new Callback<ArrayList<Event>>() {

            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        ArrayList<Event> events = response.body();
                        RecyclerView recyclerViewCards = binding.listCards;
                        recyclerViewCards.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerViewCards.setLayoutManager(llm);
                        displayCards(recyclerViewCards, events);
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

    public void getIdUser() {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);

        Call<ArrayList<User>> call = service.getUsers("Bareer " + token);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        ArrayList<User> users = response.body();

                        SharedPreferences preferences = getActivity().getSharedPreferences("sh", Context.MODE_PRIVATE);
                        String email_user = preferences.getString("email",null);
                        SharedPreferences.Editor editor = preferences.edit();

                        for (User user : users){
                            if(email_user.equals(user.getEmail())){
                                int id_final = user.getId();
                                editor.putString("id", String.valueOf(id_final));
                                editor.apply();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Incorrect data. Please try again!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(getContext(),"NO INTERNET",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}