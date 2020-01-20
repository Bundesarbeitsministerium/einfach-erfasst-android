package de.bmas.einfach_erfasst.objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTracker
{
    private final String SETTINGS_KEY = "TimeTracker";
    private final String SETTINGS_WORKING_BEGIN_KEY = "TimeTrackerWorkingBegin";
    private final String SETTINGS_WORKING_END_KEY = "TimeTrackerWorkingEnd";
    private final String SETTINGS_RESTING_BEGIN_KEY = "TimeTrackerRestingBegin";
    private final String SETTINGS_RESTING_END_KEY = "TimeTrackerRestingEnd";
    private final String SETTINGS_RESTING_TOTAL_KEY = "TimeTrackerRestingTotal";

    private Date workingBegin;
    private Date workingEnd;

    private Date restingBegin;
    private Date restingEnd;
    private long restingTotal;

    public TimeTracker ()
    {
        super();
        this.clean();
    }

    public Date getWorkingBegin () { return this.workingBegin; }
    public Date getWorkingEnd () { return this.workingEnd; }
    public void setRestingTotal (long restingTotal) { this.restingTotal = restingTotal; }
    public long getRestingTotal () { return this.restingTotal; }

    public void beginWorking ()
    {
        if (this.workingBegin == null)
        {
            this.workingBegin = new Date();
        }

        this.workingEnd = null;
    };
    public void endWorking ()
    {
        this.workingEnd = new Date();
    };
    public boolean isWorking ()
    {
        return this.workingBegin != null && this.workingEnd == null;
    }
    public boolean hasWorked ()
    {
        return this.workingBegin != null && this.workingEnd != null;
    }

    public void beginResting ()
    {
        this.restingBegin = new Date();
        this.restingEnd = null;
    };
    public void endResting ()
    {
        this.restingEnd = new Date();
        this.restingTotal += this.getRestingTime();

        this.restingBegin = null;
        this.restingEnd = null;
    };
    public boolean isResting ()
    {
        return this.restingBegin != null;
    }

    public void save()
    {
        Context context = AnApplication.getContext();
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (this.workingBegin != null) { editor.putLong(SETTINGS_WORKING_BEGIN_KEY, this.workingBegin.getTime()); }
        else { editor.remove(SETTINGS_WORKING_BEGIN_KEY); }

        if (this.workingEnd != null) { editor.putLong(SETTINGS_WORKING_END_KEY, this.workingEnd.getTime()); }
        else { editor.remove(SETTINGS_WORKING_END_KEY); }

        if (this.restingBegin != null) { editor.putLong(SETTINGS_RESTING_BEGIN_KEY, this.restingBegin.getTime()); }
        else { editor.remove(SETTINGS_RESTING_BEGIN_KEY); }

        if (this.restingEnd != null) { editor.putLong(SETTINGS_RESTING_END_KEY, this.restingEnd.getTime()); }
        else { editor.remove(SETTINGS_RESTING_END_KEY); }

        editor.putLong(SETTINGS_RESTING_TOTAL_KEY, this.restingTotal);
        editor.apply();
    }
    public void load()
    {
        Context context = AnApplication.getContext();
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, 0);

        if (settings.contains(SETTINGS_WORKING_BEGIN_KEY)) { this.workingBegin = new Date(settings.getLong(SETTINGS_WORKING_BEGIN_KEY, 0)); }
        else { this.workingBegin = null; }

        if (settings.contains(SETTINGS_WORKING_END_KEY)) { this.workingEnd = new Date(settings.getLong(SETTINGS_WORKING_END_KEY, 0)); }
        else { this.workingEnd = null; }

        if (settings.contains(SETTINGS_RESTING_BEGIN_KEY)) { this.restingBegin = new Date(settings.getLong(SETTINGS_RESTING_BEGIN_KEY, 0)); }
        else { this.restingBegin = null; }

        if (settings.contains(SETTINGS_RESTING_END_KEY)) { this.restingEnd = new Date(settings.getLong(SETTINGS_RESTING_END_KEY, 0)); }
        else { this.restingEnd = null; }

        this.restingTotal = settings.getLong(SETTINGS_RESTING_TOTAL_KEY, 0);
    }
    public void clean()
    {
        this.workingBegin = null;
        this.workingEnd = null;

        this.restingBegin = null;
        this.restingEnd = null;
        this.restingTotal = 0L;
    }

    public long getWorkingTime ()
    {
        long working = 0L;

        if (this.workingBegin != null && this.workingEnd != null)
        {
            working = this.workingEnd.getTime() - this.workingBegin.getTime();
            working -= this.restingTotal;
        }
        else if (this.workingBegin != null)
        {
            if (this.restingBegin != null)
            {
                working = this.restingBegin.getTime() - this.workingBegin.getTime();
            }
            else
            {
                working = (new Date()).getTime() - this.workingBegin.getTime();
            }

            working -= this.restingTotal;
        }

        return working;
    }
    public String getWorkingTimeString ()
    {
        return getTimeString(this.getWorkingTime());
    }
    public String getWorkingBeginDateString ()
    {
        if (this.workingBegin != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.", Locale.getDefault());
            String string = dateFormat.format(this.workingBegin);
            string = string.replaceFirst("\\.", ",");
            return string;
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateTimeString ()
    {
        if (this.workingBegin != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return dateFormat.format(this.workingBegin);
        }
        else
        {
            return "";
        }
    }
    public String getWorkingEndDateString ()
    {
        if (this.workingEnd != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.", Locale.getDefault());
            String string = dateFormat.format(this.workingEnd);
            string = string.replaceFirst("\\.", ",");
            return string;
        }
        else
        {
            return "";
        }
    }
    public String getWorkingEndDateTimeString ()
    {
        if (this.workingEnd != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return dateFormat.format(this.workingEnd);
        }
        else
        {
            return "";
        }
    }

    public long getRestingTime ()
    {
        long resting = 0L;

        if (this.restingBegin != null && this.restingEnd != null)
        {
            resting = this.restingEnd.getTime() - this.restingBegin.getTime();
        }
        else if (this.restingBegin != null)
        {
            resting = (new Date()).getTime() - this.restingBegin.getTime();
        }

        return resting;
    }
    public String getRestingTimeString ()
    {
        return this.getTimeString(this.getRestingTime() + this.restingTotal);
    }
    public String getRestingTotalTimeString ()
    {
        if (this.restingTotal != 0L)
        {
            return getTimeString(this.restingTotal, false);
        }
        else
        {
            return "";
        }
    }

    private String getTimeString (long milliseconds)
    {
        return getTimeString(milliseconds, true);
    }
    private String getTimeString (long milliseconds, boolean showSeconds)
    {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3_600;
        seconds %= 3_600;
        long minutes = seconds / 60;
        seconds %= 60;

        String string;
        if (showSeconds)
        {
            string = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        else
        {
            string = String.format("%02d:%02d", hours, minutes);
        }

        return string;
    }
}
