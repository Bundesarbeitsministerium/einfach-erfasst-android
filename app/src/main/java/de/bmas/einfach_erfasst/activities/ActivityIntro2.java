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

public class ActivityIntro2 extends ActivityBase
{
    private EditText emailName = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_intro_2);

        this.emailName = (EditText)this.findViewById(R.id.intro_email);
        Button button = (Button)this.findViewById(R.id.footer_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                saveEmail();
                startActivity(ActivityIntro3.class);
            }
        });
    }

    private void saveEmail ()
    {
        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        if (results.size() > 0)
        {
            UserProfile userProfile = results.first();
            realm.beginTransaction();
            userProfile.setEmail(this.emailName.getText().toString());
            realm.commitTransaction();
        }
        realm.close();
    }
}
