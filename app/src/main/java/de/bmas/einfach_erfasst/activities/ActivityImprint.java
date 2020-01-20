package de.bmas.einfach_erfasst.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.bmas.einfach_erfasst.R;

public class ActivityImprint extends ActivityBase
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_imprint);

        View.OnClickListener mailTo = new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                TextView textView = (TextView)view;
                Uri uri = Uri.fromParts("mailto", textView.getText().toString(), null);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                Intent letter = Intent.createChooser(intent, "Kontakt");
                startActivity(letter);
            }
        };
        View.OnClickListener browse = new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                TextView textView = (TextView)view;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(textView.getText().toString()));
                startActivity(intent);
            }
        };

        this.findViewById(R.id.aimp_text_a_0_1).setOnClickListener(mailTo);
        this.findViewById(R.id.aimp_text_a_1).setOnClickListener(mailTo);
        this.findViewById(R.id.aimp_text_a_2_1).setOnClickListener(mailTo);
        this.findViewById(R.id.aimp_text_a_2_2).setOnClickListener(browse);
    }
}
