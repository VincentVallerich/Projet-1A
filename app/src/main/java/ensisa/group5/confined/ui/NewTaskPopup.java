package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.adapter.TaskListAdapter;

public class NewTaskPopup extends Dialog implements AdapterView.OnItemSelectedListener
{
    // attributs
    private Context context;
    private Button cancelButton;
    private Button addButton;
    private Activity activity;
    private String img;
    private ImageButton imgBtn;
    private EditText name;
    private EditText description;
    private RatingBar importance;
    private RatingBar score;
    private ImageButton calendar;
    private TextView deadline;

    private ImageButton cameraBtn;

    private PickTaskImgPopup pickTaskImgPopup;
    private CalendarPopup calendarPopup;
    private TextView title;

    // constructeur
    public NewTaskPopup(Activity activity)
    {
        //super(activity, R.style.Theme_AppCompat_Light_Dialog);
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.newtask_popup_template);
        context = activity.getApplicationContext();
        this.activity = activity;

        //
        title = findViewById(R.id.newtask_popup_template_title);
        cameraBtn = findViewById(R.id.newtask_popup_template_camera_btn);
        imgBtn = findViewById(R.id.newtask_popup_template_taskimg);
        name = findViewById(R.id.newtask_popup_template_name_txtbox);
        description = findViewById(R.id.newtask_popup_template_description_txtbox);
        importance = findViewById(R.id.newtask_popup_template_importance_ratingBar);
        score = findViewById(R.id.newtask_popup_template_score_ratingBar);
        calendar = findViewById(R.id.newtask_popup_template_deadline_calendar_btn);
        deadline = findViewById(R.id.newtask_popup_template_deadline_lbl);

        //
        cancelButton = findViewById(R.id.newtask_popup_template_cancel_btn);
        addButton = findViewById(R.id.newtask_popup_template_addtask_btn);
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        imgBtn.setImageResource(0);
        this.img = img;
        int imgId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        imgBtn.setBackgroundResource(imgId);
    }

    public String getName()
    {
        return name.getText().toString();
    }

    public void setName(String txt)
    {
        this.name.setText(txt);
    }

    public String getDescription()
    {
        return description.getText().toString();
    }

    public void setDescription(String txt)
    {
        this.description.setText(txt);
    }

    public int getImportance()
    {
        return (int) importance.getRating();
    }

    public void setImportance(int rating)
    {
        this.importance.setRating(rating);
    }

    public int getScore()
    {
        return (int) score.getRating();
    }

    public void setScore(int rating)
    {
        this.score.setRating(rating);
    }

    public String getDeadline()
    {
        return deadline.getText().toString();
    }

    public void setDeadline(String deadline)
    {
        this.deadline.setText(deadline);
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public ImageButton getCameraBtn()
    {
        return cameraBtn;
    }

    public void setAddButtonName(String name)
    {
        addButton.setText(name);
    }

    public void setTitle(String t)
    {
        title.setText(t);
    }

    public void build()
    {
        // imgButton
        //imgBtn = findViewById(R.id.newtask_popup_template_taskimg);
        imgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickTaskImgPopup = new PickTaskImgPopup(activity);
                pickTaskImgPopup.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickTaskImgPopup.dismiss();
                    }
                });
                pickTaskImgPopup.getChooseButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img = pickTaskImgPopup.getImg();
                        setImg(img);
                        pickTaskImgPopup.dismiss();
                    }
                });
                pickTaskImgPopup.build();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarPopup = new CalendarPopup(activity);
                calendarPopup.getMonthLabel().setText(formatDateMonth(calendarPopup.getCalendar().getFirstDayOfCurrentMonth()));
                calendarPopup.getCalendar().setListener(new CompactCalendarView.CompactCalendarViewListener() {
                    @Override
                    public void onDayClick(Date dateClicked) {
                        deadline.setText(formatDate(dateClicked));
                        calendarPopup.dismiss();
                    }

                    @Override
                    public void onMonthScroll(Date firstDayOfNewMonth) {
                        calendarPopup.getMonthLabel().setText(formatDateMonth(calendarPopup.getCalendar().getFirstDayOfCurrentMonth()));
                    }
                });

                calendarPopup.build();
            }
        });

        show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public String formatDateMonth(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public String formatDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public void getTextFromImage(Bitmap bitmap)
    {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        if (!textRecognizer.isOperational())
            Toast.makeText(context, "Text recognition has failed", Toast.LENGTH_SHORT).show();
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for (int i=0; i<items.size(); i++)
            {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }
            ArrayList fields = new ArrayList(Arrays.asList(sb.toString().split("\n")));
            fillFields(fields);
        }
    }

    private void fillFields(ArrayList<String> fields)
    {
        for (int i=0; i<fields.size(); i++)
        {
            try {
                switch (i) {
                    case 0:
                        setName(fields.get(0));
                        break;
                    case 1:
                        setDescription(fields.get(1));
                        break;
                    case 2:

                        setImportance(Integer.parseInt(fields.get(2)));
                        break;
                    case 3:
                        setScore(Integer.parseInt(fields.get(3)));
                        break;
                    case 4:
                        setDeadline(fields.get(4));
                        break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        getTextFromImage(bitmap);
    }
}