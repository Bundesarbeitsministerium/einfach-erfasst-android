package de.bmas.einfach_erfasst.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class ActivityBase extends Activity
{
    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startActivity (Class activityClass)
    {
        this.startActivity(activityClass, false);
    }
    public void startActivity (Class activityClass, boolean finishAffinity)
    {
        this.startActivity(new Intent(this, activityClass));
        if (finishAffinity)
        {
            this.finishAffinity();
        }
    }

    public void selectTab1 (View tab1)
    {
        this.startActivity(ActivityTimeList.class, true);
    }
    public void selectTab2 (View tab2)
    {
        this.startActivity(ActivityTracking.class, true);
    }
    public void selectTab3 (View tab3)
    {
        this.startActivity(ActivitySettings.class, true);
    }
    public void goBack (View view)
    {
        this.finish();
    }
}
