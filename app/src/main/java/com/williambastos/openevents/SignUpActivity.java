package com.williambastos.openevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends Activity {

    EditText name, lastName, password, passwordRepeat, email, image;
    ImageView backLogin;
    Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);
        password = (EditText) findViewById(R.id.editTextPassword);
        passwordRepeat = (EditText) findViewById(R.id.editTextPasswordRepeat);
        email = (EditText) findViewById(R.id.editTextEmail);
        image = (EditText) findViewById(R.id.editTextProfilePicture);
        createAccount = (Button) findViewById(R.id.buttonRegister);
        backLogin = (ImageView) findViewById(R.id.backLogin);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || image.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                }else if(password.getText().length() < 8){
                    Toast.makeText(SignUpActivity.this, "Password is too short (at least 8)", Toast.LENGTH_SHORT).show();
                }else if(! password.getText().toString().equals(passwordRepeat.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Password is not the same", Toast.LENGTH_SHORT).show();
                }else{
                    createNewAccount(email.getText().toString(), name.getText().toString(), lastName.getText().toString(), password.getText().toString(), image.getText().toString());
                }
            }
        });
    }

    private void createNewAccount(String email, String name, String lastName, String password, String image) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        User user = new User(name, lastName, email, password, image);
        Call<User> call = service.signUpUser(user);

        call.enqueue(new Callback<User>() {
            @Override

            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201){
                    Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), " Data entered is not correct!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_SHORT).show();
                Log.w("Error API: ",t.toString());
            }
        });
    }
}
