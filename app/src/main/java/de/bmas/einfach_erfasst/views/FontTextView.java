package de.bmas.einfach_erfasst.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;

public class FontTextView extends TextView
{
    public FontTextView (Context context)
    {
        this(context, null);
    }
    public FontTextView (Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.textViewStyle);
    }
    public FontTextView (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if(!isInEditMode())
        {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontView);
            String fontFamily = array.getString(R.styleable.FontView_fontFamily);
            setTypeface(AnApplication.getFontProvider().getFontByString(fontFamily));
            array.recycle();
        }
    }
}
