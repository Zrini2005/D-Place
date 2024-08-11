package com.example.d_place;

import static java.security.AccessController.getContext;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private TextView start;
    private TextView output;
    private OkHttpClient client;
    private GridLayout outercontainer;
    private WebSocket ws;
    private EchoWebSocketListener listener;
    private int buttonsize;
    private Request request;
    private TextView text1,text2,text3,text4,text5,text6;
    private String userName;

    private int clicked=1;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {


        }


        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (TextView) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        start.setText(userName);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=1;
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=2;
            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=3;
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=4;
            }
        });
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=5;
            }
        });
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=6;
            }
        });


        request = new Request.Builder().url("ws://10.0.2.2:8000/ws/1723360801681").build();
        listener = new EchoWebSocketListener();
        outercontainer = findViewById(R.id.buttonContainer);
        outercontainer.setColumnCount(1);
        client = new OkHttpClient();
        ws = client.newWebSocket(request, listener);
        GridLayout innercontainer = new GridLayout(this);
        innercontainer.setColumnCount(10);
        outercontainer.addView(innercontainer);
        buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / 10, getResources().getDisplayMetrics().heightPixels / 10) - 30 ;
        for (int i = 0; i < 100; i++) {
            Button button = new Button(this);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick((Button) v);
                }
            });
            button.setTextColor(getColor(android.R.color.white));
            button.setTypeface(null, Typeface.BOLD);
           
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.white, null));
           
            button.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonsize;
            params.height = buttonsize;
            params.setMargins(10, 10, 10, 10);

            innercontainer.addView(button, params);
        }



    }

    private void onButtonClick(Button button) {
        if(clicked==1) {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.black, null));
        }else if(clicked==2){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.blue, null));
        }
        else if(clicked==3){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.red, null));
        }
        else if(clicked==4){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.yellow, null));
        }
        else if(clicked==5){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.green, null));
        } else{
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.white, null));

        }
        ObjectAnimator XUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
        ObjectAnimator XDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
        ObjectAnimator YUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
        ObjectAnimator YDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(XUp, YUp,XDown,YDown);
        animatorSet.setDuration(200);
        animatorSet.start();
        //listener.onMessage(ws,button.getTag().toString());
        ws.send(button.getTag().toString() + clicked);

    }
    private void onButtonClick2(Button button, int clicked) {
        if(clicked==1) {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.black, null));
        }else if(clicked==2){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.blue, null));
        }
        else if(clicked==3){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.red, null));
        }
        else if(clicked==4){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.yellow, null));
        }
        else if(clicked==5){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.green, null));
        } else{
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.white, null));

        }
        ObjectAnimator XUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
        ObjectAnimator XDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
        ObjectAnimator YUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
        ObjectAnimator YDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(XUp, YUp,XDown,YDown);
        animatorSet.setDuration(200);
        animatorSet.start();


    }


    @Nullable
    private Button getButtonAt(int row, int col) {
        int index = row * 10 + col;
        View view = findViewById(R.id.buttonContainer).findViewWithTag(index);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                //output.setText(output.getText().toString() + "\n" + txt);
                String s = output.getText().toString();
                String t= txt.trim();
                Toast.makeText(MainActivity.this, t, Toast.LENGTH_SHORT).show();
                if(t!=null && t.matches("\\d{3}")){
                    int a = Character.getNumericValue(t.charAt(0));
                    int b = Character.getNumericValue(t.charAt(1));
                    int c = Character.getNumericValue(t.charAt(2));
                    Button button = getButtonAt(a, b);
                    onButtonClick2(button,c);

                }


            }
        });
    }
}