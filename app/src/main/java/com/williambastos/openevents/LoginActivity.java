package com.williambastos.openevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.williambastos.openevents.API.APIConnect;
import com.williambastos.openevents.API.OpenEventsAPI;
import com.williambastos.openevents.model.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin,passwordLogin;
    private Button signIn;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        passwordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        signIn = (Button) findViewById(R.id.buttonSignIn);
        signUp = (TextView) findViewById(R.id.buttonSignUp);

        // Trigger when user click on Sign Up Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }
        });

        // Trigger when user click on Sign In Button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailLogin.getText().toString().isEmpty() || passwordLogin.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email and your password", Toast.LENGTH_SHORT).show();
                }else{
                    login(emailLogin.getText().toString(), passwordLogin.getText().toString());
                }
            }
        });
    }


    private void login(String email, String password) {
        Retrofit retrofit = APIConnect.getRetrofitInstance();
        OpenEventsAPI service = retrofit.create(OpenEventsAPI.class);
        Login login = new Login(email,password);
        Call<Login> callLogin = service.loginUser(login);

        callLogin.enqueue(new Callback<Login>() {

            @Override
            public void onResponse(Call<Login> callLogin, Response<Login> response) {
                // If response is good, we go to NavigationActivity, else user are enter wrong
                // credentials
                if (response.code() == 200){
                    SharedPreferences sh = getSharedPreferences("sh", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("email", email);
                    editor.putString("token", response.body().getAccessToken());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Email Or Password are wrong",Toast.LENGTH_SHORT).show();
                }
            }
            // If Call fail
            @Override
            public void onFailure(Call<Login> callUserToken, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection error" ,Toast.LENGTH_LONG).show();
                Log.w("Error API: ",t.toString());
            }
        });
    }
}