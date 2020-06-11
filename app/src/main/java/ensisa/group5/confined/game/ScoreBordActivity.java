package ensisa.group5.confined.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.game.adapter.UserListAdapter;
import ensisa.group5.confined.game.model.UserListItem;
import ensisa.group5.confined.ui.BoardActivity;

import ensisa.group5.confined.ui.ProfilActivity;
import ensisa.group5.confined.ui.TaskActivity;

public class ScoreBordActivity extends AppCompatActivity {
    private DataBase database;
    private List<UserListItem> userList;
    private ListView scoreboardListView;
    private Context context;
    private ScoreBordActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_bord);
        activity = this;
        context = activity.getApplicationContext();
        database = new DataBase();
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        userList = new ArrayList<>();
        scoreboardListView = findViewById(R.id.scoreboard_listview);
        Thread t2 = new Thread(new Runnable() {  @Override public void run() {  createNewLeaderBoard();  } });
        t2.start();
    }
    public void createLeaderboard( ){
        List<Document> docs = new ArrayList<Document>();
        Log.d("stitch","je suis rentrée dans leaderboard");
        database.getLeaderBoard().limit(5).into(docs).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> documents) {
                try {
                    Log.d("stitch","SUCCESS");
                    Log.d("stitch",docs.toString());
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        Log.d("stitch", obj.toString());
                        UserListItem t = new UserListItem(obj.getString("pseudo"),obj.getInt("score"),obj.getString("image"));
                        userList.add(t);
                        Toast.makeText(context,obj.getString("pseudo") , Toast.LENGTH_SHORT).show();
                    }
                    Log.d("stitch",userList.toString());
                    scoreboardListView.setAdapter(new UserListAdapter(context, userList));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void createNewLeaderBoard() {
        List<Document> docs = new ArrayList<Document>();
        database.getLeaderBoard().into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        Log.d("stitcht",obj.toString());
                        UserListItem t = new UserListItem(obj.getString("pseudo"),obj.getInt("score"),obj.getString("image"));
                        userList.add(t);
                    }
                    scoreboardListView.setAdapter(new UserListAdapter(context, userList));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean onClickNavigationBar(Integer integer ){
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
                Intent intent4 = new Intent(this, ProfilActivity.class);
                startActivity(intent4);
                break;
        }
        return false;
    }

}