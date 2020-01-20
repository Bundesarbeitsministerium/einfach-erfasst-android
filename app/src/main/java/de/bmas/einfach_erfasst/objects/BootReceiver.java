package de.bmas.einfach_erfasst.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive (Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Reminder.cancelReminder(context);
            Reminder.createReminder(context);
            Reminder.showNotification(context);
        }
    }
}
