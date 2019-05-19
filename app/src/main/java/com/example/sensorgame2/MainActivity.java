package com.example.sensorgame2;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity implements OrientationChangeListener, GameResultListener {
    OrientationSensor orientationSensor;
    SensorGameView sensorGameView;
    LinearLayout container;
    TextView count;
    CountDownTimer countDownTimer;
    Handler handler;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = findViewById(R.id.count);
        container = findViewById(R.id.container);

        handler = new Handler();

        sensorGameView = new SensorGameView(this);

        orientationSensor = new OrientationSensor(this);
        orientationSensor.registerOrientationChangeListener(this);
        sensorGameView.setGameResultListener(this);

        container.addView(sensorGameView);

        countDownTimer = new CountDownTimer(30000, 1000) {
            int secondsUntilFinished;

            @Override
            public void onTick(long millisUntilFinished) {
                secondsUntilFinished = (int) (millisUntilFinished / 1000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!gameOver)
                            count.setText(String.valueOf(secondsUntilFinished));
                        else {
                            terminateGame(secondsUntilFinished);
                            cancel();
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        container.removeAllViews();
                        count.setText("병을 지켰습니다!");
                    }
                });
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationSensor.registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationSensor.unregisterListeners();
    }

    @Override
    public void onOrientationChanged(double pitch, double roll) {
        sensorGameView.updateDst(pitch, roll);
    }

    @Override
    public void onGameOver() {
        gameOver = true;
        container.removeAllViews();
    }

    public void terminateGame(int secondsUntilFinished) {
        count.setText(MessageFormat.format("병이 깨졌습니다. 남은 시간 : {0}초", secondsUntilFinished));
    }
}
