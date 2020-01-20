package de.bmas.einfach_erfasst.objects;

public class TimeHeader
{
    private String monthYear;
    private boolean selected;

    public String getMonthYear () { return this.monthYear; }
    public void setMonthYear (String monthYear) { this.monthYear = monthYear; }
    public boolean isSelected () { return this.selected; }
    public void setSelected (boolean selected) { this.selected = selected; }
}
