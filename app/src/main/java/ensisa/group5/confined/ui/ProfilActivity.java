package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.LoginValidation;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener {

    private ProfilActivity activity;
    private ModifyProfilPopup modifyProfilPopup;
    private LoginValidation loginValidation;
    private SharedPreferences preferences;
    TextView textPseudo;
    TextView textScore;
    String img;




    protected void OnCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        loginValidation = new LoginValidation(this, preferences);
        preferences = getPreferences(MODE_PRIVATE);

        activity = this;

        ImageButton modifyButton = findViewById(R.id.modify_button);

        TextView textPseudo = (TextView) findViewById(R.id.text_pseudo);
        textPseudo.setText("retrieving");

        TextView textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText("retrieving");



        Thread t6 = new Thread(new Runnable() {  @Override public void run() {  createUserProfile();  } });
        t6.start();

    }

    public void createUserProfile( ){
        loginValidation.getUserInfo().addOnSuccessListener(new OnSuccessListener<Document>() {
            @Override
            public void onSuccess(Document document) {
                try {
                    JSONObject obj = new JSONObject(document.toJson());
                    Log.d("stitch", obj.toString());
                    String name = obj.getString("pseudo").toString();
                    textPseudo.setText(name);
                    String score =obj.getString("score").toString();
                    textScore.setText(score + " .");
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
            case R.id.modify_button:
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
                textPseudo.setText(modifyProfilPopup.getPseudo());


                // modifier les éléments dans la base de données;
                // utiliser CreateUserProfil;


        }

    }



}