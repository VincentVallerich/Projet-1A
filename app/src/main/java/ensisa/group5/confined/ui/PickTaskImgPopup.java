package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.adapter.TaskImgAdapter;
import ensisa.group5.confined.ui.model.TaskImgItem;

public class PickTaskImgPopup extends Dialog
{
    // attributs
    private Context context;
    List<TaskImgItem> taskImgItem;
    GridView gridView;
    private String img;
    private Button cancelButton;
    private Button chooseButton;

    // constructeur
    public PickTaskImgPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.picktaskimg_popup_template);
        context = activity.getApplicationContext();

        cancelButton = findViewById(R.id.picktaskimg_popup_template_cancel_btn);
        chooseButton = findViewById(R.id.picktaskimg_popup_template_choose_btn);

        taskImgItem = new ArrayList<>();
        gridView = (GridView)findViewById(R.id.picktaskimg_popup_template_gridview);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            TaskImgItem item = (TaskImgItem) gridView.getItemAtPosition(i);
            img = item.getImg();
        });

        Field[] fields = R.drawable.class.getFields();
        for (int count = 0; count < fields.length; count++)
        {
            //System.out.println(fields[count].getName());
            String name = fields[count].getName();
            if ( name.contains("taskicon_") )
                taskImgItem.add(new TaskImgItem(name));
        }

        gridView.setAdapter(new TaskImgAdapter(context, taskImgItem));
    }

    // méthodes
    public String getImg() { return img; }

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