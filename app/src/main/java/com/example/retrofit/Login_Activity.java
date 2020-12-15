package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {
    private TextInputEditText usernameTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private NetworkUtils networkUtils;
    private Gson gson;
    private Call<LoginResponse> loginApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        networkUtils = NetworkUtils.getInstance(this);
        gson = new Gson();
        usernameTextInputEditText = findViewById(R.id.edit_text_username);
        passwordTextInputEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(usernameTextInputEditText.getText().toString())){
                    usernameTextInputEditText.setError(getString(R.string.empty_username));
                }else if (TextUtils.isEmpty(passwordTextInputEditText.getText().toString())){
                    passwordTextInputEditText.setError(getString(R.string.empty_password));
                }else {
                    loginButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    loginApiCall = networkUtils.getPhotosApiInterface().login(
                            usernameTextInputEditText.getText().toString().trim(),
                            passwordTextInputEditText.getText().toString().trim()
                    );
                    loginApiCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse != null) {
                                if (loginResponse.getSuccess()) {
                                    Data.setToken(loginResponse.getToken());
                                    startActivity(new Intent(Login_Activity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), loginResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                                    loginButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }}
                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                                loginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Snackbar.make(findViewById(android.R.id.content) , t.getMessage(),Snackbar.LENGTH_LONG).show();
                        }
                    });

                    };
                }
        });

    }

    @Override
    protected void onDestroy() {
        loginApiCall.cancel();
        super.onDestroy();
    }
}