package ensisa.group5.confined.ui.model;

public class TaskListItem
{
    private String name;
    private String img;
    private String description;
    private int importance;
    private int score;
    private String frequency;
    private boolean selected = false;

    public TaskListItem(String name, String img, String description, int importance, int score, String frequency)
    {
        this.name = name;
        this.img = img;
        this.description = description;
        this.importance = importance;
        this.score = score;
        this.frequency = frequency;
    }

    public TaskListItem(String name, String img, String description, int importance, int score)
    {
        this(name, img, description, importance, score, "");
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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
