package ensisa.group5.confined.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.LoginValidation;
import ensisa.group5.confined.controller.MainActivity;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskListItem;

import static com.mongodb.client.model.Filters.eq;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;
    private NewTaskPopup newTaskPopup;
    private List<TaskListItem> taskListItem;
    private ListView taskListView;
    private SharedPreferences preferences;
    private LoginValidation loginValidation;
    private TaskActivity context;

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
        context = this;
        // list view
        taskListView = findViewById(R.id.task_list_view);
        //taskListView.setAdapter(new TaskListAdapter(this, taskListItem));
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



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
                        TaskListItem t = new TaskListItem(name,img,description,importance,score,frequency);
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
    public void createUserTasksDisplay() {

        List<Document> docs = new ArrayList<Document>();
        loginValidation.getTasksByUser()
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
                        TaskListItem t = new TaskListItem(name,img,description,importance,score,frequency);
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

