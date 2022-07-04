package com.williambastos.openevents.ui.search_users;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.R;
import com.williambastos.openevents.adapter.UsersAdapter;
import com.williambastos.openevents.databinding.FragmentSearchUsersBinding;
import com.williambastos.openevents.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchUsersFragment extends Fragment {

private FragmentSearchUsersBinding binding;

    private RecyclerView usersRecycleView;
    private UsersAdapter users_adapter;
    private ArrayList<User> usersList;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);
        usersRecycleView = view.findViewById(R.id.recyclerViewUsers);
        EditText textSearch = view.findViewById(R.id.EditTextSearchUser);
        ImageView searchUsersImageView = view.findViewById(R.id.searchButton);
        SharedPreferences sh = getActivity().getSharedPreferences("sh", Context.MODE_PRIVATE);
        token = sh.getString("token","123456");
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        usersRecycleView.setLayoutManager(llm);
        getUserList();

        searchUsersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchUser = textSearch.getText().toString();
                searchUsers(searchUser);
            }
        });
        return view;
    }

    public void searchUsers(String s) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        Call<ArrayList<User>> call = service.searchUsers("Bareer " + token, s);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        usersList = response.body();
                        users_adapter = new UsersAdapter(usersList, getContext());
                        usersRecycleView.setAdapter(users_adapter);
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

    public void getUserList() {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);

        Call<ArrayList<User>> call = service.getUsers("Bareer " + token);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        usersList = response.body();
                        users_adapter = new UsersAdapter(usersList, getContext());
                        usersRecycleView.setAdapter(users_adapter);
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