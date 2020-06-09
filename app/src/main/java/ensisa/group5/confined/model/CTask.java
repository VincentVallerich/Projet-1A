package ensisa.group5.confined.model;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */
import java.util.UUID;

<<<<<<< HEAD:app/src/main/java/ensisa/group5/confined/model/CTask.java
public class CTask implements SensorEventListener {
=======
public class CTask{
>>>>>>> taches:app/src/main/java/ensisa/group5/confined/model/Task.java
    private int id;
    private String name;
    private String description;
    public enum Priority {MINOR, IMPORTANT, URGENT};
    public enum State {NON_ATTRIBUATE, IN_PROGRESS, FINISHED};
    private Priority priority;
    private State state;

    private int points;

    public CTask(){
        this.priority = Priority.MINOR;
        this.points = 0;
    }

    public CTask(String name, String description, Priority priority, int points){
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

}