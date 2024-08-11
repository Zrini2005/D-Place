package com.example.d_place;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class GithubLoginActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_githublogin);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().getAllowContentAccess();



        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.contains("/github-code?code=")) {

                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");

                    Intent intent = new Intent();
                    intent.putExtra("code", code);
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }
        });


        webView.loadUrl("http://10.0.2.2:8000/github-login");
    }
}