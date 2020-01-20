package de.bmas.einfach_erfasst.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeSessionWrapper
{
    private TimeSession timeSession;

    public TimeSessionWrapper()
    {
        this.timeSession = null;
    }
    public TimeSessionWrapper(TimeSession timeSession)
    {
        this.timeSession = timeSession;
    }

    public boolean hasWorked ()
    {
        return this.timeSession.getWorkingBegin() != null && this.timeSession.getWorkingEnd() != null;
    }

    public long getWorkingTotalTime ()
    {
        Date workingBegin = this.timeSession.getWorkingBegin();
        Date workingEnd = this.timeSession.getWorkingEnd();
        long restingTotal = this.timeSession.getRestingTotal();

        long working = 0L;
        if (workingBegin != null && workingEnd != null)
        {
            working = workingEnd.getTime() - workingBegin.getTime();
            working -= restingTotal;
        }

        return working;
    }
    public String getWorkingTotalTimeString ()
    {
        if (this.timeSession != null)
        {
            return getTimeString(this.getWorkingTotalTime(), false);
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateMonthYear ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingBegin());
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateDay ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("dd.", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingBegin());
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateWeekday ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("EE", Locale.getDefault());
            String string = dateFormat.format(this.timeSession.getWorkingBegin());
            string = string.replaceFirst("\\.", "");
            return string;
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.", Locale.getDefault());
            String string = dateFormat.format(this.timeSession.getWorkingBegin());
            string = string.replaceFirst("\\.", ",");
            return string;
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateMailString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingBegin());
        }
        else
        {
            return "";
        }
    }
    public String getWorkingBeginDateTimeString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingBegin() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingBegin());
        }
        else
        {
            return "";
        }
    }
    public String getWorkingEndDateString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingEnd() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.", Locale.getDefault());
            String string = dateFormat.format(this.timeSession.getWorkingEnd());
            string = string.replaceFirst("\\.", ",");
            return string;
        }
        else
        {
            return "";
        }
    }
    public String getWorkingEndDateMailString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingEnd() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingEnd());
        }
        else
        {
            return "";
        }
    }
    public String getWorkingEndDateTimeString ()
    {
        if (this.timeSession != null && this.timeSession.getWorkingEnd() != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return dateFormat.format(this.timeSession.getWorkingEnd());
        }
        else
        {
            return "";
        }
    }

    public long getRestingTotalTime ()
    {
        return this.timeSession.getRestingTotal();
    }
    public String getRestingTotalTimeString ()
    {
        if (this.timeSession != null)
        {
            return getTimeString(this.getRestingTotalTime(), false);
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
