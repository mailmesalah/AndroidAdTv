package com.lychee.sely.adstv.scheduledadprogram.detail;

import android.content.ContentValues;
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
import com.lychee.sely.adstv.scheduledadprogram.ScheduledAdItem;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdCustomActivity;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdImageActivity;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdVideoActivity;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledDetailFragment extends DetailsFragment {
    private static final String TAG = ScheduledDetailFragment.class.getSimpleName();
    private static final int ACTION_OPEN = 1;
    private static final int ACTION_REMOVE = 2;
    private static final int ACTION_MOVE_UP = 3;
    private static final int ACTION_MOVE_DOWN = 4;

    private ArrayObjectAdapter mAdapter;
    private long mScheduledAdItemID;
    private ScheduledAdItem mScheduledAdItem;
    private long mProgramID;

    public ScheduledDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mScheduledAdItemID = getActivity().getIntent().getLongExtra("SCHEDULED_AD_ITEM_ID", 0);
        mProgramID = getActivity().getIntent().getLongExtra("PROGRAM_ID", 0);
        //Log.d(TAG,"Ad "+mScheduledAdItemID+" pcode "+mProgramID);
        mScheduledAdItem = new ScheduledAdItem();
        //populate data from database
        Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_URI, null, "_ID=?", new String[]{mScheduledAdItemID + ""}, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            mScheduledAdItem.setId(c.getLong(c.getColumnIndex("_ID")));
            mScheduledAdItem.setProgramCode(c.getLong(c.getColumnIndex("ProgramCode")));
            mScheduledAdItem.setAdType(c.getInt(c.getColumnIndex("AdType")));
            mScheduledAdItem.setAdCode(c.getLong(c.getColumnIndex("AdCode")));
            mScheduledAdItem.setAdName(c.getString(c.getColumnIndex("AdName")));
            mScheduledAdItem.setAdPath(c.getString(c.getColumnIndex("AdPath")));


            c.close();
        }

        setupFragment();
    }

    private void setupFragment() {
        final ClassPresenterSelector selector = new ClassPresenterSelector();
        // Attach your media item details presenter to the row presenter:
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(new ScheduledDetailPresenter());
        rowPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_grid_item_background));
        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START);
        //action listener
        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_OPEN) {
                    if(mScheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_IMAGE) {
                        Intent intent = new Intent(getActivity(), ScheduledAdImageActivity.class);
                        // pass the item information
                        intent.putExtra("SCHEDULED_AD_ITEM_ID", mScheduledAdItem.getId());
                        intent.putExtra("AD_ID", mScheduledAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }else if(mScheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_VIDEO) {
                        Intent intent = new Intent(getActivity(), ScheduledAdVideoActivity.class);
                        // pass the item information
                        intent.putExtra("SCHEDULED_AD_ITEM_ID", mScheduledAdItem.getId());
                        intent.putExtra("AD_ID", mScheduledAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }else if(mScheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_CUSTOM) {
                        Intent intent = new Intent(getActivity(), ScheduledAdCustomActivity.class);
                        // pass the item information
                        intent.putExtra("SCHEDULED_AD_ITEM_ID", mScheduledAdItem.getId());
                        intent.putExtra("AD_ID", mScheduledAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }


                } else if (action.getId() == ACTION_REMOVE) {
                    //Remove Scheduled Ad item
                    getActivity().getContentResolver().delete(ProgramAdContentProvider.SCHEDULED_AD_URI,"_ID=?", new String[]{mScheduledAdItemID+""});
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
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mScheduledAdItem);
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(ACTION_OPEN, new Action(ACTION_OPEN, "Open"));
        adapter.set(ACTION_REMOVE, new Action(ACTION_REMOVE, "Remove"));
        detailsOverview.setActionsAdapter(adapter);

        mAdapter.add(detailsOverview);
        //Provide Scheduled Ad Item
        mAdapter.add(mScheduledAdItem);

    }


}
