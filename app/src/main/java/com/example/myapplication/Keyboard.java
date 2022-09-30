package com.example.myapplication;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class Keyboard extends AppCompatActivity {

    static HttpRequest request = new HttpRequest();
    private GestureDetector gestureDetector;

//    --------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        this.setTitle("Keyboard Keys");

        Button btn_up = findViewById(R.id.button_up);
        Button btn_left = findViewById(R.id.button_left);
        Button btn_right = findViewById(R.id.button_right);
        Button btn_down = findViewById(R.id.button_down);

        Button btn_back = findViewById(R.id.keyboard_back);
        Button btn_shutdown = findViewById(R.id.shutdown);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Keyboard.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btn_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getKeysResponse("up");
            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKeysResponse("left");
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKeysResponse("right");
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKeysResponse("down");
            }
        });
        btn_shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKeysResponse("cmd");
            }
        });
    }

//    -------------------------------------

    public static void getKeysResponse(String keyType) {
        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {
                System.out.println("Response Success");
            } else {
                System.out.println("Error");
            }
        });
        request.setOnErrorListener(new HttpRequest.OnErrorListener() {
            @Override
            public void onError(HttpError error) {
                // There was an error, deal with it
                System.out.println("Error Found"+error);
            }
        });
        request.get("http://192.168.1.32:8000/get-key/"+keyType);
    }
}
