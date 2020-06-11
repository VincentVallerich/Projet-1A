package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.DataBase;
import ensisa.group5.confined.ui.adapter.IconAdapter;

import ensisa.group5.confined.ui.model.IconImgItem;


    public class PickIconPopup extends Dialog
    {
        // attributs
        private Context context;
        List<IconImgItem> iconImgItem;
        GridView gridView;
        private String img;
        private Button cancelButton;
        private Button chooseButton;
        private DataBase dataBase=new DataBase();
        private int userScore = -1;

        // constructeur
        public PickIconPopup(Activity activity) {
            super(activity, R.style.Theme_AppCompat_Light_Dialog);
            setContentView(R.layout.pickicon_popup_template);
            context = activity.getApplicationContext();

            cancelButton = findViewById(R.id.pickicon_popup_template_cancel_btn);
            chooseButton = findViewById(R.id.pickicon_popup_template_choose_btn);

            iconImgItem = new ArrayList<>();
            gridView = (GridView) findViewById(R.id.pickicon_popup_template_gridview);
            gridView.setOnItemClickListener((adapterView, view, i, l) -> {
                IconImgItem item = (IconImgItem) gridView.getItemAtPosition(i);
                img = item.getImg();
            });

            getScore();
            Thread thread = new Thread() {
                public void run() {
                    while (userScore < 0) {
                        Field[] fields = R.drawable.class.getFields();
                        int iconRank = 0;
                        for (int count = 0; count < fields.length; count++) {
                            String name = fields[count].getName();
                            if (name.contains("profil_icon")) {
                                iconImgItem.add(new IconImgItem(name));
                            } else if (name.contains("profil_reward")) {
                                iconRank += 1;
                                if (iconRank > getRewardRank()) {
                                    iconImgItem.add(new IconImgItem("caps_lock"));
                                } else {
                                    iconImgItem.add(new IconImgItem(name));
                                }
                            }
                        }
                        gridView.setAdapter(new IconAdapter(context, iconImgItem));
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }

        // méthodes
        public String getImg()
        {
            return img;
        }

        public Button getCancelButton()
        {
            return cancelButton;
        }

        public Button getChooseButton()
        {
            return chooseButton;
        }

        public void build()
        {
            show();
        }


        public void getScore(){
            Document doc = new Document();
            dataBase.getUserInfo().addOnSuccessListener(document -> {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(document.toJson());
                    String score = obj.getString("score");
                    userScore = Integer.parseInt(score);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        public int getRewardRank(){
            int i=5;
            int step=0;
            while(userScore > i)
            {
                step+=1;
                i+=5;
            }
            return step;
        }


    }

