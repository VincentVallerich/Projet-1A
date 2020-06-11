package ensisa.group5.confined.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.ui.PickTaskImgPopup;

import static android.content.Context.MODE_PRIVATE;

public class ModifyProfilPopup extends Dialog {
    private Button cancelButton;
    private Button validationButton;
    private Context context;
    private EditText pseudo;
    private Activity activity;
    private ImageButton imgBtn;
    private String img;
    private PickIconPopup pickIconPopup;
    private DataBase dataBase = new DataBase();


    // constructeur
    public ModifyProfilPopup(Activity activity) {
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.modify_popup_template);
        context = activity.getApplicationContext();
        this.activity = activity;

        pseudo = findViewById(R.id.modify_popup_template_name_txtbox);
        cancelButton = findViewById(R.id.modify_popup_template_cancel_btn);
        imgBtn = findViewById(R.id.modify_img_popup_button);
        validationButton = findViewById(R.id.modify_popup_template_validation_btn);

    }


    public void build() {

        imgBtn.setOnClickListener(v -> {
            pickIconPopup = new PickIconPopup(activity);
            pickIconPopup.getCancelButton().setOnClickListener(view -> pickIconPopup.dismiss());
            pickIconPopup.getChooseButton().setOnClickListener(view -> {
                img = pickIconPopup.getImg();
                if (img != null && img != "caps_lock") {
                    setImg(img);
                    pickIconPopup.dismiss();
                }
            });
            pickIconPopup.build();
        });
        show();
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }
    public Button getValidationButton(){return validationButton;};

    public String getPseudo()
    {
        return pseudo.getText().toString();
    }

    public void setImg(String img)
    {
        this.img = img;
        int drawableId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        imgBtn.setImageResource(drawableId);

    }

    public String getImg()
    {
        return img;
    }

    public void setBasePseudo(){
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        dataBase.setPseudo(pseudo.getText().toString());
    }
}