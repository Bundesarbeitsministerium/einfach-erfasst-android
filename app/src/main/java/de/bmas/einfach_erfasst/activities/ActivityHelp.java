package de.bmas.einfach_erfasst.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.bmas.einfach_erfasst.R;

public class ActivityHelp extends ActivityBase
{
    private final int pageNumber = 9;
    private int currentPage;

    private TextView hText;
    private TextView pText;
    private Button prevButton;
    private Button nextButton;
    private TextView navText;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_help);

        this.hText = (TextView)this.findViewById(R.id.ah_text_h);
        this.pText = (TextView)this.findViewById(R.id.ah_text_p);
        this.prevButton = (Button)this.findViewById(R.id.ah_button_prev);
        this.nextButton = (Button)this.findViewById(R.id.ah_button_next);
        this.navText = (TextView)this.findViewById(R.id.ah_nav_text);

        this.currentPage = 0;
        this.update();
    }

    public void showPrevPage (View view)
    {
        if (this.currentPage > 0) { this.currentPage--; }
        this.update();
    }
    public void showNextPage (View view)
    {
        if (this.currentPage < this.pageNumber - 1) { this.currentPage++; }
        this.update();
    }
    private void update ()
    {
        this.hText.setText(this.getResources().getIdentifier("ah_text_h_" + this.currentPage,"string",
                                                             "de.bmas.einfach_erfasst"));
        this.pText.setText(this.getResources().getIdentifier("ah_text_p_" + this.currentPage, "string",
                                                             "de.bmas.einfach_erfasst"));
        this.navText.setText((this.currentPage + 1) + " von " + this.pageNumber);

        if (this.currentPage == 0)
        {
            this.prevButton.setVisibility(View.INVISIBLE);
            this.nextButton.setVisibility(View.VISIBLE);
        }
        else if (this.currentPage == this.pageNumber - 1)
        {
            this.prevButton.setVisibility(View.VISIBLE);
            this.nextButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            this.prevButton.setVisibility(View.VISIBLE);
            this.nextButton.setVisibility(View.VISIBLE);
        }
    }
}
