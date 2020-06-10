package ensisa.group5.confined.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ensisa.group5.confined.R;

import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import ensisa.group5.confined.game.ScoreBordActivity;
import java.util.ArrayList;
import java.util.Locale;

import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskListItem;
import ensisa.group5.confined.controller.DataBase;
public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private List<TaskListItem> taskInProgressItem;
    private List<TaskListItem> taskDoneItem;
    private List<TaskListItem> allTaskList;
    private List<TaskListItem> taskList;
    private TaskListItem item;
    private ListView taskInProgress;
    private ListView taskDone;
    private ListView allTask;
    private SharedPreferences preferences;
    private Context context;

    private TextView titleTextView;

    private Button taskInProgressTabButton;
    private Button taskDoneTabButton;
    private Button allTaskTabButton;

    private List<Event> events = new ArrayList<Event>();

    private CalendarPopup calendarPopup;

    private String deadline;
    private DataBase dataBase;

    private ImageButton finishTask;
    private ImageButton abortTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        activity = this;
        context = activity.getApplicationContext();

        dataBase = new DataBase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        taskInProgressTabButton = (Button) findViewById(R.id.task_in_progress_tab);
        taskInProgressTabButton.setOnClickListener(this);

        taskDoneTabButton = (Button) findViewById(R.id.task_done_tab);
        taskDoneTabButton.setOnClickListener(this);

        allTaskTabButton = (Button) findViewById(R.id.all_task_tab);
        allTaskTabButton.setOnClickListener(this);

        titleTextView = findViewById(R.id.task_activity_day);
        Date todayDate = Calendar.getInstance().getTime();
        deadline = formatDate(todayDate);
        titleTextView.setText("Tâches du " + deadline);

        ImageButton calendarButton = findViewById(R.id.show_calendar_button);
        calendarButton.setOnClickListener(this);

        finishTask = findViewById(R.id.finish_task);
        finishTask.setOnClickListener(this);

        abortTask = findViewById(R.id.abort_task);
        abortTask.setOnClickListener(this);

        taskInProgressItem = new ArrayList<>();
        taskInProgress = findViewById(R.id.task_in_progress_list_view);
        taskInProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskListItem item = taskInProgressItem.get(i);
                item.setSelected(!item.isSelected());
                taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem, false, true));
                if (isItemSelected()) {
                    findViewById(R.id.finish_task).setVisibility(View.VISIBLE);
                    findViewById(R.id.abort_task).setVisibility(View.VISIBLE);
                }
                else
                {
                    findViewById(R.id.finish_task).setVisibility(View.GONE);
                    findViewById(R.id.abort_task).setVisibility(View.GONE);
                }
            }
        });

        taskDoneItem = new ArrayList<>();
        taskDone = findViewById(R.id.task_done_list_view);

        taskList = new ArrayList<>();

        allTaskList = new ArrayList<>();
        allTask = findViewById(R.id.all_task_list_view);

        preferences = getPreferences(MODE_PRIVATE);
        dataBase = new DataBase(this, preferences);
        // les threads rempliront la page lorsque les informations seront récupérées depuis la base de données.
        try {
            Thread t1 = new Thread(new Runnable() { @Override public void run() { createUserTasksDisplay(); }  });
            t1.start();
            Thread t4 = new Thread(new Runnable() {  @Override public void run() {  dataBase.finishTask("5edb9d925f4b418aee1abdf7");  } });
            t4.start();
            Thread t5 = new Thread(new Runnable() {  @Override public void run() {  dataBase.startTask("5edb9d925f4b418aee1abdf7");  } });
            t5.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUserTasksDisplay() {

        List<Document> docs = new ArrayList<Document>();
        dataBase.getTasksByUser()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        String name = obj.getString("task_name").toString();
                        String img = obj.getString("task_name").toString();
                        String description = obj.getString("task_desc").toString();
                        int importance = (int)Integer.parseInt(obj.getString("task_priority"));
                        int score = (int)Integer.parseInt( obj.getString("task_score"));
                        String frequency = obj.getString("task_priority").toString();
                        String status = obj.getString("task_status").toString();

                        String strDate = obj.getString("task_limit_date").toString();
                        strDate = strDate.replace("{\"$date\":","").replace("}","");
                        long date = (long)Long.parseLong(strDate);
                        String deadline = formatDate(new Date(date));

                        String id = d.getObjectId("_id").toString();

                        TaskListItem t = new TaskListItem(name,img,description,importance,score,frequency,deadline,status,id);
                        taskList.add(t);
                    }
                    createEvents();
                    displayTaskForCurrentDay();
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
                //   Intent intent4 = new Intent(this, ProfilActivity.class);
                // startActivity(intent4);
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
                calendarPopup.getCalendar().addEvents(events);
                calendarPopup.getMonthLabel().setText(formatDateMonth(calendarPopup.getCalendar().getFirstDayOfCurrentMonth()));
                calendarPopup.getCalendar().setListener(new CompactCalendarView.CompactCalendarViewListener() {
                    @Override
                    public void onDayClick(Date dateClicked) {
                        deadline = formatDate(dateClicked);
                        titleTextView.setText("Tâches du " + deadline);
                        displayTaskForCurrentDay();
                        taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
                        taskDone.setAdapter(new TaskListAdapter(context, taskDoneItem));
                        calendarPopup.dismiss();
                    }

                    @Override
                    public void onMonthScroll(Date firstDayOfNewMonth) {
                        calendarPopup.getMonthLabel().setText(formatDateMonth(calendarPopup.getCalendar().getFirstDayOfCurrentMonth()));
                    }
                });

                calendarPopup.build();
                break;
            case R.id.task_in_progress_tab:
                taskDoneTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                allTaskTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                taskInProgressTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button_clicked));
                findViewById(R.id.task_in_progress_list_view).setVisibility(View.VISIBLE);
                findViewById(R.id.task_done_list_view).setVisibility(View.GONE);
                findViewById(R.id.all_task_list_view).setVisibility(View.GONE);
                break;
            case R.id.task_done_tab:
                taskInProgressTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                allTaskTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                taskDoneTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button_clicked));
                findViewById(R.id.task_in_progress_list_view).setVisibility(View.GONE);
                findViewById(R.id.task_done_list_view).setVisibility(View.VISIBLE);
                findViewById(R.id.all_task_list_view).setVisibility(View.GONE);
                //check if tasks are selected with a bool var before turnOffSelection ?
                turnOffSelection();
                break;
            case R.id.all_task_tab:
                taskInProgressTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                taskDoneTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button));
                allTaskTabButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.tab_button_clicked));
                findViewById(R.id.task_in_progress_list_view).setVisibility(View.GONE);
                findViewById(R.id.task_done_list_view).setVisibility(View.GONE);
                findViewById(R.id.all_task_list_view).setVisibility(View.VISIBLE);
                turnOffSelection();
                break;
            case R.id.finish_task:
                findViewById(R.id.finish_task).setVisibility(View.GONE);
                findViewById(R.id.abort_task).setVisibility(View.GONE);
                for (int i=0; i<taskInProgressItem.size(); i++)
                    if (taskInProgressItem.get(i).isSelected())
                    {
                        item = taskInProgressItem.get(i);
                        try {
                            Thread t1 = new Thread(new Runnable() { @Override public void run() { dataBase.finishTask(item.getId()); }  });
                            t1.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        taskDoneItem.add(taskInProgressItem.get(i));
                        taskInProgressItem.remove(i);
                        i--;
                    }
                taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
                break;
            case R.id.abort_task:
                findViewById(R.id.finish_task).setVisibility(View.GONE);
                findViewById(R.id.abort_task).setVisibility(View.GONE);

                for (int i=0; i<taskInProgressItem.size(); i++)
                    if (taskInProgressItem.get(i).isSelected())
                    {
                        item = taskInProgressItem.get(i);
                        try {
                            Thread t1 = new Thread(new Runnable() { @Override public void run() { dataBase.abandonTask(item.getId()); }  });
                            t1.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        taskInProgressItem.remove(i);
                        i--;
                    }
                taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
                break;
        }
    }

    public void displayTaskForCurrentDay()
    {
        taskInProgressItem = new ArrayList<>();
        taskDoneItem = new ArrayList<>();
        allTaskList = new ArrayList<>();
        for (TaskListItem item : taskList) {
            if (item.getStatus().equals("IN_PROGRESS"))
                allTaskList.add(item);
            if (item.getDeadline().equals(deadline) && item.getStatus().equals("IN_PROGRESS"))
                taskInProgressItem.add(item);
            else if (item.getStatus().equals("FINISHED"))
                taskDoneItem.add(item);
        }
        taskInProgress.setAdapter(new TaskListAdapter(context, taskInProgressItem));
        taskDone.setAdapter(new TaskListAdapter(context, taskDoneItem));
        allTask.setAdapter(new TaskListAdapter(context, allTaskList));
    }

    public String formatDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public String formatDateMonth(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public Date formatDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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

    private void createEvents()
    {
        for (TaskListItem item : taskList)
            if (item.getStatus().equals("IN_PROGRESS"))
                events.add(new Event(Color.YELLOW, formatDate(item.getDeadline()).getTime(), item.getName()));
    }
}