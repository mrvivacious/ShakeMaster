package us.mrvivacio.shakemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private Sensor accelerometer;

    float x, y, z;
    float prevX = 0, prevY = 0, prevZ = 0;

    long prevTime = 0;

    MediaPlayer mediaPlayer;


    private void initSensors() {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null) {
            Toast.makeText(this, "RIP no accelerometer", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ouch);

        initSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Thank you,
        // https://stackoverflow.com/questions/5271448/how-to-detect-shake-event-with-android
        // Accelerometer changed, detect shake
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        long currTime = System.currentTimeMillis();

        if (currTime - prevTime > 1000) {
            float speed = Math.abs(x + y + z - prevX - prevY - prevZ) * 10000;
            Log.d("$$$$$", "" + speed);

            if (speed > 30000) {
//                Toast.makeText(this, "Shaken", Toast.LENGTH_LONG).show();
                mediaPlayer.start();
            }

            prevX = x;
            prevY = y;
            prevZ = z;
            prevTime = currTime;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
