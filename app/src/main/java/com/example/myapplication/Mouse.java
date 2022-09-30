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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class Mouse extends AppCompatActivity {

    static HttpRequest request = new HttpRequest();

    private static final String TAG = "swipe position";
    private static float disX, mLastTouchX, mPosX;
    private static float disY, mLastTouchY, mPosY;
    private static float initialX, initialY, initial_X, initial_Y;
    private GestureDetector gestureDetector;
    private RelativeLayout mouseLayout = null;
    private View.OnTouchListener touchListener;
    private int mActivePointerId;

//  scrolling_events------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        this.setTitle("Move Mouse Cursor");

        Button btn_back = findViewById(R.id.mouse_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mouse.this, MainActivity.class);
                startActivity(intent);
            }

        });
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener());

        mouseLayout = (RelativeLayout) findViewById(R.id.mouseLayout);
        mouseLayout.setOnTouchListener(touchListener);

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

    public boolean onTouchEvent(MotionEvent ev) {

        gestureDetector.onTouchEvent(ev);

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {

            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                Log.e("TAG", "down event mLastTouchX : " + mLastTouchX + " mLastTouchY " + mLastTouchY);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                Log.e("TAG", "move event mLastTouchX : "+dx+" , "+x+" , "+dy+ " , "+y);

                mPosX += dx;
                mPosY += dy;
                Log.e("TAG", "move event mPosX : " + mPosX + " mPosY " + mPosY);
                mouseLayout.invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;
                Log.e("TAG", "move event xxx : " + mLastTouchX + " yyy " + mLastTouchY);
                getValuesResponse(dx, dy);

                break;
            }

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }

        }
        return true;
    }

//  ---------------------------------

//    View.OnTouchListener touchListener = new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            // pass the events to the gesture detector
//            // a return value of true means the detector is handling it
//            // a return value of false means the detector didn't
//            // recognize the event
//            return gestureDetector.onTouchEvent(event);
//
//        }
//    };
//
//    static class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onDown(MotionEvent event) {
//            Log.d("TAG","onDown: ");
//
//            // don't return false here or else none of the other
//            // gestures will work
//            return true;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Log.i("TAG", "onSingleTapConfirmed: ");
//            return false;
//        }
//
//        @Override
//        public void onLongPress(MotionEvent e) {
//            Log.i("TAG", "onLongPress: ");
//        }
//
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            Log.i("TAG", "onDoubleTap: ");
//            return false;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            disX = (e2.getX() - e1.getX())/20;
//            disY = (e2.getY() - e1.getY())/20;
//            Log.e("TAG", "onMove: "+disX+ " y " +disY);
//
//
////            disX = distanceX/10;
////            disY = distanceY/10;
////            x = 0;
////            y = 0;
////            Log.e("TAG", "onScroll: "+x + " y " +disY);
//////            MainActivity.getKeysResponse(x, disY);
////            if (e2.getY() > e1.getY()) {
////                MainActivity.getValueResponse(x, disY);
////                // direction up
////            }else {
////                MainActivity.getValueResponse(x, disY);
////                // direction down
////            }
////
////            if (e2.getX() > e1.getX()) {
////                MainActivity.getValueResponse(disX, y);
////                // direction right
////            }else {
////                MainActivity.getValueResponse(disX, y);
////                // direction left
////            }
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent event1, MotionEvent event2,
//                               float velocityX, float velocityY) {
//            Log.d("TAG", "onFling: ");
//            return false;
//        }
//    }
//
//    //  ---------------------------------
//
    public static void getValuesResponse ( float x, float y){
        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {
                System.out.println("Response Success " +x+ " " +y);
            } else {
                System.out.println("Error");
            }
        });
        request.setOnErrorListener(error -> {
            // There was an error, deal with it
            System.out.println("Error Found" + error);
        });
        request.get("http://192.168.1.32:8000/mouse-move/" +x+ "/" +y);
    }
}
