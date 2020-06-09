package ensisa.group5.confined.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ensisa.group5.confined.R;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ensisa.group5.confined.game.ScoreBordActivity;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskListItem;
import ensisa.group5.confined.controller.DataBase;
public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private NewTaskPopup newTaskPopup;
    private NewTaskPopup modifyTaskPopup;
    private List<TaskListItem> taskInProgressItem;
    private List<TaskListItem> taskDoneItem;
    private List<TaskListItem> taskList;
    private ListView taskInProgress;
    private ListView taskDone;
    private SharedPreferences preferences;
    private Context context;

    private TextView titleTextView;
    private ImageButton finishTask;

    private Button taskInProgressTabButton;
    private Button taskDoneTabButton;

    private CalendarPopup calendarPopup;

    private String deadline;

    private DataBase database;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        database = new DataBase();
        setContentView(R.layout.task_activity);
        activity = this;
        context = activity.getApplicationContext();

        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        taskInProgressTabButton = (Button) findViewById(R.id.task_in_progress_tab);
        taskInProgressTabButton.setOnClickListener(this);

        taskDoneTabButton = (Button) findViewById(R.id.task_done_tab);
        taskDoneTabButton.setOnClickListener(this);

        titleTextView = findViewById(R.id.task_activity_day);
        Date todayDate = Calendar.getInstance().getTime();
        deadline = formatDate(todayDate);
        titleTextView.setText("Tâches du " + deadline);

        ImageButton calendarButton = findViewById(R.id.show_calendar_button);
        calendarButton.setOnClickListener(this);

        finishTask = findViewById(R.id.finish_task);
        finishTask.setOnClickListener(this);

        taskList = new ArrayList<>();

        taskInProgressItem = new ArrayList<>();
        taskInProgress = findViewById(R.id.task_in_progress_list_view);
        taskInProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskListItem item = taskInProgressItem.get(i);
                item.setSelected(!item.isSelected());
                taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem, false, true));
                if (isItemSelected())
                    findViewById(R.id.finish_task).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.finish_task).setVisibility(View.GONE);
            }
        });

        taskDoneItem = new ArrayList<>();
        taskDone = findViewById(R.id.task_done_list_view);

        taskList.add(new TaskListItem("Faire la cuisine", "taskicon_cleaner_1", "", 2, 1, "0", "09-06-2020", false));
        taskList.add(new TaskListItem("Faire la cuisine", "taskicon_cleaning_14", "", 2, 1, "0", "10-06-2020", false));
        taskList.add(new TaskListItem("Faire la cuisine", "taskicon_cleaner_2", "", 2, 1, "0", "09-06-2020", true));
        taskList.add(new TaskListItem("Faire la cuisine", "taskicon_cleaner_1", "", 2, 1, "0", "09-06-2020", false));
        taskList.add(new TaskListItem("Faire la cuisine", "taskicon_cleaning", "", 2, 1, "0", "09-06-2020", true));

        /*taskInProgress.setAdapter(new TaskListAdapter(this, taskInProgressItem));
        taskDone.setAdapter(new TaskListAdapter(this, taskDoneItem));*/

        displayTaskForCurrentDay();

        /*
        preferences = getPreferences(MODE_PRIVATE);
        loginValidation = new LoginValidation(this, preferences);
        // les threads rempliront la page lorsque les informations seront récupérées depuis la base de données.
        try {
            Thread t1 = new Thread(new Runnable() { @Override public void run() { createUserTasksDisplay(); }  });
            t1.start();
            Thread t2 = new Thread(new Runnable() {  @Override public void run() {  createLeaderboard();  } });
            // t2.start();
            Thread t3 = new Thread(new Runnable() {  @Override public void run() {  createUnassignedTaskDisplay();  } });
            //t3.start();
            Thread t4 = new Thread(new Runnable() {  @Override public void run() {  loginValidation.finishTask("5edb9d925f4b418aee1abdf7");  } });
            t4.start();
            Thread t5 = new Thread(new Runnable() {  @Override public void run() {  loginValidation.startTask("5edb9d925f4b418aee1abdf7");  } });
            t5.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
    /*
    public void createLeaderboard( ){
        List<Document> docs = new ArrayList<Document>();
        loginValidation.getLeaderBoard()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        Log.d("stitch", obj.toString());
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void createUnassignedTaskDisplay() {
        List<Document> docs = new ArrayList<Document>();
        loginValidation.getNonAssignedTasks()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        // ici je récupère directement les infos du JSON,
                        // Il faudrait peut etre transformer dans un premier temps le json en un object Tâche concret
                        // Ensuite ajouter la tâche en tant que ListItem;
                        JSONObject obj = new JSONObject(d.toJson());
                        String name = obj.getString("task_name").toString();
                        String img = obj.getString("task_name").toString();
                        String description = obj.getString("task_desc").toString();
                        int importance = (int) Integer.parseInt(obj.getString("task_status"));
                        int score = (int)Integer.parseInt( obj.getString("task_priority"));
                        String frequency = obj.getString("task_name").toString();
                        TaskListItem t = new TaskListItem(name,img,description,importance,score,frequency,"");
                        taskListItem.add(t);
                    }
                    taskListView.setAdapter(new TaskListAdapter(context, taskListItem));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

     */
    public void createUserTasksDisplay() {

        List<Document> docs = new ArrayList<Document>();
        database.getTasksByUser()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        String name = obj.getString("task_name").toString();
                        String img = "img_random";
                        String description = obj.getString("task_desc").toString();
                        int importance = (int) Integer.parseInt(obj.getString("task_priority"));
                        int score = (int)Integer.parseInt( obj.getString("task_score"));
                        String frequency = " 0";
                        TaskListItem t = new TaskListItem(name,img,description,importance,score,frequency,"",false);
                     //   taskListItem.add(t);
                       // taskListView.setAdapter(new TaskListAdapter(context, taskListItem));

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean onClickNavigationBar(Integer integer ){
        Log.d("stitch","going in onclick" + integer);
        switch (integer) {

            case R.id.action_board:
                // go to activity
                Intent intent = new Intent(this, BoardActivity.class);
                startActivity(intent);
                break;
            case R.id.action_leaderboard:
                Intent intent2 = new Intent(this, ScoreBordActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_mytasks:
                Intent intent3 = new Intent(this, TaskActivity.class);
                startActivity(intent3);
                break;
            case R.id.action_profile:
                Intent intent4 = new Intent(this, ProfileActivity.class);
                startActivity(intent4);
                break;
        }
        return false;

    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.show_calendar_button:
                calendarPopup = new CalendarPopup(activity);
                calendarPopup.getCalendar().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, i);
                        c.set(Calendar.MONTH, i1);
                        c.set(Calendar.DAY_OF_MONTH, i2);
                        deadline = formatDate(c.getTime());
                        titleTextView.setText("Tâches du " + deadline);
                        displayTaskForCurrentDay();
                        taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
                        taskDone.setAdapter(new TaskListAdapter(context, taskDoneItem));
                        calendarPopup.dismiss();
                    }
                });
                calendarPopup.build();
                break;
            case R.id.task_in_progress_tab:
                taskDoneTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                taskInProgressTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button_clicked));
                findViewById(R.id.task_in_progress_list_view).setVisibility(View.VISIBLE);
                findViewById(R.id.task_done_list_view).setVisibility(View.GONE);
                break;
            case R.id.task_done_tab:
                taskInProgressTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                taskDoneTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button_clicked));
                findViewById(R.id.task_in_progress_list_view).setVisibility(View.GONE);
                findViewById(R.id.task_done_list_view).setVisibility(View.VISIBLE);
                //check if tasks are selected with a bool var before turnOffSelection ?
                turnOffSelection();
                break;
            case R.id.finish_task:
                findViewById(R.id.finish_task).setVisibility(View.GONE);
                deleteSelection();
                taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
                break;
        }
    }

    public void displayTaskForCurrentDay()
    {
        taskInProgressItem = new ArrayList<>();
        taskDoneItem = new ArrayList<>();
        for (TaskListItem item : taskList)
            if (item.getDeadline().equals(deadline) && !item.isDone())
                taskInProgressItem.add(item);
            else if (item.getDeadline().equals(deadline) && item.isDone())
                taskDoneItem.add(item);
        taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
        taskDone.setAdapter(new TaskListAdapter(context, taskDoneItem));
    }

    public String formatDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    private void deleteSelection()
    {
        for (int i=0; i<taskInProgressItem.size(); i++)
            if (taskInProgressItem.get(i).isSelected())
            {
                taskInProgressItem.remove(i);
                i--;
            }
    }

    private boolean isItemSelected()
    {
        for (TaskListItem item : taskInProgressItem)
            if (item.isSelected())
                return true;
        return false;
    }

    private void turnOffSelection()
    {
        findViewById(R.id.finish_task).setVisibility(View.GONE);
        for (TaskListItem item : taskInProgressItem)
            item.setSelected(false);
        taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
    }
}