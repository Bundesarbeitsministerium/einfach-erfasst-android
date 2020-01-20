package de.bmas.einfach_erfasst.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.UserProfile;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityAccountChange extends ActivityBase
{
    private EditText firstName;
    private EditText lastName;
    private EditText email;

    private Realm realm = null;
    private UserProfile userProfile = null;
    private boolean shouldSendLetter = false;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_account_change);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null)
        {
            this.shouldSendLetter = extras.getBoolean("SHOULD_SEND_LETTER");
        }

        this.realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<UserProfile> realmResults = this.realm.where(UserProfile.class).findAll();
        if (realmResults.size() > 0)
        {
            this.userProfile = realmResults.first();
        }

        this.firstName = (EditText)this.findViewById(R.id.st_ac_first_name);
        this.lastName = (EditText)this.findViewById(R.id.st_ac_last_name);
        this.email = (EditText)this.findViewById(R.id.st_ac_email);
        if (this.userProfile != null)
        {
            this.firstName.setText(this.userProfile.getFirstName());
            this.lastName.setText(this.userProfile.getLastName());
            this.email.setText(this.userProfile.getEmail());
        }

        Button saveButton = (Button)this.findViewById(R.id.st_ac_save_button);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                saveButtonClicked();
            }
        });
    }
    @Override
    public void onDestroy ()
    {
        if (this.realm != null)
        {
            this.realm.close();
        }

        super.onDestroy();
    }

    private void saveButtonClicked ()
    {
        this.realm.beginTransaction();
        if (this.userProfile == null)
        {
            this.userProfile = realm.createObject(UserProfile.class);
        }

        this.userProfile.setFirstName(firstName.getText().toString());
        this.userProfile.setLastName(lastName.getText().toString());
        this.userProfile.setEmail(email.getText().toString());
        this.realm.commitTransaction();

        if (this.shouldSendLetter)
        {
            if (AnApplication.canLetterBeSent())
            {
                this.startActivity(Intent.createChooser(AnApplication.getLetterIntent(), "Versenden"));
                this.finish();
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityAccountChange.this).create();
                alertDialog.setTitle("Hinweis");
                alertDialog.setMessage("Um Ihre Arbeitszeiten versenden zu können, füllen Sie bitte alle Felder vollständig aus.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                      new DialogInterface.OnClickListener()
                                      {
                                          public void onClick(DialogInterface dialog, int which)
                                          {
                                              dialog.dismiss();
                                          }
                                      });
                alertDialog.show();
            }
        }
        else
        {
            this.finish();
        }
    }
}
