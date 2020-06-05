package ensisa.group5.confined.model;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */

public class Task {
    private int id;
    private String name;
    private String description;
    private int priority;
    private int points;
    private enum Priority {MINOR, IMPORTANT, URGENT};

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setPoints(int points) {
        this.points = points;
    }
}
