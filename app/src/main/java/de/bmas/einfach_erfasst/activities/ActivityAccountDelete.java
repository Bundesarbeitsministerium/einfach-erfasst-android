package de.bmas.einfach_erfasst.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.TimeSession;
import de.bmas.einfach_erfasst.objects.UserProfile;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityAccountDelete extends ActivityBase
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_account_delete);

        Button deleteButton = (Button)this.findViewById(R.id.st_ad_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                confirmAccountDeletion();
            }
        });
    }

    private void confirmAccountDeletion ()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ActivityAccountDelete.this).create();
        alertDialog.setMessage("Möchten sie wirklich ihre Daten löschen?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen",
                              new DialogInterface.OnClickListener()
                              {
                                  public void onClick (DialogInterface dialog, int which)
                                  {
                                      dialog.dismiss();
                                  }
                              });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                              new DialogInterface.OnClickListener()
                              {
                                  public void onClick(DialogInterface dialog, int which)
                                  {
                                      deleteAccount();

                                      dialog.dismiss();
                                      finish();
                                  }
                              });
        alertDialog.show();
    }
    private void deleteAccount ()
    {
        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<TimeSession> timeSessions = realm.where(TimeSession.class).findAll();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAll();

        realm.beginTransaction();
        timeSessions.clear();
        userProfiles.clear();
        realm.commitTransaction();
        realm.close();
    }
}
