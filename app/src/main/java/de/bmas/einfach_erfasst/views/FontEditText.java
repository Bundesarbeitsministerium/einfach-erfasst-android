package de.bmas.einfach_erfasst.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AnApplication;

public class FontEditText extends EditText
{
    public FontEditText (Context context)
    {
        this(context, null);
    }
    public FontEditText (Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.editTextStyle);
    }
    public FontEditText (Context context, AttributeSet attrs, int defStyle)
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
