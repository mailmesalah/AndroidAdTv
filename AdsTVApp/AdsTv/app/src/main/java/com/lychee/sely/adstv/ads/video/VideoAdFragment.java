package com.lychee.sely.adstv.ads.video;

import android.app.Activity;
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

import com.lychee.sely.adstv.ads.Ad;
import com.lychee.sely.adstv.ads.video.details.VideoAdDetailActivity;
import com.lychee.sely.adstv.extras.LoadersID;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 23-Dec-16.
 */

public class VideoAdFragment extends VerticalGridFragment{
    private static final String TAG = VideoAdFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;
    public static final int AD_SELECTED = 5;

    private CursorObjectAdapter mAdAdapter;

    public VideoAdFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setupFragment();
        setupEventListeners();

        ((VideoAdActivity)getActivity()).getSupportLoaderManager().initLoader(LoadersID.VIDEO_AD_FRAGMENT_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity().getBaseContext(), ProgramAdContentProvider.VIDEO_AD_URI, null, null, null, null);
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
        mAdAdapter = new CursorObjectAdapter(new VideoAdPresenter());
        setAdapter(mAdAdapter);
        mAdAdapter.setMapper(new VideoAdCursorMapper());
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent = new Intent(getActivity(), VideoAdDetailActivity.class);
                // pass the item information
                Ad p = (Ad)item;
                intent.putExtra("AD_ID", p.getId());
                Log.d(TAG,"Ad_id "+p.getId());
                startActivityForResult(intent,AD_SELECTED);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== VideoAdFragment.AD_SELECTED && resultCode == Activity.RESULT_OK){
            //Forward the Ad id to the StackedVideoAdActivity or ScheduledVideoAdActivity
            Intent i =  new Intent();
            i.putExtra("AD_ID",data.getLongExtra("AD_ID",0));
            getActivity().setResult(Activity.RESULT_OK,i);
            //Close this detail window
            getActivity().finish();
        }
    }
}
