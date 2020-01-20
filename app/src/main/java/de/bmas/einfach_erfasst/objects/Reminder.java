package de.bmas.einfach_erfasst.objects;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.Date;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.activities.ActivityTimeList;
import io.realm.Realm;
import io.realm.RealmResults;

public class Reminder extends BroadcastReceiver
{
    @Override
    public void onReceive (Context context, Intent intent)
    {
        this.showNotification(context);
    }

    public static void showNotification (Context context) {
        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, -7);
        //currentDate.add(Calendar.HOUR, -1);

        Realm realm = Realm.getInstance(context);
        RealmResults<TimeSession> realmResults = realm.where(TimeSession.class)
                                                      .equalTo("sent", false)
                                                      .lessThan("workingBegin", currentDate.getTime())
                                                      .findAll();

        if (realmResults.size() > 0)
        {
            Intent contentIntent = new Intent(context, ActivityTimeList.class);
            contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                                                    R.integer.timeListNotificationId,
                                                                    contentIntent,
                                                                    0);

            Notification notification = new Notification.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.timeListNotification))
                    .setSmallIcon(R.drawable.ic_statusbar)
                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(R.integer.timeListNotificationId, notification);
        }
        else
        {
            notificationManager.cancel(R.integer.timeListNotificationId);
        }

        realm.close();
    }

    public static void createReminder (Context context)
    {
        long interval = 86_400_000;

        Intent intent = new Intent(context, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.integer.reminderId, intent, 0);

        Date date = new Date();
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarAlarm = Calendar.getInstance();
        calendarNow.setTime(date);
        calendarAlarm.setTime(date);
        calendarAlarm.set(Calendar.HOUR_OF_DAY, 10);
        calendarAlarm.set(Calendar.MINUTE, 0);
        calendarAlarm.set(Calendar.SECOND, 0);
        if (calendarAlarm.before(calendarNow))
        {
            calendarAlarm.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendarAlarm.getTimeInMillis(), interval,
                                  pendingIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                      PackageManager.DONT_KILL_APP);
    }

    public static void cancelReminder (Context context)
    {
        Intent intent = new Intent(context, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.integer.reminderId, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                      PackageManager.DONT_KILL_APP);
    }
}
