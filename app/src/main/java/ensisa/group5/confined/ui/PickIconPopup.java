package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
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

        // constructeur
        public PickIconPopup(Activity activity)
        {
            super(activity, R.style.Theme_AppCompat_Light_Dialog);
            setContentView(R.layout.pickicon_popup_template);
            context = activity.getApplicationContext();

            cancelButton = findViewById(R.id.pickicon_popup_template_cancel_btn);
            chooseButton = findViewById(R.id.pickicon_popup_template_choose_btn);

            iconImgItem= new ArrayList<>();
            gridView = (GridView)findViewById(R.id.pickicon_popup_template_gridview);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    IconImgItem item = (IconImgItem) gridView.getItemAtPosition(i);
                    img = item.getImg();
                }
            });

            Field[] fields = R.drawable.class.getFields();
            for (int count = 0; count < fields.length; count++)
            {
                String name = fields[count].getName();
                if ( name.contains("profil_icon_") )
                    iconImgItem.add(new IconImgItem(name));
            }

            gridView.setAdapter(new IconAdapter(context, iconImgItem));
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

    }

