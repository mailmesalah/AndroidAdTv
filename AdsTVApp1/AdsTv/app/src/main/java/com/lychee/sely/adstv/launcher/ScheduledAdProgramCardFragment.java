package com.lychee.sely.adstv.launcher;

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
import com.lychee.sely.adstv.launcher.detail.ProgramDetailActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdProgramCardFragment extends VerticalGridFragment{
    private static final String TAG = ScheduledAdProgramCardFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;

    private CursorObjectAdapter mProgramAdapter;

    public ScheduledAdProgramCardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFragment();
        setupEventListeners();

        ((DashboardActivity)getActivity()).getSupportLoaderManager().initLoader(LoadersID.SCHEDULED_AD_PROGRAM_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Log.d("OnCreateLoader1","Loader Calls");
                return new CursorLoader(getActivity().getBaseContext(), ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mProgramAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mProgramAdapter.swapCursor(null);
            }

        });
    }


    private void setupFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        mProgramAdapter = new CursorObjectAdapter(new ScheduledAdProgramCardPresenter());
        setAdapter(mProgramAdapter);
        mProgramAdapter.setMapper(new ScheduledAdProgramCardCursorMapper());
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent = new Intent(getActivity(), ProgramDetailActivity.class);
                // pass the item information
                Program p = (Program)item;
                intent.putExtra("PROGRAM_ID", p.getId());
                intent.putExtra("PROGRAM_TYPE", Program.SCHEDULED);
                startActivity(intent);
            }
        });
    }


}
