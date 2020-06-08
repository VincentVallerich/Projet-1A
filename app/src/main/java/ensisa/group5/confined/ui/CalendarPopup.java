package ensisa.group5.confined.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import ensisa.group5.confined.R;

public class CalendarPopup extends Dialog
{

    public CalendarPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.calendar_popup_template);
    }

    public void build()
    {
        show();
    }
}
