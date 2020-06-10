package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.controller.LoginValidation;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener{
    private ProfilActivity activity;
    private ModifyProfilPopup modifyProfilPopup;
    private DataBase dataBase;
    private SharedPreferences preferences;
    private TextView textPseudo;
    private TextView textScore;
    private Button modifyButton;
    private String pseudo;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        dataBase = new DataBase(this, preferences);
        preferences = getPreferences(MODE_PRIVATE);
        activity = this;

        modifyButton =(Button) findViewById(R.id.modify_profile_button);
        modifyButton.setOnClickListener(this);

        textPseudo = (TextView) findViewById(R.id.text_pseudo);
        textPseudo.setText("retrieving");

        textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText("retrieving");

        Thread t6 = new Thread(new Runnable() {  @Override public void run() {  createUserProfile();  } });
        t6.start();

    }

        public void createUserProfile( ){
            dataBase.getUserInfo().addOnSuccessListener(new OnSuccessListener<Document>() {
                @Override
                public void onSuccess(Document document) {
                    try {
                        Log.d("stitch",document.toString());
                        JSONObject obj = new JSONObject(document.toJson());
                        Log.d("stitch", obj.toString());
                        String name = obj.getString("pseudo");
                        textPseudo.setText(name);
                        String score = obj.getString("score");
                        textScore.setText("Score :"+score);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    pseudo = modifyProfilPopup.getPseudo();
                    textPseudo.setText(pseudo);
                    modifyProfilPopup.setBasePseudo();
                    modifyProfilPopup.dismiss();
                    break;
            }
        }
    }
