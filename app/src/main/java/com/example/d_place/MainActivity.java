package com.example.d_place;

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

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private TextView output;
    private OkHttpClient client;
    private GridLayout outercontainer;
    private WebSocket ws;
    private EchoWebSocketListener listener;
    private int buttonsize;
    private Request request;
    private TextView text1,text2,text3;
    private int clicked=1;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }


        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
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
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
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


        request = new Request.Builder().url("http://127.0.0.1:8000/").build();
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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

    }

    private void onButtonClick(Button button) {
        if(clicked==1) {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.black, null));
        }else if(clicked==2){
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.blue, null));
        }
        else{
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.red, null));
        }
        listener.onMessage(ws,button.getTag().toString());
        ws.send(button.getTag().toString());

    }

    private void start() {



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
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}