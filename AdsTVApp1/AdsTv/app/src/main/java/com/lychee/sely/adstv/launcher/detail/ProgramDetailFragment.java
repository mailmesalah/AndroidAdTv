package com.lychee.sely.adstv.launcher.detail;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.launcher.Program;
import com.lychee.sely.adstv.playermanager.PlayerActivity;
import com.lychee.sely.adstv.scheduledadprogram.ScheduledAdProgramActivity;
import com.lychee.sely.adstv.stackedadprogram.StackedAdProgramActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 27-Dec-16.
 */

public class ProgramDetailFragment extends DetailsFragment {
    private static final String TAG = ProgramDetailFragment.class.getSimpleName();
    private static final int ACTION_PLAY = 1;
    private static final int ACTION_OPEN = 2;
    private static final int ACTION_DELETE = 3;

    private ArrayObjectAdapter mAdapter;
    private long mProgramID;
    private Program mProgram;
    private int mProgramType;

    public ProgramDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mProgramID = getActivity().getIntent().getLongExtra("PROGRAM_ID", 0);
        mProgramType = getActivity().getIntent().getIntExtra("PROGRAM_TYPE", 0);
        //Populate Program object from database
        mProgram = new Program();
        if (mProgramType == Program.STACKED) {
            Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, null, "_ID=?", new String[]{mProgramID + ""}, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                mProgram.setId(mProgramID);
                mProgram.setTitle(c.getString(c.getColumnIndex("ProgramName")));
                mProgram.setType(Program.STACKED);
                mProgram.setDescription("Descritpion");
                mProgram.setRepeat(c.getInt(c.getColumnIndex("Repeat")) == 1);
                c.close();
            }
        } else if (mProgramType == Program.SCHEDULED) {
            Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, null, "_ID=?", new String[]{mProgramID + ""}, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                mProgram.setId(mProgramID);
                mProgram.setTitle(c.getString(c.getColumnIndex("ProgramName")));
                mProgram.setType(Program.SCHEDULED);
                mProgram.setDescription("Descritpion");
                mProgram.setRepeat(c.getInt(c.getColumnIndex("Repeat")) == 1);
                c.close();
            }
        }
        setupFragment();
    }

    private void setupFragment() {
        ClassPresenterSelector selector = new ClassPresenterSelector();
        // Attach your media item details presenter to the row presenter:
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(new ProgramDetailPresenter());
        rowPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_grid_item_background));
        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START);

        //action listener
        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_PLAY) {
                    //Run program///....................
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra("PROGRAM_TYPE", mProgram.getType());
                    intent.putExtra("PROGRAM_ID", mProgramID);
                    startActivity(intent);
                } else if (action.getId() == ACTION_OPEN) {
                    //Open the Stacked Ad program or Scheduled ad program depending on the type
                    Intent intent = mProgramType == Program.SCHEDULED ? new Intent(getActivity(), ScheduledAdProgramActivity.class) : new Intent(getActivity(), StackedAdProgramActivity.class);
                    intent.putExtra("PROGRAM_ID", mProgramID);
                    startActivity(intent);
                    //Close the activity
                    getActivity().finish();
                } else if (action.getId() == ACTION_DELETE) {
                    //Delete the program and its detailed datas
                    getActivity().getContentResolver().delete(mProgramType == Program.SCHEDULED ?ProgramAdContentProvider.SCHEDULED_AD_URI:ProgramAdContentProvider.STACKED_AD_URI, "ProgramCode=?", new String[]{mProgramID + ""});
                    getActivity().getContentResolver().delete(mProgramType == Program.SCHEDULED ?ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI:ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, "_ID=?", new String[]{mProgramID + ""});
                    //Close the activity
                    getActivity().finish();
                }
            }
        });


        selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        rowPresenter.setActionsBackgroundColor(ContextCompat.getColor(getActivity(), R.color.secondary_background));
        mAdapter = new ArrayObjectAdapter(selector);

        setAdapter(mAdapter);

        Resources res = getActivity().getResources();
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mProgram);
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(ACTION_PLAY, new Action(ACTION_PLAY, "Play"));
        adapter.set(ACTION_OPEN, new Action(ACTION_OPEN, "Open"));
        adapter.set(ACTION_DELETE, new Action(ACTION_DELETE, "Delete"));
        detailsOverview.setActionsAdapter(adapter);

        mAdapter.add(detailsOverview);

    }


}
