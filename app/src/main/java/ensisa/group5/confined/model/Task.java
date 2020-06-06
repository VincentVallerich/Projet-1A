package ensisa.group5.confined.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */

public class Task implements SensorEventListener {
    private int id;
    private String name;
    private String description;

    TextView tv_steps;
    SensorManager sensorManager;

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private enum Priority {MINOR, IMPORTANT, URGENT};
    private int points;


}
