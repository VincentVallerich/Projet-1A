package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ensisa.group5.confined.R;

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
    private EditText frequency;
    private ImageButton calendar;
    private TextView deadline;

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

        imgBtn = findViewById(R.id.newtask_popup_template_taskimg);
        name = findViewById(R.id.newtask_popup_template_name_txtbox);
        description = findViewById(R.id.newtask_popup_template_description_txtbox);
        importance = findViewById(R.id.newtask_popup_template_importance_ratingBar);
        score = findViewById(R.id.newtask_popup_template_score_ratingBar);
        frequency = findViewById(R.id.newtask_popup_template_frequency_txtbox);
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

    public String getFrequency()
    {
        return frequency.getText().toString();
    }

    public void setFrequency(String txt)
    {
        this.frequency.setText(txt);
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
                calendarPopup.getCalendar().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        //deadline = formatter.format(new Date(i-1900, i1, i2));
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, i);
                        c.set(Calendar.MONTH, i1);
                        c.set(Calendar.DAY_OF_MONTH, i2);
                        deadline.setText(formatter.format(c.getTime()));
                        calendarPopup.dismiss();
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
}