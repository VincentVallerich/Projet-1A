package ensisa.group5.confined.ui.model;

/**
 * Replace by CTask
 */

public class TaskListItem
{
    private String name;
    private String img;
    private String description;
    private int importance;
    private int score;
    private String frequency;
    private String deadline;
    private boolean selected = false;
    private boolean done;

    public TaskListItem(String name, String img, String description, int importance, int score, String frequency, String deadline, boolean done)
    {
        this.name = name;
        this.img = img;
        this.description = description;
        this.importance = importance;
        this.score = score;
        this.frequency = frequency;
        this.deadline = deadline;
        this.done = done;
    }

    public String getName()
    {
        return name;
    }

    public String getImg()
    {
        return img;
    }

    public String getDescription()
    {
        return description;
    }

    public int getImportance()
    {
        return importance;
    }

    public int getScore()
    {
        return score;
    }

    public String getFrequency()
    {
        return frequency;
    }

    public String getDeadline()
    {
        return deadline;
    }

    public boolean isDone()
    {
        return done;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
