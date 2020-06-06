package ensisa.group5.confined.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.MainActivity;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskListItem;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private NewTaskPopup newTaskPopup;
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

        ImageButton addTaskButton = findViewById(R.id.add_task);
        addTaskButton.setOnClickListener(this);

        // item list
        // → interrogation base de données
        taskListItem = new ArrayList<>();

        // list view
        taskListView = findViewById(R.id.task_list_view);
        taskListView.setAdapter(new TaskListAdapter(this, taskListItem));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.task_button:
                Toast.makeText(activity, "Task!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.leaderboard_button:
                Toast.makeText(activity, "Leaderboard!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_button:
                Toast.makeText(activity, "Profile!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_task:
                Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                newTaskPopup = new NewTaskPopup(activity);
                newTaskPopup.getCancelButton().setOnClickListener(this);
                newTaskPopup.getAddButton().setOnClickListener(this);
                newTaskPopup.build();
                break;
            case R.id.newtask_popup_template_cancel_btn:
                newTaskPopup.dismiss();
                break;
            case R.id.newtask_popup_template_addtask_btn:
                //check empty fields
                String name = newTaskPopup.getName();
                String img = newTaskPopup.getImg();
                String description = newTaskPopup.getDescription();
                int importance = newTaskPopup.getImportance();
                int score = newTaskPopup.getScore();
                String frequency = newTaskPopup.getFrequency();
                //int frequency = newTaskPopup.getFrequency();

                //store the new task in the bdd

                //add new tasks in the list
                taskListItem.add(new TaskListItem(name, img, description, importance, score, frequency));
                taskListView.setAdapter(new TaskListAdapter(this, taskListItem));
                newTaskPopup.dismiss();
                break;
        }
    }
}

