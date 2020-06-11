package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import ensisa.group5.confined.R;

public class CalendarPopup extends Dialog
{

    private CompactCalendarView calendar;
    private TextView monthLabel;
    private String date;

    public CalendarPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.calendar_popup_template);

        calendar = findViewById(R.id.compactcalendar_view);
        monthLabel = findViewById(R.id.compactcalendar_month_lbl);
    }

    public String getDate()
    {
        return date;
    }

    public CompactCalendarView getCalendar()
    {
        return calendar;
    }

    public TextView getMonthLabel()
    {
        return monthLabel;
    }

    public void build()
    {
        show();
    }
}
