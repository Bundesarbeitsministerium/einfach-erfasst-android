package de.bmas.einfach_erfasst.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.Date;

public class TimeSession extends RealmObject
{
    @PrimaryKey private long id;
    private Date workingBegin;
    private Date workingEnd;
    private long restingTotal;
    private boolean sent;
    private boolean selected;

    public long getId () { return this.id; }
    public void setId (long id) { this.id = id; }
    public Date getWorkingBegin () { return this.workingBegin; }
    public void setWorkingBegin (Date workingBegin) { this.workingBegin = workingBegin; }
    public Date getWorkingEnd () { return this.workingEnd; }
    public void setWorkingEnd (Date workingEnd) { this.workingEnd = workingEnd; }
    public long getRestingTotal () { return this.restingTotal; }
    public void setRestingTotal (long restingTotal) { this.restingTotal = restingTotal; }
    public boolean isSent () { return this.sent; }
    public void setSent (boolean sent) { this.sent = sent; }
    public boolean isSelected () { return this.selected; }
    public void setSelected (boolean selected) { this.selected = selected; }
}
