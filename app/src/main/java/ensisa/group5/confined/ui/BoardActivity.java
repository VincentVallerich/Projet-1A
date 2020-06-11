package ensisa.group5.confined.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.game.ScoreBordActivity;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;
import ensisa.group5.confined.ui.model.TaskListItem;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener
{
    private BoardActivity activity;
    private NewTaskPopup newTaskPopup;
    private NewTaskPopup modifyTaskPopup;
    private List<TaskListItem> taskListItem;
    private ListView taskListView;
    private Context context;

    private boolean selectionMode;
    private boolean managerMode;
    private boolean editMode;
    private TaskListItem item;

    private DataBase dateBase;
    private SharedPreferences preferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);
        activity = this;
        context = this.getApplicationContext();

        dateBase = new DataBase();


        Thread t = new Thread() {
            public void run(){
                dateBase. watchCollections(context);
            }
        };
        t.start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        ImageButton addTaskButton = findViewById(R.id.add_task);
        addTaskButton.setOnClickListener(this);

        ImageButton delTaskButton = findViewById(R.id.del_task);
        delTaskButton.setOnClickListener(this);

        ImageButton backTaskButton = findViewById(R.id.back_task);
        backTaskButton.setOnClickListener(this);

        ImageButton checkTaskButton = findViewById(R.id.ok_task);
        checkTaskButton.setOnClickListener(this);

        ImageButton editTaskButton = findViewById(R.id.edit_task);
        editTaskButton.setOnClickListener(this);

        // need to check if user is manager instead
        managerMode = true;
        try {
            Thread t3 = new Thread(new Runnable() {  @Override public void run() {  createUnassignedTaskDisplay();  } });
            t3.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (managerMode)
            enableManagerMode();
        else
            disableManagerMode();

        taskListItem = new ArrayList<>();
        taskListView = findViewById(R.id.task_list_view);
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (editMode && managerMode)
                    enableSelectionMode();

                if (!editMode)
                {
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(view);
                    ClipData data = ClipData.newPlainText("", "");
                    view.startDrag(data, dragShadow, view, 0);
                }

                return false;
            }
        });
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                TaskListItem item = taskListItem.get(i);
                //System.out.println(taskListItem.get(i));
                if (selectionMode && editMode && managerMode)
                {
                    //TaskListItem item = taskListItem.get(i);
                    item.setSelected(!item.isSelected());
                    taskListView.setAdapter(new TaskListAdapter(context, taskListItem, true));
                }
                else if (!selectionMode && editMode && managerMode)
                {
                    modifyTaskPopup = new NewTaskPopup(activity);
                    modifyTaskPopup.setTitle(getString(R.string.modifytask_popup_title));
                    modifyTaskPopup.setImg(item.getImg());
                    modifyTaskPopup.setName(item.getName());
                    modifyTaskPopup.setDescription(item.getDescription());
                    modifyTaskPopup.setImportance(item.getImportance());
                    modifyTaskPopup.setScore(item.getScore());
                    modifyTaskPopup.setDeadline(item.getDeadline());
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
                            String deadline = modifyTaskPopup.getDeadline();

                            //check fields

                            //update the db

                            //add new tasks in the list
                            taskListItem.remove(item);
                            taskListItem.add(new TaskListItem(name, img, description, importance, score, deadline, "NON_ATTRIBUATE", ""));
                            taskListView.setAdapter(new TaskListAdapter(context, taskListItem));
                            modifyTaskPopup.dismiss();
                        }
                    });
                    modifyTaskPopup.build();
                }
                else if (!editMode)
                {
                    item.setSelected(!item.isSelected());
                    if (selectionMode && !isItemSelected())
                        hideOk();
                    else
                        showOk();
                }
            }
        });

        preferences = getPreferences(MODE_PRIVATE);
        dateBase = new DataBase(this, preferences);
        // les threads rempliront la page lorsque les informations seront récupérées depuis la base de données.

    }

    public void createUnassignedTaskDisplay() {
        List<Document> docs = new ArrayList<Document>();
        dateBase.getNonAssignedTasks()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        // ici je récupère directement les infos du JSON,
                        // Il faudrait peut etre transformer dans un premier temps le json en un object Tâche concret
                        // Ensuite ajouter la tâche en tant que ListItem;
                        JSONObject obj = new JSONObject(d.toJson());
                        String id = d.getObjectId("_id").toString();
                        String name = obj.getString("task_name").toString();
                        String img = obj.getString("task_image").toString();
                        String description = obj.getString("task_desc").toString();
                        int importance = (int)Integer.parseInt(obj.getString("task_priority"));
                        int score = (int)Integer.parseInt( obj.getString("task_score"));
                        String frequency = obj.getString("task_priority").toString();
                        String status = obj.getString("task_status").toString();
                        String strDate = obj.getString("task_limit_date").toString();
                        strDate = strDate.replace("{\"$date\":","").replace("}","");

                        long date = (long)Long.parseLong(strDate);
                        String deadline = formatDate(new Date(date));
                        TaskListItem t = new TaskListItem(name,img,description,importance,score,deadline,status,id);
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
                Log.d("stitch","going in leaderboard");
                startActivity(intent2);
                break;
            case R.id.action_mytasks:
                Intent intent3 = new Intent(this, TaskActivity.class);
                Log.d("stitch","going in mytasks");
                startActivity(intent3);
                break;
            case R.id.action_profile:
                Intent intent4 = new Intent(this, ProfilActivity.class);
                Log.d("stitch","going in profile");
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
            case R.id.add_task:
                //Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                newTaskPopup = new NewTaskPopup(activity);
                newTaskPopup.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newTaskPopup.dismiss();
                    }
                });
                newTaskPopup.getCameraBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,0);
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
                        String deadline = newTaskPopup.getDeadline();
                        System.out.println("On click parce qu'on créer un nouveau task");

                            Thread t5 = new Thread(new Runnable() {  @Override public void run() {
                                dateBase.createTask(name, img, description, importance, score, formatDate(deadline)).addOnCompleteListener( new OnCompleteListener<RemoteInsertOneResult>()
                                {
                                        @Override
                                        public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                                            taskListItem.add( new TaskListItem(name,img,description,importance,score,deadline,"NON_ATTRIBUATE",task.getResult().getInsertedId().asObjectId().toString()));
                                            Log.d("stitch ", "new ID : " + task.getResult().getInsertedId().toString() );
                                            TaskListAdapter a = new TaskListAdapter(context,taskListItem);
                                            taskListView.setAdapter(a);
                                            a.notifyDataSetChanged();
                                        }
                                    }
                            );}});

                            t5.start();

                        newTaskPopup.dismiss();
                    }
                });
                newTaskPopup.build();
                break;
            case R.id.del_task:
                //deleteSelection();
                try {
                    for (int i=0; i<taskListItem.size(); i++) {
                        if (taskListItem.get(i).isSelected()) {
                            item = taskListItem.get(i);
                            Thread t5 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dateBase.deleteTask(item.getId());
                                    taskListItem.remove(item);
                                }
                            });
                            t5.start();

                        }
                    }

                    TaskListAdapter a = new TaskListAdapter(context,taskListItem);
                    taskListView.setAdapter(a);
                    a.notifyDataSetChanged();
                }

                catch (Exception e) {
                e.printStackTrace();
            }

        disableSelectionMode();
        // + del on db
        break;
            case R.id.back_task:
                if (selectionMode && editMode)
                {
                    uncheckAllTasks();
                    disableSelectionMode();
                }
                else if (!selectionMode && editMode)
                    disableEditMode();
                else
                {
                    uncheckAllTasks();
                    hideOk();
                }
                break;

            case R.id.edit_task:
                enableEditMode();
                break;

            case R.id.ok_task:
                try {
                    for (int i=0; i<taskListItem.size(); i++) {
                        if (taskListItem.get(i).isSelected()) {
                            item = taskListItem.get(i);
                            Thread t5 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dateBase.startTask(item.getId());
                                    taskListItem.remove(item);
                                }
                            });
                            t5.start();
                            i--;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                deleteSelection();
                hideOk();
                break;
        }
    }

    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newTaskPopup.onActivityResult(requestCode, resultCode, data);
    }

    private void disableSelectionMode()
    {
        selectionMode = false;
        findViewById(R.id.add_task).setVisibility(View.VISIBLE);
        findViewById(R.id.del_task).setVisibility(View.GONE);
        //findViewById(R.id.back_task).setVisibility(View.GONE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, false));
    }

    private void enableSelectionMode()
    {
        selectionMode = true;
        findViewById(R.id.add_task).setVisibility(View.GONE);
        findViewById(R.id.del_task).setVisibility(View.VISIBLE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, true));
    }

    private void disableEditMode()
    {
        selectionMode = false;
        editMode = false;
        findViewById(R.id.add_task).setVisibility(View.GONE);
        findViewById(R.id.back_task).setVisibility(View.GONE);
        findViewById(R.id.edit_task).setVisibility(View.VISIBLE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, false));
    }

    private void enableEditMode()
    {
        editMode = true;
        findViewById(R.id.add_task).setVisibility(View.VISIBLE);
        findViewById(R.id.back_task).setVisibility(View.VISIBLE);
        findViewById(R.id.edit_task).setVisibility(View.GONE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, false));
    }

    private void disableManagerMode()
    {
        selectionMode = false;
        editMode = false;
        managerMode = false;
        findViewById(R.id.add_task).setVisibility(View.GONE);
        findViewById(R.id.back_task).setVisibility(View.GONE);
        findViewById(R.id.edit_task).setVisibility(View.GONE);
        findViewById(R.id.del_task).setVisibility(View.GONE);
    }

    private void enableManagerMode()
    {
        selectionMode = false;
        editMode = false;
        managerMode = true;
        findViewById(R.id.add_task).setVisibility(View.GONE);
        findViewById(R.id.back_task).setVisibility(View.GONE);
        findViewById(R.id.edit_task).setVisibility(View.VISIBLE);
        findViewById(R.id.del_task).setVisibility(View.GONE);
    }

    private void uncheckAllTasks()
    {
        for (TaskListItem item : taskListItem)
            item.setSelected(false);
    }

    private void hideOk()
    {
        if (managerMode)
            findViewById(R.id.edit_task).setVisibility(View.VISIBLE);
        selectionMode = false;
        findViewById(R.id.ok_task).setVisibility(View.GONE);
        findViewById(R.id.back_task).setVisibility(View.GONE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem));
    }

    private void showOk()
    {
        if (managerMode)
            findViewById(R.id.edit_task).setVisibility(View.GONE);
        selectionMode = true;
        findViewById(R.id.ok_task).setVisibility(View.VISIBLE);
        findViewById(R.id.back_task).setVisibility(View.VISIBLE);
        taskListView.setAdapter(new TaskListAdapter(context, taskListItem, false, true));
    }

    private void deleteSelection()
    {
        for (int i=0; i<taskListItem.size(); i++)
            if (taskListItem.get(i).isSelected())
            {
                taskListItem.remove(i);
                i--;
            }
    }

    private boolean isItemSelected()
    {
        for (TaskListItem item : taskListItem)
            if (item.isSelected())
                return true;
        return false;
    }

    public String formatDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public Date formatDate(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
