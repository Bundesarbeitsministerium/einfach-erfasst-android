package de.bmas.einfach_erfasst.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import de.bmas.einfach_erfasst.R;

public class FragmentAlert extends DialogFragment
{
    private String pause = "";
    private String pauseNeeded = "";

    public void setPause (String pause)
    {
        this.pause = pause;
    }
    public void setPauseNeeded (String pauseNeeded)
    {
        this.pauseNeeded = pauseNeeded;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_alert);

        TextView textView = (TextView)dialog.findViewById(R.id.al_text_1);
        String string = textView.getText().toString();
        string = string.replaceFirst("%pause%", this.pause);
        string = string.replaceFirst("%pause-needed%", this.pauseNeeded);
        textView.setText(string);

        dialog.findViewById(R.id.al_button_yes).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                accept();
            }
        });
        dialog.findViewById(R.id.al_button_no).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                cancel();
            }
        });

        return dialog;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void accept () {}
    public void cancel () {}
}
