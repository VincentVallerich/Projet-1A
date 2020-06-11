package ensisa.group5.confined.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import ensisa.group5.confined.ui.TaskActivity;
import ensisa.group5.confined.ui.model.TaskListItem;

public class ScoreBordActivity extends AppCompatActivity {


    private DataBase database;
    private List<UserListItem> userList;
    private ListView scoreboardListView;
    private Context context;
    private ScoreBordActivity activity;

    ListView listView;
    private int[] imagesRankList = {R.drawable.score_1, R.drawable.score_2, R.drawable.score_3, R.drawable.score_4, R.drawable.score_5, R.drawable.score_6, R.drawable.score_7, R.drawable.score_8, R.drawable.score_9, R.drawable.score_10, R.drawable.score_11, R.drawable.score_12 };
    String[] pseudoList = {"Neo", "Arwen", "Granger", "HarryPotter"};
    String[] scoreList = {"3990", "1990", "987", "2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_bord);

        listView = (ListView) findViewById(R.id.scoreboard_listview);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pseudoList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_list_view_scoreboard, null);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_rank);
            TextView textView_pseudo = (TextView) convertView.findViewById(R.id.textView_pseudo);
            TextView textView_score = (TextView) convertView.findViewById(R.id.textView_score);

            imageView.setImageResource(imagesRankList[position]);
            textView_pseudo.setText(pseudoList[position]);
            textView_score.setText(scoreList[position]);

            return convertView;
        }
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
             //   Intent intent4 = new Intent(this, ProfileActivity.class);
         //       startActivity(intent4);
                break;
        }
        return false;
    }

}