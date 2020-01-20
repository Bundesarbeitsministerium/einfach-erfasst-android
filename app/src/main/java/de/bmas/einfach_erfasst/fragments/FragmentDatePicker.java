package de.bmas.einfach_erfasst.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private Calendar calendar;
    private Date date;
    public Date getDate () { return date; }
    public void setDate (Date date) { this.date = date; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);

        return new DatePickerDialog(this.getActivity(),
                                    this,
                                    this.calendar.get(Calendar.YEAR),
                                    this.calendar.get(Calendar.MONTH),
                                    this.calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        this.calendar.set(Calendar.YEAR, year);
        this.calendar.set(Calendar.MONTH, month);
        this.calendar.set(Calendar.DAY_OF_MONTH, day);
        this.date.setTime(this.calendar.getTimeInMillis());
    }
}
