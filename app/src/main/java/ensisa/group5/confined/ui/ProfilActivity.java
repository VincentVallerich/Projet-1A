package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.controller.MainActivity;
import ensisa.group5.confined.game.ScoreBordActivity;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener{
    private ProfilActivity activity;
    private ModifyProfilPopup modifyProfilPopup;
    private DataBase dataBase;
    private SharedPreferences preferences;
    private TextView textPseudo;
    private TextView textScore;
    private Button modifyButton;
    private String pseudo;
    private String img;
    private ImageView profileIcon;
    private TextView textMail;
    private Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> onClickNavigationBar(item.getItemId()));
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        dataBase = new DataBase(this, preferences);
        preferences = getSharedPreferences("Pref",MODE_PRIVATE);
        activity = this;

        logoutButton =(Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);
        modifyButton =(Button) findViewById(R.id.modify_profile_button);
        modifyButton.setOnClickListener(this);
        textPseudo = (TextView) findViewById(R.id.text_pseudo);
        textScore = (TextView) findViewById(R.id.text_score);
        textMail = (TextView) findViewById(R.id.text_email);
        textMail.setText(dataBase.getUserEmail());
        profileIcon = findViewById(R.id.profile_icon_image);

        Thread t6 = new Thread(new Runnable() {  @Override public void run() {  createUserProfile();  } });
        t6.start();

    }

        public void createUserProfile( ){
            dataBase.getUserInfo().addOnSuccessListener(document -> {
                try {
                    Log.d("stitch",document.toString());
                    JSONObject obj = new JSONObject(document.toJson());
                    Log.d("stitch", obj.toString());
                    String name = obj.getString("pseudo");
                    textPseudo.setText(name);
                    String score = obj.getString("score");
                    textScore.setText(score);
                    String profil_image = obj.getString("image");
                    setImg(profil_image);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.modify_profile_button:
                    modifyProfilPopup = new ModifyProfilPopup(activity);
                    modifyProfilPopup.getCancelButton().setOnClickListener(this);
                    modifyProfilPopup.getValidationButton().setOnClickListener(this);
                    modifyProfilPopup.build();
                    break;

                case R.id.modify_popup_template_cancel_btn:
                    modifyProfilPopup.dismiss();
                    break;
                case  R.id.modify_popup_template_validation_btn:
                    img = modifyProfilPopup.getImg();
                    if (img != "modify_icon_research") {
                        dataBase.setImage(img);
                        setImg(img);
                    }
                    pseudo = modifyProfilPopup.getPseudo();
                    if(dataBase.isUsernameFormatCorrect(modifyProfilPopup.getPseudo())) {
                        textPseudo.setText(pseudo);
                        modifyProfilPopup.setBasePseudo();
                    }
                    modifyProfilPopup.dismiss();
                    break;
                case R.id.logout_button:
                    SharedPreferences preferences = getSharedPreferences( getString(R.string.PREF_KEY_MAIL), MODE_PRIVATE);
                    preferences.edit().clear().commit();
                    finish();
                    System.exit(0);
                    break;
            }
        }

    private boolean onClickNavigationBar(Integer integer){
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

    public void setImg(String img)
    {
        this.img = img;
        int drawableId = this.getResources().getIdentifier(img, "drawable", this.getPackageName());
        profileIcon.setImageResource(drawableId);
    }
}
