package de.bmas.einfach_erfasst.objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class TimeSessionCreator
{
    public static TimeSession newTimeSession ()
    {
        long id = 0;

        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<TimeSession> realmResults = realm.where(TimeSession.class).findAllSorted("id");
        if (realmResults.size() > 0)
        {
            id = realmResults.last().getId() + 1;
        }
        realm.close();

        TimeSession timeSession = new TimeSession();
        timeSession.setId(id);

        return timeSession;
    }
}
