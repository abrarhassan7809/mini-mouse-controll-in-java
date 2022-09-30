package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class MainActivity extends AppCompatActivity{

    static HttpRequest request = new HttpRequest();

    private static final String TAG = "swipe position";
    private static float x;
    private static float y;
    private static float disX;
    private static float disY;
    private static float initialX, initialY, initial_X, initial_Y;
    private GestureDetector mDetector;
    private RelativeLayout myLayout = null;

//  scrolling_events------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Document Scroll");

        Button btn_keyboard = findViewById(R.id.keyboard_button);
        Button btn_mouse = findViewById(R.id.mouse_button);

        btn_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(MainActivity.this, Keyboard.class);
                startActivity(intent_1);
            }

        });
        btn_mouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_2 = new Intent(MainActivity.this, Mouse.class);
                startActivity(intent_2);
            }

        });
        mDetector = new GestureDetector(this, new MyGestureListener());

        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
        myLayout.setOnTouchListener(touchListener);

//        myLayout.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                x1 = event.getX();
//                y1 = event.getY();
//                System.out.println("value_of x is "+x1+" value_of y is "+y1);
//                getKeysResponse(x1, y1);
//                return true;
//            }
//        });
    }

//    public boolean onTouchEvent(MotionEvent event) {
//
//        int action = event.getActionMasked();
//
//        switch (action) {
//
//            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG, "Mouse Action was start");
////                initial_X = event.getX();
////                initial_Y = event.getY();
////                getKeysResponse(initial_X, initial_Y);
//                break;
//
//            case MotionEvent.ACTION_UP:
//
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                initial_X = event.getX();
//                initial_Y = event.getY();
//
//                break;
//
//            case MotionEvent.ACTION_POINTER_UP:
//                float final_X = event.getX();
//                float final_Y = event.getY();
//
//                Log.d(TAG, "Action was UP");
//
////                if (initialX < finalX) {
////                    Log.d(TAG, "Left to Right swipe performed");
////                    float y1 = 0;
////                    float x1 = (initialX - finalX)/25;
////                    Log.d(TAG, "Left to Right swipe performed ");
////                    getKeysResponse(x1, y1);
////                }
////
////                else if (initialX > finalX) {
////                    Log.d(TAG, "Right to Left swipe performed");
////                    float y1 = 0;
////                    float x1 = (initialX - finalX)/25;
////                    Log.d(TAG, "Left to Right swipe performed ");
////                    getKeysResponse(x1, y1);
////                }
//
//                if (initial_Y < final_Y) {
//                    System.out.println("Up to Down swipe performed");
//                    float x1 = 0;
//                    float y1 = (initial_Y - final_Y)/25;
//                    System.out.println("first x "+initial_X+" final x "+x1+" first y "+initial_Y+" final y "+y1);
//                    getKeysResponse(x1, y1);
//
//                }
//
//                else if (initial_Y > final_Y) {
//                    System.out.println("Down to Up swipe performed");
//                    float x1 = 0;
//                    float y1 = (initial_Y - final_Y)/25;
//                    System.out.println("first x "+initial_X+" final x "+x1+" first y "+initial_Y+" final y "+y1);
//                    getKeysResponse(x1, y1);
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "Action was MOVE");
//
//                break;
//
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG,"Action was CANCEL");
//                break;
//
//            case MotionEvent.ACTION_OUTSIDE:
//                Log.d(TAG, "Movement occurred outside bounds of current screen element");
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

//  ---------------------------------

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return mDetector.onTouchEvent(event);

        }
    };

    static class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");

            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            disX = (e1.getX() - e2.getX())/100;
            disY = (e1.getY() - e2.getY())/100;
            Log.e("TAG", "onScroll: "+disX+ " y " +disY);
            getKeysResponse(disX, disY);

//            disX = distanceX/10;
//            disY = distanceY/10;
//            x = 0;
//            y = 0;
//            Log.e("TAG", "onScroll: "+x + " y " +disY);
////            MainActivity.getKeysResponse(x, disY);
//            if (e2.getY() > e1.getY()) {
//                MainActivity.getValueResponse(x, disY);
//                // direction up
//            }else {
//                MainActivity.getValueResponse(x, disY);
//                // direction down
//            }
//
//            if (e2.getX() > e1.getX()) {
//                MainActivity.getValueResponse(disX, y);
//                // direction right
//            }else {
//                MainActivity.getValueResponse(disX, y);
//                // direction left
//            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return false;
        }
    }

    //  ---------------------------------

    public static void getKeysResponse ( float x, float y){
        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {
                System.out.println("Response Success " +x+ " " +y);
            } else {
                System.out.println("Error");
            }
        });
        request.setOnErrorListener(error -> {

            System.out.println("Error Found" + error);
        });
        request.get("http://192.168.1.32:8000/start-scroll/" +x+ "/" +y);
    }
}