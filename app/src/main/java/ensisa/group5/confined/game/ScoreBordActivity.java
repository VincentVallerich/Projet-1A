package ensisa.group5.confined.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.BoardActivity;

import ensisa.group5.confined.ui.TaskActivity;

public class ScoreBordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_bord);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
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