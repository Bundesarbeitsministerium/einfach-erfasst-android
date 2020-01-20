package de.bmas.einfach_erfasst.objects;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import de.bmas.einfach_erfasst.R;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class AdapterTimeList extends RealmBaseAdapter<TimeSession> implements ListAdapter
{
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_SIZE = 2;

    private static class HeaderViewHolder
    {
        ToggleButton checkbox;
        TextView monthYear;
    }
    private static class ItemViewHolder
    {
        LinearLayout bg;
        ToggleButton checkbox;
        TextView weekday;
        TextView day;
        TextView totalTime;
        TextView beginEndTime;
        ImageView arrow;
        View divider;
    }

    private ArrayList<Object> items;
    private boolean editing;

    public AdapterTimeList(Context context, RealmResults<TimeSession> realmResults,
                           boolean automaticUpdate)
    {
        super(context, realmResults, automaticUpdate);

        this.items = new ArrayList<Object>();
        this.update();
    }

    public boolean isEditing () { return  this.editing; }
    public void setEditing (boolean editing)
    {
        this.editing = editing;

        this.updateStatuses();
        this.notifyDataSetChanged();
    }

    public void headerChecked (int position, boolean isChecked)
    {
        TimeHeader header = (TimeHeader)this.items.get(position);
        header.setSelected(isChecked);

        Realm realm = Realm.getInstance(AnApplication.getContext());
        realm.beginTransaction();

        for (int i = position + 1; i < this.items.size(); i++)
        {
            Object object = this.items.get(i);
            if (object instanceof TimeSession)
            {
                TimeSession timeSession = (TimeSession)object;
                timeSession.setSelected(isChecked);
            }
            else if (object instanceof TimeHeader)
            {
                break;
            }
        }

        realm.commitTransaction();
        realm.close();

        this.updateStatuses();
        this.notifyDataSetChanged();
    }
    public void itemChecked (int position, boolean isChecked)
    {
        TimeSession session = (TimeSession)this.items.get(position);

        Realm realm = Realm.getInstance(AnApplication.getContext());
        realm.beginTransaction();
        session.setSelected(isChecked);
        realm.commitTransaction();
        realm.close();

        this.updateStatuses();
        this.notifyDataSetChanged();
    }

    public void update ()
    {
        this.updateSections();
        this.updateStatuses();
        this.notifyDataSetChanged();
    }
    public void updateSections ()
    {
        String monthYear = "";
        this.items.clear();
        for (int i = 0; i < this.realmResults.size(); i++)
        {
            TimeSession session = (TimeSession)this.realmResults.get(i);
            TimeSessionWrapper wrapper = new TimeSessionWrapper(session);

            String newMonthYear = wrapper.getWorkingBeginDateMonthYear();
            if (!monthYear.equalsIgnoreCase(newMonthYear))
            {
                monthYear = newMonthYear;
                TimeHeader header = new TimeHeader();
                header.setMonthYear(monthYear);
                this.items.add(header);
            }

            this.items.add(session);
        }
    }
    public void updateStatuses ()
    {
        boolean selected = true;
        for (int i = this.items.size() - 1; i >= 0; i--)
        {
            Object object = this.items.get(i);
            if (object instanceof TimeSession)
            {
                TimeSession timeSession = (TimeSession)object;
                selected = selected && timeSession.isSelected();
            }
            else if (object instanceof TimeHeader)
            {
                TimeHeader timeHeader = (TimeHeader)object;
                timeHeader.setSelected(selected);

                selected = true;
            }
        }
    }

    @Override
    public int getCount()
    {
        return this.items.size();
    }
    public Object getListItem (int position)
    {
        return this.items.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return (long)position;
    }

    @Override
    public int getViewTypeCount() { return VIEW_TYPE_SIZE; }
    @Override
    public int getItemViewType(int position)
    {
        Object object = this.items.get(position);
        return (object instanceof TimeHeader) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int type = this.getItemViewType(position);

        if (type == VIEW_TYPE_HEADER)
        {
            return this.getHeaderView(position, convertView, parent);
        }
        else
        {
            return this.getItemView(position, convertView, parent);
        }
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        HeaderViewHolder holder;
        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.view_time_header, parent, false);
            holder = new HeaderViewHolder();
            holder.checkbox = (ToggleButton)convertView.findViewById(R.id.tl_ti_checkbox);
            holder.monthYear = (TextView)convertView.findViewById(R.id.tl_ti_month_year);
            convertView.setTag(holder);
        }
        else
        {
            holder = (HeaderViewHolder)convertView.getTag();
        }

        TimeHeader header = (TimeHeader)this.items.get(position);
        if (header != null)
        {
            holder.checkbox.setTag(position);
            holder.checkbox.setVisibility(this.editing ? View.VISIBLE : View.GONE);
            holder.checkbox.setOnCheckedChangeListener(null);
            holder.checkbox.setChecked(header.isSelected());
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged (CompoundButton buttonView, boolean isChecked)
                {
                    int position = (int)buttonView.getTag();
                    headerChecked(position, isChecked);
                }
            });

            holder.monthYear.setText(header.getMonthYear());
        }

        return convertView;
    }
    public View getItemView(int position, View convertView, ViewGroup parent)
    {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.view_time_item, parent, false);
            holder = new ItemViewHolder();
            holder.bg = (LinearLayout)convertView.findViewById(R.id.tl_ti_bg);
            holder.checkbox = (ToggleButton)convertView.findViewById(R.id.tl_ti_checkbox);
            holder.weekday = (TextView)convertView.findViewById(R.id.tl_ti_weekday);
            holder.day = (TextView)convertView.findViewById(R.id.tl_ti_day);
            holder.totalTime = (TextView)convertView.findViewById(R.id.tl_ti_total_time);
            holder.beginEndTime = (TextView)convertView.findViewById(R.id.tl_ti_begin_end_time);
            holder.arrow = (ImageView)convertView.findViewById(R.id.tl_ti_arrow);
            holder.divider = convertView.findViewById(R.id.tl_ti_divider);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder)convertView.getTag();
        }

        TimeSession session = (TimeSession)this.items.get(position);
        if (session != null)
        {
            TimeSessionWrapper wrapper = new TimeSessionWrapper(session);

            if (session.isSent()) {
                holder.bg.setBackgroundResource(R.color.c_time_sent);
            } else {
                holder.bg.setBackgroundResource(R.color.c_background);
            }

            holder.checkbox.setTag(position);
            holder.checkbox.setVisibility(this.editing ? View.VISIBLE : View.GONE);
            holder.checkbox.setOnCheckedChangeListener(null);
            holder.checkbox.setChecked(session.isSelected());
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged (CompoundButton buttonView, boolean isChecked)
                {
                    int position = (int)buttonView.getTag();
                    itemChecked(position, isChecked);
                }
            });

            holder.weekday.setText(wrapper.getWorkingBeginDateWeekday());
            holder.day.setText(wrapper.getWorkingBeginDateDay());

            holder.totalTime.setText(wrapper.getWorkingTotalTimeString() + " h");
            holder.beginEndTime.setText(wrapper.getWorkingBeginDateTimeString() + " - " +
                                        wrapper.getWorkingEndDateTimeString());

            holder.arrow.setVisibility(this.editing ? View.GONE : View.VISIBLE);

            boolean gone = (position + 1) != this.items.size() &&
                           this.getItemViewType(position + 1) == VIEW_TYPE_HEADER;
            holder.divider.setVisibility(gone ? View.GONE : View.VISIBLE);
        }

        return convertView;
    }
}
