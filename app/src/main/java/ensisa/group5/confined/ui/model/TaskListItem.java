package ensisa.group5.confined.ui.model;

public class TaskListItem
{
    private String name;
    private String img;
    private final static String DEFAULT_IMG="taskicon_cleaning";
    private String description;
    private int importance;
    private int score;
    private String deadline;
    private boolean selected = false;
    private String status;
    private String id;

    public TaskListItem(String name, String img, String description, int importance, int score, String deadline, String status, String id)
    {
        this.name = name;
        this.img = img;
        this.description = description;
        this.importance = importance;
        this.score = score;
        this.deadline = deadline;
        this.status = status;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getImg()
    {
        if (img == null)
            return DEFAULT_IMG;
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

    public String getDeadline()
    {
        return deadline;
    }

    public String getStatus()
    {
        return status;
    }

    public String getId()
    {
        return id;
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
