package de.bmas.einfach_erfasst.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.UserProfile;
import de.bmas.einfach_erfasst.objects.AnApplication;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivitySplash extends ActivityBase
{
    private Handler handler = null;
    private Runnable runnable = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_splash);

        this.runnable = new Runnable()
        {
            @Override
            public void run ()
            {
                startAppropriateActivity();
            }
        };
        this.handler = new Handler();
        this.handler.postDelayed(this.runnable, 3000);

        ImageView imageView = (ImageView)this.findViewById(R.id.imageView);
        imageView.setImageBitmap(this.getBitmap(R.drawable.a_splash));
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                handler.removeCallbacks(runnable);
                startAppropriateActivity();
            }
        });
    }
    @Override
    public void onDestroy ()
    {
        this.handler.removeCallbacks(this.runnable);
        super.onDestroy();
    }

    private void startAppropriateActivity ()
    {
        Realm realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
        UserProfile userProfile = null;
        if (results.size() > 0)
        {
            userProfile = results.first();
        }

        if (userProfile != null && userProfile.isAccepted())
        {
            startActivity(ActivityTracking.class, true);
        }
        else
        {
            realm.beginTransaction();
            realm.createObject(UserProfile.class);
            realm.commitTransaction();

            startActivity(ActivityIntro1.class, true);
        }

        realm.close();
    }

    private Bitmap getBitmap (int id)
    {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int reqWidth = (int)(metrics.widthPixels / metrics.density);
        int reqHeight = (int)(metrics.heightPixels / metrics.density);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(this.getResources(), id, options);
        options.inSampleSize = this.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(this.getResources(), id, options);
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
