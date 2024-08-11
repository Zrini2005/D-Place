package com.example.d_place;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginPage extends AppCompatActivity {
    private static final int GITHUB_LOGIN_REQUEST_CODE = 1;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        editText=findViewById(R.id.usernameEditText);

        Button githubLoginButton = findViewById(R.id.githubLoginButton);
        githubLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, GithubLoginActivity.class);
                startActivityForResult(intent, GITHUB_LOGIN_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GITHUB_LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra("code");
            if (code != null) {

                exchangeCodeForAccessToken(code);
            }
        }
    }

    private void exchangeCodeForAccessToken(String code) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("10.0.2.2")
                .port(8000)
                .addPathSegment("github-code")
                .addQueryParameter("code", code)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GitHub Error", "Network request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("GitHub Response", responseBody);

                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    if (!editText.getText().toString().isEmpty()) {
                        intent.putExtra("username", editText.getText().toString());
                    } else {
                        intent.putExtra("username", "XYZ - No Username");
                    }
                    startActivity(intent);
                } else {
                    Log.e("GitHub Error", "Server responded with code: " + response.code());
                }
            }
        });
    }
}