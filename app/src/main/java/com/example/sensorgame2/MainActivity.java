package com.example.sensorgame2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements OrientationChangeListener {
    OrientationSensor orientationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orientationSensor = new OrientationSensor(this);
        orientationSensor.setOrientationChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationSensor.registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationSensor.unregisterListener();
    }

    @Override
    public void onOrientationChanged(int pitch, int roll) {
        Log.d("MyLog", "MainActivity" + pitch + ", " + roll);
    }
}
