package ensisa.group5.confined.model;

<<<<<<< HEAD
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */
=======
import java.util.UUID;
>>>>>>> 3a6a8cf233ff0632a54d94d2336003832ca7881a

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
    private Priority priority;
    private int points;

    public Task(){
        this.priority = Priority.MINOR;
        this.points = 0;
    }

    public Task(String name, String description,Priority priority,int points){
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}