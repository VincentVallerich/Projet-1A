package ensisa.group5.confined.model;

import java.util.UUID;

public class CTask{
    private int id;
    private String name;
    private String description;
    public enum State {NON_ATTRIBUATE, IN_PROGRESS, FINISHED};
    private int priority;
    private State state;
    private int points;

    public CTask(){
        this.priority = 0;
        this.points = 0;
    }

    public CTask(String name, String description, int priority, int points){
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
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