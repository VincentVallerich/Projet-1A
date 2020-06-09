package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.contentcapture.ContentCaptureContext;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import ensisa.group5.confined.R;

public class CalendarPopup extends Dialog
{

    private CalendarView calendar;
    private String date;

    public CalendarPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.calendar_popup_template);

        calendar = findViewById(R.id.calendarView);
        /*calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                // date format : mm-dd-yy
                date = String.valueOf(i1) + "-" + String.valueOf(i2) + "-" + String.valueOf(i);
                CalendarPopup.super.dismiss();
            }
        });*/
    }

    public String getDate()
    {
        return date;
    }

    public CalendarView getCalendar()
    {
        return calendar;
    }

    public void build()
    {
        show();
    }
}
