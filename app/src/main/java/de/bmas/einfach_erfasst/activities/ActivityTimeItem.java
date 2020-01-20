package de.bmas.einfach_erfasst.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import de.bmas.einfach_erfasst.R;
import de.bmas.einfach_erfasst.fragments.FragmentAlert;
import de.bmas.einfach_erfasst.fragments.FragmentDatePicker;
import de.bmas.einfach_erfasst.fragments.FragmentTimePicker;
import de.bmas.einfach_erfasst.objects.AnApplication;
import de.bmas.einfach_erfasst.objects.TimeSession;
import de.bmas.einfach_erfasst.objects.TimeSessionCreator;
import de.bmas.einfach_erfasst.objects.TimeSessionWrapper;
import io.realm.Realm;
import io.realm.RealmResults;

public class ActivityTimeItem extends ActivityBaseForm
{
    private TextView workingTime = null;

    private Realm realm = null;

    private long timeSessionId = -1L;
    private TimeSession timeSession = null;

    private TimeSession tempSession = null;
    private TimeSessionWrapper tempSessionWrapper = null;

    @Override
    public void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_timeitem);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.timeSessionId = extras.getLong("TimeSessionId");
        }

        this.initializeForm();

        this.realm = Realm.getInstance(AnApplication.getContext());
        RealmResults<TimeSession> realmResults = this.realm.where(TimeSession.class)
                                                           .equalTo("id", this.timeSessionId)
                                                           .findAll();
        if (realmResults.size() > 0)
        {
            this.timeSession = realmResults.first();
        }

        this.tempSession = new TimeSession();
        this.tempSessionWrapper = new TimeSessionWrapper(this.tempSession);
        if (this.timeSession != null)
        {
            this.tempSession.setWorkingBegin(this.timeSession.getWorkingBegin());
            this.tempSession.setWorkingEnd(this.timeSession.getWorkingEnd());
            this.tempSession.setRestingTotal(this.timeSession.getRestingTotal());
        }
        else
        {
            this.tempSession.setWorkingBegin(new Date());
            this.tempSession.setWorkingEnd(new Date());
            this.tempSession.setRestingTotal(0L);
        }

        this.workingTime = (TextView)this.findViewById(R.id.ti_working_time);

        this.update();
        this.tfBeginItemDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                beginItemDateClicked();
            }
        });
        this.tfBeginItemTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                beginItemTimeClicked();
            }
        });
        this.tfEndItemDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                endItemDateClicked();
            }
        });
        this.tfEndItemTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                endItemTimeClicked();
            }
        });
        this.tfPauseItemTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                pauseItemTimeClicked();
            }
        });
        this.tfSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                saveButtonClicked();
            }
        });
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

    private void beginItemDateClicked ()
    {
        FragmentDatePicker datePicker = new FragmentDatePicker()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                update();
            }
        };
        datePicker.setDate(this.tempSession.getWorkingBegin());
        datePicker.show(this.getFragmentManager(), "datePicker");
    }
    private void beginItemTimeClicked ()
    {
        FragmentTimePicker timePicker = new FragmentTimePicker()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                update();
            }
        };
        timePicker.setDate(this.tempSession.getWorkingBegin());
        timePicker.show(this.getFragmentManager(), "timePicker");
    }
    private void endItemDateClicked ()
    {
        FragmentDatePicker datePicker = new FragmentDatePicker()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                update();
            }
        };
        datePicker.setDate(this.tempSession.getWorkingEnd());
        datePicker.show(this.getFragmentManager(), "datePicker");
    }
    private void endItemTimeClicked ()
    {
        FragmentTimePicker timePicker = new FragmentTimePicker()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                update();
            }
        };
        timePicker.setDate(this.tempSession.getWorkingEnd());
        timePicker.show(this.getFragmentManager(), "timePicker");
    }
    private void pauseItemTimeClicked ()
    {
        final Date pauseDate = new Date(this.tempSession.getRestingTotal());
        FragmentTimePicker timePicker = new FragmentTimePicker()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                tempSession.setRestingTotal(pauseDate.getTime());
                update();
            }
        };
        timePicker.setDate(pauseDate);
        timePicker.setTimer(true);
        timePicker.show(this.getFragmentManager(), "timePicker");
    }

    private void saveButtonClicked ()
    {
        final long workingTime = this.tempSessionWrapper.getWorkingTotalTime();
        final long restingTime = this.tempSessionWrapper.getRestingTotalTime();
        final long h09 = 32_400_000;
        final long m45 = 2_700_000;
        final long h06 = 21_600_000;
        final long m30 = 1_800_000;
        final long m01 = 60_000;

        if (workingTime > h09 && restingTime < m45)
        {
            FragmentAlert alert = new FragmentAlert()
            {
                @Override
                public void accept ()
                {
                    long temp = workingTime + restingTime - h09;
                    tempSession.setRestingTotal(Math.min(Math.max(temp, m30), m45));
                    this.dismiss();
                    saveAndFinish();
                }
                @Override
                public void cancel ()
                {
                    this.dismiss();
                    saveAndFinish();
                }
            };
            alert.setPause((restingTime / m01) + "");
            alert.setPauseNeeded("45");
            alert.show(this.getFragmentManager(), "alert");
        }
        else if (workingTime > h06 && restingTime < m30)
        {
            FragmentAlert alert = new FragmentAlert()
            {
                @Override
                public void accept ()
                {
                    long temp = workingTime + restingTime - h06;
                    tempSession.setRestingTotal(Math.min(temp, m30));
                    this.dismiss();
                    saveAndFinish();
                }
                @Override
                public void cancel ()
                {
                    this.dismiss();
                    saveAndFinish();
                }
            };
            alert.setPause((restingTime / m01) + "");
            alert.setPauseNeeded("30");
            alert.show(this.getFragmentManager(), "alert");
        }
        else
        {
            this.saveAndFinish();
        }
    }

    private void saveAndFinish()
    {
        this.saveTimeSession();
        this.finish();
    }
    private void saveTimeSession ()
    {
        this.realm.beginTransaction();

        if (this.timeSession != null)
        {
            this.timeSession.setWorkingBegin(this.tempSession.getWorkingBegin());
            this.timeSession.setWorkingEnd(this.tempSession.getWorkingEnd());
            this.timeSession.setRestingTotal(this.tempSession.getRestingTotal());
        }
        else
        {
            TimeSession timeSession = TimeSessionCreator.newTimeSession();
            timeSession.setWorkingBegin(this.tempSession.getWorkingBegin());
            timeSession.setWorkingEnd(this.tempSession.getWorkingEnd());
            timeSession.setRestingTotal(this.tempSession.getRestingTotal());
            this.realm.copyToRealm(timeSession);
        }

        this.realm.commitTransaction();
    }
    private void update ()
    {
        this.workingTime.setText(this.tempSessionWrapper.getWorkingTotalTimeString() + " h");
        this.tfBeginItemDate.setText(this.tempSessionWrapper.getWorkingBeginDateString());
        this.tfBeginItemTime.setText(this.tempSessionWrapper.getWorkingBeginDateTimeString());
        this.tfEndItemDate.setText(this.tempSessionWrapper.getWorkingEndDateString());
        this.tfEndItemTime.setText(this.tempSessionWrapper.getWorkingEndDateTimeString());
        this.tfPauseItemTime.setText(this.tempSessionWrapper.getRestingTotalTimeString());
        this.tfSaveButton.setEnabled(this.tempSessionWrapper.getWorkingTotalTime() >= 0);
    }
}
