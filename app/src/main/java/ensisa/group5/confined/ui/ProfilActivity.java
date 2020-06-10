package ensisa.group5.confined.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;

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

        profileIcon = findViewById(R.id.profile_icon_image);




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
                        String profil_image = obj.getString("image");
                        setImg(profil_image);
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
                    dataBase.setImage()
                    img = modifyProfilPopup.getImg();
                    setImg(img);

                    pseudo = modifyProfilPopup.getPseudo();
                    if(dataBase.isUsernameFormatCorrect(modifyProfilPopup.getPseudo())) {
                        textPseudo.setText(pseudo);
                        modifyProfilPopup.setBasePseudo();
                    }
                    modifyProfilPopup.dismiss();
                    break;
            }
        }


    public void setImg(String img)
    {
        this.img = img;
        int drawableId = this.getResources().getIdentifier(img, "drawable", this.getPackageName());
        profileIcon.setImageResource(drawableId);
    }


    }
