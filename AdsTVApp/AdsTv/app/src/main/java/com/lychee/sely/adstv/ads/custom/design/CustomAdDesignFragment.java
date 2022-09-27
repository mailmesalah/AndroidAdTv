package com.lychee.sely.adstv.ads.custom.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.ads.custom.design.details.CustomAdDesignDetailActivity;
import com.lychee.sely.adstv.ads.custom.details.CustomAdDetailActivity;
import com.lychee.sely.adstv.extras.LoadersID;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 03-Jun-17.
 */

public class CustomAdDesignFragment extends VerticalGridFragment{
    private static final String TAG = CustomAdDesignFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;

    private CursorObjectAdapter mAdAdapter;
    private long mAdID;

    protected DataReceiver dataReceiver;
    public static final String REC_DATA = "REC_DATA";
    private LoaderManager.LoaderCallbacks<Cursor> loaderManager;

    public CustomAdDesignFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mAdID=getActivity().getIntent().getLongExtra("AD_ID",0);

        //Broadcast receiver for Custom Ad id
        dataReceiver = new DataReceiver();
        IntentFilter intentFilter = new IntentFilter(REC_DATA);
        getActivity().registerReceiver(dataReceiver, intentFilter);

        setupFragment();
        setupEventListeners();

        loaderManager = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                ((CustomAdDesignActivity)getActivity()).updateAdItemMaps();
                return new CursorLoader(getActivity().getBaseContext(), ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{"" + mAdID}, "ZOrder");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ((CustomAdDesignActivity)getActivity()).updateAdItemMaps();
                mAdAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdAdapter.swapCursor(null);
            }

        };

        ((CustomAdDesignActivity) getActivity()).getSupportLoaderManager().initLoader(LoadersID.CUSTOM_AD_DESIGN_FRAGMENT_ID, null, loaderManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregister the broadcast sender
        getActivity().unregisterReceiver(dataReceiver);
    }
    private void setupFragment() {
        mAdAdapter = new CursorObjectAdapter(new CustomAdDesignPresenter());
        setAdapter(mAdAdapter);
        mAdAdapter.setMapper(new CustomAdDesignCursorMapper());
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent = new Intent(getActivity(), CustomAdDesignDetailActivity.class);
                // pass the item information
                CustomAdItem cai= (CustomAdItem) item;
                intent.putExtra("AD_ITEM_ID", cai.getId());
                intent.putExtra("AD_ID", mAdID);
                startActivity(intent);

            }
        });
    }

    private class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            mAdID = intent.getLongExtra("AD_ID",0);

            ((CustomAdDesignActivity)getActivity()).getSupportLoaderManager().restartLoader(LoadersID.CUSTOM_AD_DESIGN_FRAGMENT_ID,null,loaderManager);

        }
    }
}
