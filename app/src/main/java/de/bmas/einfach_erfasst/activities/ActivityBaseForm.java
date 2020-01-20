package de.bmas.einfach_erfasst.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.views.FontTextView;

public class ActivityBaseForm extends ActivityBase
{
    protected View tf = null;
    protected View tfBeginItem = null;
    protected FontTextView tfBeginItemDate = null;
    protected FontTextView tfBeginItemTime = null;
    protected View tfEndItem = null;
    protected FontTextView tfEndItemDate = null;
    protected FontTextView tfEndItemTime = null;
    protected View tfPauseItem = null;
    protected FontTextView tfPauseItemTime = null;
    protected Button tfSaveButton = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
    }

    protected void initializeForm ()
    {
        try
        {
            this.tf = this.findViewById(R.id.ff_content);

            this.tfBeginItem = this.tf.findViewById(R.id.ff_begin_item);
            this.tfBeginItemDate = (FontTextView)this.tfBeginItem.findViewById(R.id.ff_begin_item_date);
            this.tfBeginItemTime = (FontTextView)this.tfBeginItem.findViewById(R.id.ff_begin_item_time);

            this.tfEndItem = this.tf.findViewById(R.id.ff_end_item);
            this.tfEndItemDate = (FontTextView)this.tfEndItem.findViewById(R.id.ff_end_item_date);
            this.tfEndItemTime = (FontTextView)this.tfEndItem.findViewById(R.id.ff_end_item_time);

            this.tfPauseItem = this.tf.findViewById(R.id.ff_pause_item);
            this.tfPauseItemTime = (FontTextView)this.tfPauseItem.findViewById(R.id.ff_pause_item_time);

            this.tfSaveButton = (Button)this.tf.findViewById(R.id.ff_save_button);
        }
        catch (NullPointerException exception)
        {
            Log.e(NullPointerException.class.toString(), exception.toString());
        }
    }
}
