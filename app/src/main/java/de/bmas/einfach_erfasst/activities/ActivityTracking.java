package de.bmas.einfach_erfasst.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.fragments.FragmentAlert;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.TimeSession;
import de.bmas.einfach_erfasst.objects.TimeSessionCreator;
import de.bmas.einfach_erfasst.objects.TimeTracker;
import io.realm.Realm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityTracking extends ActivityBaseForm
{
    private Timer timer = null;
    private TimerTask timerTask = null;
    private TimeTracker timeTracker = null;

    private TextView workingTime = null;
    private TextView restingTime = null;
    private ToggleButton workingButton = null;
    private ToggleButton restingButton = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_tracking);

        this.initializeForm();

        this.workingTime = (TextView)this.findViewById(R.id.at_working_time);
        this.restingTime = (TextView)this.findViewById(R.id.at_resting_time);
        this.workingButton = (ToggleButton)this.findViewById(R.id.at_working_button);
        this.restingButton = (ToggleButton)this.findViewById(R.id.at_resting_button);

        this.timeTracker = new TimeTracker();
        this.timeTracker.load();
        this.update();

        this.workingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean checked)
            {
                workingButtonChecked(checked);
            }
        });
        this.restingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean checked)
            {
                restingButtonChecked(checked);
            }
        });
        this.tfSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                saveButtonClicked();
            }
        });

        this.timer = new Timer();
        this.timerTask = new TimerTask()
        {
            @Override
            public void run ()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        if (workingButton.isChecked() && restingButton.isChecked())
                        {
                            restingTime.setText(timeTracker.getRestingTimeString());
                        }
                        else if (workingButton.isChecked())
                        {
                            workingTime.setText(timeTracker.getWorkingTimeString());
                        }
                    }
                });
            }
        };
        this.timer.scheduleAtFixedRate(timerTask, 0, 250);
    }

    @Override
    public void onDestroy ()
    {
        this.timeTracker.save();
        this.timer.cancel();

        super.onDestroy();
    }

    private void workingButtonChecked (boolean checked)
    {
        if (checked)
        {
            this.timeTracker.beginWorking();
            this.tfBeginItemDate.setText(this.timeTracker.getWorkingBeginDateString());
            this.tfBeginItemTime.setText(this.timeTracker.getWorkingBeginDateTimeString());
            this.tfEndItemDate.setText("");
            this.tfEndItemTime.setText("");
            this.tfSaveButton.setEnabled(false);

            this.addIconToTray();
        }
        else
        {
            if (this.restingButton.isChecked())
            {
                this.restingButton.setChecked(false);
            }

            this.timeTracker.endWorking();
            this.tfEndItemDate.setText(this.timeTracker.getWorkingEndDateString());
            this.tfEndItemTime.setText(this.timeTracker.getWorkingEndDateTimeString());
            this.tfSaveButton.setEnabled(true);

            this.removeIconFromTray();
        }

        this.workingTime.setText(this.timeTracker.getWorkingTimeString());
        this.restingButton.setEnabled(checked);
        this.timeTracker.save();
    }
    private void restingButtonChecked (boolean checked)
    {
        if (checked)
        {
            this.restingTime.setVisibility(View.VISIBLE);
            this.timeTracker.beginResting();
        }
        else
        {
            this.restingTime.setVisibility(View.GONE);
            this.timeTracker.endResting();
            this.tfPauseItemTime.setText(this.timeTracker.getRestingTotalTimeString());
        }

        this.restingTime.setText(this.timeTracker.getRestingTimeString());
        this.timeTracker.save();
    }
    private void saveButtonClicked ()
    {
        final long workingTime = this.timeTracker.getWorkingTime();
        final long restingTime = this.timeTracker.getRestingTotal();
        final long h09 = 32_400_000;
        final long m45 = 2_700_000;
        final long h06 = 21_600_000;
        final long m30 = 1_800_000;
        final long m01 = 60_000;

        if (workingTime > h09 && restingTime < m45)
        {
            FragmentAlert alert = new FragmentAlert()
            {
                @Override
                public void accept ()
                {
                    long temp = workingTime + restingTime - h09;
                    timeTracker.setRestingTotal(Math.min(Math.max(temp, m30), m45));
                    this.dismiss();
                    saveAndUpdate();
                }
                @Override
                public void cancel ()
                {
                    this.dismiss();
                    saveAndUpdate();
                }
            };
            alert.setPause((restingTime / m01) + "");
            alert.setPauseNeeded("45");
            alert.show(this.getFragmentManager(), "alert");
        }
        else if (workingTime > h06 && restingTime < m30)
        {
            FragmentAlert alert = new FragmentAlert()
            {
                @Override
                public void accept ()
                {
                    long temp = workingTime + restingTime - h06;
                    timeTracker.setRestingTotal(Math.min(temp, m30));
                    this.dismiss();
                    saveAndUpdate();
                }
                @Override
                public void cancel ()
                {
                    this.dismiss();
                    saveAndUpdate();
                }
            };
            alert.setPause((restingTime / m01) + "");
            alert.setPauseNeeded("30");
            alert.show(this.getFragmentManager(), "alert");
        }
        else
        {
            this.saveAndUpdate();
        }
    }

    private void saveAndUpdate ()
    {
        this.saveTimeSession();
        this.update();
    }
    private void saveTimeSession ()
    {
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        TimeSession timeSession = TimeSessionCreator.newTimeSession();
        GregorianCalendar gc = new GregorianCalendar();

        // Remove seconds and milliseconds in Working Begin
        gc.setTime(this.timeTracker.getWorkingBegin());
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        timeSession.setWorkingBegin(gc.getTime());

        // Remove seconds and milliseconds in Working End
        gc.setTime(this.timeTracker.getWorkingEnd());
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        timeSession.setWorkingEnd(gc.getTime());

        // Remove seconds and milliseconds in Resting Total
        long restingTotal = (this.timeTracker.getRestingTotal() / 60_000) * 60_000;
        timeSession.setRestingTotal(restingTotal);

        realm.copyToRealm(timeSession);
        realm.commitTransaction();
        realm.close();

        this.timeTracker.clean();
        this.timeTracker.save();
    }
    private void update ()
    {
        this.workingTime.setText(this.timeTracker.getWorkingTimeString());
        this.restingTime.setText(this.timeTracker.getRestingTimeString());
        this.restingTime.setVisibility(this.timeTracker.isResting() ? View.VISIBLE : View.GONE);

        this.workingButton.setChecked(this.timeTracker.isWorking());
        this.restingButton.setEnabled(this.timeTracker.isWorking());
        this.restingButton.setChecked(this.timeTracker.isResting());

        this.tfBeginItemDate.setText(this.timeTracker.getWorkingBeginDateString());
        this.tfBeginItemTime.setText(this.timeTracker.getWorkingBeginDateTimeString());
        this.tfEndItemDate.setText(this.timeTracker.getWorkingEndDateString());
        this.tfEndItemTime.setText(this.timeTracker.getWorkingEndDateTimeString());
        this.tfPauseItemTime.setText(this.timeTracker.getRestingTotalTimeString());
        this.tfSaveButton.setEnabled(this.timeTracker.hasWorked());

        if (this.timeTracker.isWorking())
        {
            this.addIconToTray();
        }
        else
        {
            this.removeIconFromTray();
        }
    }

    private void addIconToTray ()
    {
        Context context = AnApplication.getContext();
        Intent contentIntent = new Intent(context, ActivityTracking.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                                                R.integer.trackingNotificationId,
                                                                contentIntent,
                                                                0);

        Notification notification = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(this.getString(R.string.trackingNotification))
                .setSmallIcon(R.drawable.ic_statusbar)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.integer.trackingNotificationId, notification);
    }
    private void removeIconFromTray ()
    {
        Context context = AnApplication.getContext();

        NotificationManager notificationManager = ((NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE));
        notificationManager.cancel(R.integer.trackingNotificationId);
    }
}
