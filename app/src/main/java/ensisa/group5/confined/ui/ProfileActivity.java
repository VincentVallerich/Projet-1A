package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ensisa.group5.confined.R;

public class ProfileActivity extends AppCompatActivity {

    private ProfileActivity activity;
  //  private NewProfilPopup modifyProfilPopup;
    private String name;
    private int score;


    protected void OnCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        activity = this;

        ImageButton modifyButton = findViewById(R.id.modify_button);
      //  modifyButton.setOnClickListener(this);

        TextView textPseudo = (TextView) findViewById(R.id.text_pseudo);
        textPseudo.setText("Pseudo :" + name);

        TextView textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText("Score : " + score);

        //ImageView profilImage = findViewById(R.id.profile_image);

    }

    public void onClick(View view)
    {
        switch(view.getId())
        {

        }

    }
}