package ensisa.group5.confined.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.MainActivity;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskImgItem;
import ensisa.group5.confined.ui.model.TaskListItem;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private NewTaskPopup newTaskPopup;
    private NewTaskPopup modifyTaskPopup;
    private List<TaskListItem> taskListItem;
    private ListView taskListView;
    //private CheckBox taskSelected;
    private Context context;
    private boolean selectionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        activity = this;
        context = this.getApplicationContext();
        selectionMode = false;

        ImageButton taskButton = findViewById(R.id.task_button);
        taskButton.setOnClickListener(this);

        ImageButton leaderboardButton = findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(this);

        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(this);

        ImageButton addTaskButton = findViewById(R.id.add_task);
        addTaskButton.setOnClickListener(this);

        ImageButton delTaskButton = findViewById(R.id.del_task);
        delTaskButton.setOnClickListener(this);

        ImageButton backTaskButton = findViewById(R.id.back_task);
        backTaskButton.setOnClickListener(this);

        // item list
        // → interrogation base de données
        taskListItem = new ArrayList<>();

        // list view
        taskListView = findViewById(R.id.task_list_view);
        //taskListView.setAdapter(new TaskListAdapter(this, taskListItem));
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //change mode
                selectionMode = true;
                //change layout buttons
                findViewById(R.id.add_task).setVisibility(View.GONE);
                findViewById(R.id.del_task).setVisibility(View.VISIBLE);
                findViewById(R.id.back_task).setVisibility(View.VISIBLE);
                taskListView.setAdapter(new TaskListAdapter(context, taskListItem, true));
                return false;
            }
        });
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                TaskListItem item = taskListItem.get(i);
                //System.out.println(taskListItem.get(i));
                if (selectionMode)
                {
                    //TaskListItem item = taskListItem.get(i);
                    item.setSelected(!item.isSelected());
                    taskListView.setAdapter(new TaskListAdapter(context, taskListItem, true));
                }
                else
                {
                    modifyTaskPopup = new NewTaskPopup(activity);
                    modifyTaskPopup.setTitle(R.string.modifytask_popup_title);
                    modifyTaskPopup.setImg(item.getImg());
                    modifyTaskPopup.setName(item.getName());
                    modifyTaskPopup.setDescription(item.getDescription());
                    modifyTaskPopup.setImportance(item.getImportance());
                    modifyTaskPopup.setScore(item.getScore());
                    modifyTaskPopup.setFrequency(item.getFrequency());
                    modifyTaskPopup.setAddButtonName(getString(R.string.modifytask_popup_add_button_name));
                    modifyTaskPopup.getCancelButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modifyTaskPopup.dismiss();
                        }
                    });
                    modifyTaskPopup.getAddButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = modifyTaskPopup.getName();
                            String img = modifyTaskPopup.getImg();
                            String description = modifyTaskPopup.getDescription();
                            int importance = modifyTaskPopup.getImportance();
                            int score = modifyTaskPopup.getScore();
                            String frequency = modifyTaskPopup.getFrequency();

                            //check fields

                            //update the db

                            //add new tasks in the list
                            taskListItem.remove(item);
                            taskListItem.add(new TaskListItem(name, img, description, importance, score, frequency));
                            taskListView.setAdapter(new TaskListAdapter(context, taskListItem));
                            modifyTaskPopup.dismiss();
                        }
                    });
                    modifyTaskPopup.build();
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.task_button:
                //Toast.makeText(activity, "Task!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.leaderboard_button:
                //Toast.makeText(activity, "Leaderboard!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_button:
                //Toast.makeText(activity, "Profile!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_task:
                //Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                newTaskPopup = new NewTaskPopup(activity);
                newTaskPopup.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newTaskPopup.dismiss();
                    }
                });
                newTaskPopup.getAddButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = newTaskPopup.getName();
                        String img = newTaskPopup.getImg();
                        String description = newTaskPopup.getDescription();
                        int importance = newTaskPopup.getImportance();
                        int score = newTaskPopup.getScore();
                        String frequency = newTaskPopup.getFrequency();

                        //check fields

                        //store the new task in the bdd

                        //add new tasks in the list
                        taskListItem.add(new TaskListItem(name, img, description, importance, score, frequency));
                        taskListView.setAdapter(new TaskListAdapter(context, taskListItem));
                        newTaskPopup.dismiss();
                    }
                });
                newTaskPopup.build();
                break;
            case R.id.del_task:
                for (int i=0; i<taskListItem.size(); i++)
                    if (taskListItem.get(i).isSelected())
                    {
                        taskListItem.remove(i);
                        i--;
                    }
                disableSelectionMode();
                // + del on db
                break;
            case R.id.back_task:
                for (TaskListItem item : taskListItem)
                    item.setSelected(false);
                disableSelectionMode();
                break;
        }
    }

    private void disableSelectionMode()
    {
        selectionMode = false;
        findViewById(R.id.add_task).setVisibility(View.VISIBLE);
        findViewById(R.id.del_task).setVisibility(View.GONE);
        findViewById(R.id.back_task).setVisibility(View.GONE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, false));
    }
}

