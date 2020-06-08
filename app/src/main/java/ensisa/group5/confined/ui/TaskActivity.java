package ensisa.group5.confined.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.model.TaskListItem;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private NewTaskPopup newTaskPopup;
    private NewTaskPopup modifyTaskPopup;
    private List<TaskListItem> taskListItem;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        activity = this;

        ImageButton taskButton = findViewById(R.id.task_button);
        taskButton.setOnClickListener(this);

        ImageButton leaderboardButton = findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(this);

        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(this);

        ImageButton boardButton = findViewById(R.id.board_button);
        boardButton.setOnClickListener(this);

        ImageButton calendarButton = findViewById(R.id.show_calendar_button);
        calendarButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.task_button:
                //Toast.makeText(activity, "Task!", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
                break;
            case R.id.leaderboard_button:
                //Toast.makeText(activity, "Leaderboard!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_button:
                //Toast.makeText(activity, "Profile!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.board_button:
                Intent intent = new Intent(this, BoardActivity.class);
                startActivity(intent);
                break;
            case R.id.show_calendar_button:
                CalendarPopup calendarPopup = new CalendarPopup(activity);
                calendarPopup.build();
                break;
        }
    }
}