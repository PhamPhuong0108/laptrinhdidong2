package com.example.admin.myapplication1;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import vn.edu.tdc.myapplication1.R;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager sensorManager;
    private boolean isColor = false;
    private View view;
    private long lastUpdate;private ImageView img;
    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;
    private TextView lblPosY, lblPosX;
    private ImageView imgView;
    private RelativeLayout viewRoot;
    private int count = 0;
    float lastXAxis = 0f;
    float lastYAxis = 0f;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        img = (ImageView) rootLayout.findViewById(R.id.imageView);
        imgView = (ImageView) findViewById(R.id.imageView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        img.setLayoutParams(layoutParams);
        img.setOnTouchListener(new ChoiceTouchListener());
        lblPosY = (TextView) findViewById(R.id.lblPositionY);
        lblPosX = (TextView) findViewById(R.id.lblPositionX);
        view = findViewById(R.id.view_root);
        view.setBackgroundResource(R.drawable.balkajdsj);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }
    //overriding two methods of SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
// Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long actualTime = System.currentTimeMillis();
//        Toast.makeText(getApplicationContext(),String.valueOf(accelationSquareRoot)+" "+
//                SensorManager.GRAVITY_EARTH,Toast.LENGTH_SHORT).show();

        if (accelationSquareRoot >= 2) //it will be executed if you shuffle
        {

            if (actualTime - lastUpdate < 200) {
                return;
            }

            lastUpdate = actualTime;//updating lastUpdate for next shuffle
            if (isColor && count == 1) {
                view.setBackgroundResource(R.drawable.balkajdsj);
                imgView.setImageResource(R.drawable.guyfawkes);
                count++;
            } else if(isColor && count == 0) {
                view.setBackgroundResource(R.drawable.blueflowers);
                imgView.setImageResource(R.drawable.smile);
                count++;
            }
            else if(isColor && count == 2) {
                view.setBackgroundResource(R.drawable.imageas);
                imgView.setImageResource(R.drawable.apple);
                count = 0;
            }
            isColor = !isColor;
        }
    }

    private final class ChoiceTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    final float x = event.getX();
                    final float y = event.getY();

                    lastXAxis = x;
                    lastYAxis = y;

                    Toast.makeText(getApplicationContext(),Float.toString(lastXAxis),Toast.LENGTH_SHORT).show();
                break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    lblPosY.setText("Y=" + layoutParams.topMargin);
                    lblPosX.setText("X=" + layoutParams.leftMargin);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
// register this class as a listener for the orientation and
// accelerometer sensors
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
// unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
