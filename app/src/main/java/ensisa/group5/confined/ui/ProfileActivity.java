package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ensisa.group5.confined.R;
import ensisa.group5.confined.game.ScoreBordActivity;

public class ProfileActivity extends AppCompatActivity {

    private ProfileActivity activity;
  //  private NewProfilPopup modifyProfilPopup;
    private String name;
    private int score;


    protected void OnCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        activity = this;
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(3).setChecked(true);

        ImageButton modifyButton = findViewById(R.id.modify_button);
      //  modifyButton.setOnClickListener(this);

        TextView textPseudo = (TextView) findViewById(R.id.text_pseudo);
        textPseudo.setText("Pseudo :" + name);

        TextView textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText("Score : " + score);

        //ImageView profilImage = findViewById(R.id.profile_image);

    }
    private boolean onClickNavigationBar(Integer integer ){
        Log.d("stitch","going in onclick");
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

    public void onClick(View view)
    {

    }
}