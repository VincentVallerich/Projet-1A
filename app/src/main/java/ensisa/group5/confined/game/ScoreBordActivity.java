package ensisa.group5.confined.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import ensisa.group5.confined.ui.model.TaskListItem;

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
        Thread t2 = new Thread(new Runnable() {  @Override public void run() {  createLeaderboard();  } });
        t2.start();


    }
    public void createLeaderboard( ){
        List<Document> docs = new ArrayList<Document>();
        database.getLeaderBoard()
                .into(docs).addOnSuccessListener(new OnSuccessListener<List<Document>>() {
            @Override
            public void onSuccess(List<Document> documents) {
                try {
                    for (Document d : docs) {
                        JSONObject obj = new JSONObject(d.toJson());
                        Log.d("stitch", obj.toString());
                        UserListItem t = new UserListItem(obj.getString("pseudo"),obj.getInt("score"));
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