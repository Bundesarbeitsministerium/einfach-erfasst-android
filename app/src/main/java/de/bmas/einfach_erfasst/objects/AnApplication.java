package de.bmas.einfach_erfasst.objects;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class AnApplication extends Application
{
    private static AnApplication application = null;
    public static AnApplication getApplication() { return application; }
    private static Context context = null;
    public static Context getContext() { return context; }

    private static FontProvider fontProvider = null;
    public static FontProvider getFontProvider() { return fontProvider; }

    @Override
    public void onCreate()
    {
        super.onCreate();

        application = this;
        context = this.getApplicationContext();

        fontProvider = new FontProvider(context);
        Locale.setDefault(Locale.GERMANY);

        Reminder.cancelReminder(context);
        Reminder.createReminder(context);
    }

    public static boolean canLetterBeSent ()
    {
        UserProfile profile = null;

        Realm realm = Realm.getInstance(context);
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        if (results.size() > 0)
        {
            profile = results.first();
        }

        boolean isEmpty = profile == null ||
                          profile.getFirstName() == null || profile.getFirstName().equalsIgnoreCase("") ||
                          profile.getLastName() == null || profile.getLastName().equalsIgnoreCase("") ||
                          profile.getEmail() == null || profile.getEmail().equalsIgnoreCase("");
        realm.close();

        return !isEmpty;
    }
    public static Intent getLetterIntent ()
    {
        String name = "";
        String mail = "";

        Realm realm = Realm.getInstance(context);
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        if (results.size() > 0)
        {
            UserProfile profile = results.first();
            name = profile.getFirstName() + " " + profile.getLastName();
            mail = profile.getEmail();
        }
        realm.close();

        String subject = "Zeiterfassung von " + name + " " + getLetterSubjectDates();
        String text = name + "\n\n" + getLetterSelectedTime();

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return intent;
    }
    public static String getLetterSubjectDates ()
    {
        Realm realm = Realm.getInstance(context);
        RealmResults<TimeSession> results = realm.where(TimeSession.class).equalTo("selected", true)
                .findAllSorted("workingBegin");

        String first = (new TimeSessionWrapper(results.first())).getWorkingBeginDateMailString();
        String last = (new TimeSessionWrapper(results.last())).getWorkingBeginDateMailString();

        if (!first.equalsIgnoreCase(last)) {
            return first + " - " + last;
        } else {
            return first;
        }
    }
    public static String getLetterSelectedTime ()
    {
        Realm realm = Realm.getInstance(context);
        RealmResults<TimeSession> results = realm.where(TimeSession.class).equalTo("selected", true)
                                                 .findAllSorted("workingBegin");

        long totalTime = 0L;
        String text = "";
        for (int i = 0; i < results.size(); i++)
        {
            TimeSession timeSession = results.get(i);
            TimeSessionWrapper wrapper = new TimeSessionWrapper(timeSession);
            totalTime += wrapper.getWorkingTotalTime();

            text += "Datum: " + wrapper.getWorkingBeginDateMailString() + "\n";
            text += "Arbeitsbeginn: " + wrapper.getWorkingBeginDateTimeString() + " h\n";
            text += "Arbeitsende: " + wrapper.getWorkingEndDateTimeString() + " h\n";
            text += "Arbeitsdauer: " + wrapper.getWorkingTotalTimeString() + " h\n";
            text += "Pausendauer: " + wrapper.getRestingTotalTimeString() + " h\n\n";
        }

        realm.beginTransaction();
        for (int i = 0; i < results.size(); i++)
        {
            TimeSession timeSession = results.get(i);
            timeSession.setSent(true);
        }
        realm.commitTransaction();
        realm.close();

        long totalSeconds = totalTime / 1000;
        long totalHours = totalSeconds / 3_600;
        totalSeconds %= 3_600;
        long totalMinutes = totalSeconds / 60;
        totalSeconds %= 60;

        String totalTimeString = String.format("%02d:%02d", totalHours, totalMinutes);
        text += "Gesamtarbeitsdauer: " + totalTimeString + " h\n";

        return text;
    }
}
