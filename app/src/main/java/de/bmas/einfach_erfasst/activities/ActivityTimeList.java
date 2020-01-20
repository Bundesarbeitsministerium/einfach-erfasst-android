package de.bmas.einfach_erfasst.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.objects.AdapterTimeList;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.TimeHeader;
import de.bmas.einfach_erfasst.objects.TimeSession;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityTimeList extends ActivityBase
{
    private Button addButton;
    private Button selectButton;
    private Button cancelButton;
    private View botbar;
    private View tabbar;
    private AdapterTimeList listAdapter;
    private Realm realm;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_timelist);

        this.addButton = (Button)this.findViewById(R.id.tl_hd_button_add);
        this.addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                addButtonClicked();
            }
        });
        this.selectButton = (Button)this.findViewById(R.id.tl_hd_button_select);
        this.selectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                selectButtonClicked();
            }
        });
        this.cancelButton = (Button)this.findViewById(R.id.tl_hd_button_cancel);
        this.cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                cancelButtonClicked();
            }
        });

        this.botbar = this.findViewById(R.id.botbar);
        Button deleteButton = (Button)this.findViewById(R.id.tl_ft_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                confirmDeletion();
            }
        });
        Button sendButton = (Button)this.findViewById(R.id.tl_ft_button_send);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                sendButtonClicked();
            }
        });
        this.tabbar = this.findViewById(R.id.tabbar);

        this.realm = Realm.getInstance(this);
        RealmResults<TimeSession> results = this.realm.where(TimeSession.class)
                                                      .findAllSorted("workingBegin", false);

        this.listAdapter = new AdapterTimeList(this, results, false);
        View listViewEmpty = this.findViewById(R.id.list_view_empty);
        ListView listView = (ListView)this.findViewById(R.id.list_view);
        listView.setAdapter(this.listAdapter);
        listView.setEmptyView(listViewEmpty);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int position, long index)
            {
                itemSelected(position);
            }
        });

        this.selectButton.setVisibility(this.listAdapter.getCount() > 0 ? View.VISIBLE : View.INVISIBLE);
    }
    @Override
    public void onResume ()
    {
        super.onResume();

        this.listAdapter.update();
        if (this.listAdapter.isEditing())
        {
            this.selectButton.setVisibility(View.GONE);
        }
        else
        {
            this.selectButton.setVisibility(this.listAdapter.getCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        }
    }
    @Override
    public void onDestroy ()
    {
        if (this.realm != null)
        {
            this.realm.close();
        }

        super.onDestroy();
    }

    private void addButtonClicked ()
    {
        startActivity(ActivityTimeItem.class);
    }
    private void selectButtonClicked ()
    {
        this.addButton.setVisibility(View.INVISIBLE);
        this.selectButton.setVisibility(View.GONE);
        this.cancelButton.setVisibility(View.VISIBLE);

        this.botbar.setVisibility(View.VISIBLE);
        this.tabbar.setVisibility(View.GONE);

        this.listAdapter.setEditing(true);
        this.listAdapter.update();
    }
    private void cancelButtonClicked ()
    {
        this.addButton.setVisibility(View.VISIBLE);
        this.selectButton.setVisibility(this.listAdapter.getCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        this.cancelButton.setVisibility(View.GONE);

        this.botbar.setVisibility(View.GONE);
        this.tabbar.setVisibility(View.VISIBLE);

        this.listAdapter.setEditing(false);
        this.listAdapter.update();
    }

    private void confirmDeletion ()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ActivityTimeList.this).create();
        alertDialog.setMessage("Die von Ihnen ausgewählten Einträge werden unwiderruflich gelöscht.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen", new DialogInterface.OnClickListener()
        {
            public void onClick (DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick (DialogInterface dialog, int which)
            {
                deleteItems();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void deleteItems ()
    {
        RealmResults<TimeSession> selected = this.realm.where(TimeSession.class)
                                                       .equalTo("selected", true)
                                                       .findAllSorted("workingBegin", false);
        this.realm.beginTransaction();
        selected.clear();
        this.realm.commitTransaction();
        this.listAdapter.update();

        RealmResults<TimeSession> all = this.realm.where(TimeSession.class)
                                                  .findAllSorted("workingBegin", false);
        if (all.size() == 0)
        {
            this.cancelButtonClicked();
        }
    }
    private void sendButtonClicked ()
    {
        if (AnApplication.canLetterBeSent())
        {
            this.startActivity(Intent.createChooser(AnApplication.getLetterIntent(), "Versenden"));
        }
        else
        {
            Intent intent = new Intent(this, ActivityAccountChange.class);
            intent.putExtra("SHOULD_SEND_LETTER", true);
            this.startActivity(intent);
        }
    }

    private void itemSelected(int position)
    {
        Object object = (Object)this.listAdapter.getListItem(position);
        if (object instanceof TimeHeader)
        {
            if (this.listAdapter.isEditing())
            {
                TimeHeader timeHeader = (TimeHeader)object;
                this.listAdapter.headerChecked(position, !timeHeader.isSelected());
            }
        }
        else if (object instanceof TimeSession)
        {
            if (this.listAdapter.isEditing())
            {
                TimeSession timeSession = (TimeSession)object;
                this.listAdapter.itemChecked(position, !timeSession.isSelected());
            }
            else
            {
                TimeSession timeSession = (TimeSession)object;
                Intent intent = new Intent(getBaseContext(), ActivityTimeItem.class);
                intent.putExtra("TimeSessionId", timeSession.getId());
                startActivity(intent);
            }
        }
    }
}
