package com.example.sensorgame2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer, magneticField;
    private float[] accData = null;
    private float[] magData = null;
    private int pitch;
    private int roll;
    private OrientationChangeListener orientationChangeListener;

    OrientationSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    void setOrientationChangeListener(OrientationChangeListener orientationChangeListener) {
        this.orientationChangeListener = orientationChangeListener;
    }

    void registerListener() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_GAME);
    }

    void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] rotationMatrix = new float[9];
        float[] values = new float[3];
        int prePitch = pitch;
        int preRoll = roll;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magData = event.values.clone();
                break;
        }

        if (accData != null && magData != null) { // 들어온 값으로 기기의 방향을 계산해주는 과정
            SensorManager.getRotationMatrix(rotationMatrix, null, accData, magData);
            SensorManager.getOrientation(rotationMatrix, values);
            pitch = (int) Math.toDegrees(values[1]);
            roll = (int) Math.toDegrees(values[2]);
        }

        if (prePitch != pitch || preRoll != roll) {
            orientationChangeListener.onOrientationChanged(pitch, roll);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
