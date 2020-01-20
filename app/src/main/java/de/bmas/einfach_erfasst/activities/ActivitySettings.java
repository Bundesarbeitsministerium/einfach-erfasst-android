package de.bmas.einfach_erfasst.activities;

import android.os.Bundle;
import android.view.View;

import de.bmas.einfach_erfasst.R;

public class ActivitySettings extends ActivityBase
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_settings);

        View changeView = this.findViewById(R.id.st_bt_change);
        changeView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(ActivityAccountChange.class);
            }
        });

        View deleteView = this.findViewById(R.id.st_bt_delete);
        deleteView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(ActivityAccountDelete.class);
            }
        });

        View agreementView = this.findViewById(R.id.st_bt_agreement);
        agreementView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(ActivityAgreement.class);
            }
        });

        View imprintView = this.findViewById(R.id.st_bt_imprint);
        imprintView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(ActivityImprint.class);
            }
        });

        View helpView = this.findViewById(R.id.st_bt_help);
        helpView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(ActivityHelp.class);
            }
        });
    }
}
