package de.bmas.einfach_erfasst.objects;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import de.bmas.einfach_erfasst.R;

import java.util.HashMap;

public class FontProvider
{
    private Context context = null;
    private HashMap<String, Typeface> fonts = null;

    public FontProvider (Context context)
    {
        this.context = context;
        this.fonts = new HashMap<String, Typeface>();
    }

    public Typeface getFontBundesSans () { return this.getFontById(R.string.font_bundes_sans); }
    public Typeface getFontBundesSansBold () { return this.getFontById(R.string.font_bundes_sans_bold); }
    public Typeface getFontBundesSansBoldItalic () { return this.getFontById(R.string.font_bundes_sans_bold_italic); }
    public Typeface getFontBundesSansItalic () { return this.getFontById(R.string.font_bundes_sans_italic); }

    public Typeface getFontBundesSerif () { return this.getFontById(R.string.font_bundes_serif); }
    public Typeface getFontBundesSerifBold () { return this.getFontById(R.string.font_bundes_serif_bold); }
    public Typeface getFontBundesSerifBoldItalic () { return this.getFontById(R.string.font_bundes_serif_bold_italic); }
    public Typeface getFontBundesSerifItalic () { return this.getFontById(R.string.font_bundes_serif_italic); }

    public Typeface getFontById (Integer id)
    {
        return getFontByString(this.context.getString(id));
    }
    public Typeface getFontByString (String string)
    {
        if (!this.fonts.containsKey(string))
        {
            AssetManager assets = this.context.getAssets();
            Typeface typeface = Typeface.createFromAsset(assets, string);
            this.fonts.put(string, typeface);
        }

        return this.fonts.get(string);
    }
}
