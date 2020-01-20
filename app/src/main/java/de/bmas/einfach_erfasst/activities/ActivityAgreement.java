package de.bmas.einfach_erfasst.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.bmas.einfach_erfasst.R;

public class ActivityAgreement extends ActivityBase
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_agreement);

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
    }
}
