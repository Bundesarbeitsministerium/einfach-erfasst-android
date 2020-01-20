package de.bmas.einfach_erfasst.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.UserProfile;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityIntro1 extends ActivityBase
{
    private EditText firstName = null;
    private EditText lastName = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_intro_1);

        this.firstName = (EditText)this.findViewById(R.id.intro_first_name);
        this.lastName = (EditText)this.findViewById(R.id.intro_last_name);
        Button button = (Button)this.findViewById(R.id.footer_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                saveNames();
                startActivity(ActivityIntro2.class);
            }
        });
    }

    private void saveNames ()
    {
        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        if (results.size() > 0)
        {
            UserProfile userProfile = results.first();
            realm.beginTransaction();
            userProfile.setFirstName(this.firstName.getText().toString());
            userProfile.setLastName(this.lastName.getText().toString());
            realm.commitTransaction();
        }
        realm.close();
    }
}
