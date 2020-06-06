package ensisa.group5.confined.model;

import java.util.UUID;

public class Task {
    private int id;
    private String name;
    private String description;
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