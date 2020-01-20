package de.bmas.einfach_erfasst.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FragmentTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    private Calendar calendar;
    private Date date;
    private boolean timer;

    public Date getDate () { return date; }
    public void setDate (Date date) { this.date = date; }
    public boolean isTimer () { return timer; }
    public void setTimer (boolean timer) { this.timer = timer; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
        if (this.timer)
        {
            this.calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        return new TimePickerDialog(this.getActivity(),
                                    this,
                                    this.calendar.get(Calendar.HOUR_OF_DAY),
                                    this.calendar.get(Calendar.MINUTE),
                                    true);
    }

    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute)
    {
        this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.calendar.set(Calendar.MINUTE, minute);
        this.date.setTime(this.calendar.getTimeInMillis());
    }
}
