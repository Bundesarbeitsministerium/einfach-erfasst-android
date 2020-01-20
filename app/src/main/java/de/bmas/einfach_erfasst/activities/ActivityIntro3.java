package de.bmas.einfach_erfasst.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.UserProfile;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityIntro3 extends ActivityBase
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_intro_3);

        TextView link = (TextView)this.findViewById(R.id.intro_text_a_5);
        link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                Uri uri = Uri.fromParts("mailto", "datenschutz-app@bmas.bund.de", null);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                Intent letter = Intent.createChooser(intent, "Kontakt");
                startActivity(letter);
            }
        });

        Button button = (Button)this.findViewById(R.id.footer_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                acceptUserAgreement();
                startActivity(ActivityTracking.class, true);
            }
        });
    }

    private void acceptUserAgreement ()
    {
        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        if (results.size() > 0)
        {
            UserProfile userProfile = results.first();
            realm.beginTransaction();
            userProfile.setAccepted(true);
            realm.commitTransaction();
        }
        realm.close();
    }
}
