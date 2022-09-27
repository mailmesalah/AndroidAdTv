package com.lychee.sely.adstv.scheduledadprogram;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.CursorObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.lychee.sely.adstv.extras.LoadersID;
import com.lychee.sely.adstv.scheduledadprogram.detail.ScheduledDetailActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdFragment extends VerticalGridFragment{
    private static final String TAG = ScheduledAdFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;

    private CursorObjectAdapter mAdAdapter;

    private long mProgramID;

    public ScheduledAdFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgramID=getActivity().getIntent().getLongExtra("PROGRAM_ID",0);

        setupFragment();
        setupEventListeners();

        ((ScheduledAdProgramActivity)getActivity()).getSupportLoaderManager().initLoader(LoadersID.SCHEDULED_AD_FRAGMENT_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity().getBaseContext(), ProgramAdContentProvider.SCHEDULED_AD_URI, null, "ProgramCode=?", new String[]{mProgramID+""}, "AdDateTime");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdAdapter.swapCursor(null);
            }

        });
    }


    private void setupFragment() {
        mAdAdapter = new CursorObjectAdapter(new ScheduledAdPresenter());
        setAdapter(mAdAdapter);
        mAdAdapter.setMapper(new ScheduledAdCursorMapper());
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent = new Intent(getActivity(), ScheduledDetailActivity.class);
                // pass the item information
                ScheduledAdItem sa = (ScheduledAdItem) item;
                intent.putExtra("SCHEDULED_AD_ITEM_ID", sa.getId());
                intent.putExtra("PROGRAM_ID", mProgramID);
                startActivity(intent);
            }
        });
    }
}
