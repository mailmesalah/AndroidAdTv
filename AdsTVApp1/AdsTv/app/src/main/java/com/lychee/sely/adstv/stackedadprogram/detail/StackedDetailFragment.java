package com.lychee.sely.adstv.stackedadprogram.detail;

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
import com.lychee.sely.adstv.stackedadprogram.StackedAdItem;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdCustomActivity;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdImageActivity;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdVideoActivity;
import com.lychee.sely.adstv.storage.tables.StackedAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 14-Jan-17.
 */

public class StackedDetailFragment extends DetailsFragment {
    private static final String TAG = StackedDetailFragment.class.getSimpleName();
    private static final int ACTION_OPEN = 1;
    private static final int ACTION_REMOVE = 2;
    private static final int ACTION_MOVE_UP = 3;
    private static final int ACTION_MOVE_DOWN = 4;

    private ArrayObjectAdapter mAdapter;
    private long mStackedAdItemID;
    private StackedAdItem mStackedAdItem;
    private long mProgramID;

    public StackedDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mStackedAdItemID = getActivity().getIntent().getLongExtra("STACKED_AD_ITEM_ID", 0);
        mProgramID = getActivity().getIntent().getLongExtra("PROGRAM_ID", 0);
        //Log.d(TAG,"Ad "+mStackedAdItemID+" pcode "+mProgramID);
        mStackedAdItem= new StackedAdItem();
        //populate data from database
        Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "_ID=?", new String[]{mStackedAdItemID + ""}, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            mStackedAdItem.setId(c.getLong(c.getColumnIndex("_ID")));
            mStackedAdItem.setSerialNo(c.getInt(c.getColumnIndex("SerialNo")));
            mStackedAdItem.setProgramCode(c.getLong(c.getColumnIndex("ProgramCode")));
            mStackedAdItem.setAdType(c.getInt(c.getColumnIndex("AdType")));
            mStackedAdItem.setAdCode(c.getLong(c.getColumnIndex("AdCode")));
            mStackedAdItem.setAdName(c.getString(c.getColumnIndex("AdName")));
            mStackedAdItem.setAdPath(c.getString(c.getColumnIndex("AdPath")));
            mStackedAdItem.setAdTimePeriod(c.getInt(c.getColumnIndex("AdTimePeriod")));

            c.close();
        }

        setupFragment();
    }

    private void setupFragment() {
        final ClassPresenterSelector selector = new ClassPresenterSelector();
        // Attach your media item details presenter to the row presenter:
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(new StackedDetailPresenter());
        rowPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_grid_item_background));
        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START);
        //action listener
        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_OPEN) {
                    if(mStackedAdItem.getAdType()== StackedAd.AD_TYPE_IMAGE) {
                        Intent intent = new Intent(getActivity(), StackedAdImageActivity.class);
                        // pass the item information
                        intent.putExtra("STACKED_AD_ITEM_ID", mStackedAdItem.getId());
                        intent.putExtra("AD_ID", mStackedAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        intent.putExtra("SERIAL_NO", mStackedAdItem.getSerialNo());
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }else if(mStackedAdItem.getAdType()== StackedAd.AD_TYPE_VIDEO) {
                        Intent intent = new Intent(getActivity(), StackedAdVideoActivity.class);
                        // pass the item information
                        intent.putExtra("STACKED_AD_ITEM_ID", mStackedAdItem.getId());
                        intent.putExtra("AD_ID", mStackedAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        intent.putExtra("SERIAL_NO", mStackedAdItem.getSerialNo());
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }else if(mStackedAdItem.getAdType()== StackedAd.AD_TYPE_CUSTOM) {
                        Intent intent = new Intent(getActivity(), StackedAdCustomActivity.class);
                        // pass the item information
                        intent.putExtra("STACKED_AD_ITEM_ID", mStackedAdItem.getId());
                        intent.putExtra("AD_ID", mStackedAdItem.getAdCode());
                        intent.putExtra("PROGRAM_ID", mProgramID);
                        intent.putExtra("SERIAL_NO", mStackedAdItem.getSerialNo());
                        startActivity(intent);
                        //close this activity
                        getActivity().finish();
                    }

                } else if (action.getId() == ACTION_REMOVE) {
                    //Remove Stack Ad item
                    getActivity().getContentResolver().delete(ProgramAdContentProvider.STACKED_AD_URI,"_ID=?", new String[]{mStackedAdItemID+""});
                    //Update All other Serial Nos
                    int serialNo = mStackedAdItem.getSerialNo();
                    ContentValues cv = new ContentValues();
                    cv.put("SerialNo",serialNo);
                    while(getActivity().getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI, cv, "ProgramCode=? And SerialNo=?", new String[]{mProgramID+"",(serialNo+1) + ""})>0){
                        ++serialNo;
                        cv.put("SerialNo",serialNo);
                    }
                    //Close the activity
                    getActivity().finish();

                } else if (action.getId() == ACTION_MOVE_UP) {
                    //update serial nos
                    if(mStackedAdItem.getSerialNo()>1) {
                        ContentValues cv = new ContentValues();
                        cv.put("SerialNo", mStackedAdItem.getSerialNo());
                        getActivity().getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI, cv, "ProgramCode=? And SerialNo=?", new String[]{mProgramID+"",(mStackedAdItem.getSerialNo() - 1) + ""});
                        cv.put("SerialNo", mStackedAdItem.getSerialNo() - 1);
                        getActivity().getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI, cv, "_ID=?", new String[]{mStackedAdItem.getId() + ""});
                        //Close the activity
                        getActivity().finish();
                    }

                } else if (action.getId() == ACTION_MOVE_DOWN) {
                    //update serial nos
                    Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "ProgramCode=?", new String[]{mProgramID+""}, null);
                    if(c!=null){
                        int count = c.getCount();
                        c.close();

                        if(count>mStackedAdItem.getSerialNo()){
                            ContentValues cv = new ContentValues();
                            cv.put("SerialNo", mStackedAdItem.getSerialNo());
                            getActivity().getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI, cv, "ProgramCode=? And SerialNo=?", new String[]{mProgramID+"",(mStackedAdItem.getSerialNo() + 1) + ""});
                            cv.put("SerialNo", mStackedAdItem.getSerialNo() + 1);
                            getActivity().getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI, cv, "_ID=?", new String[]{mStackedAdItem.getId() + ""});
                            //Close the activity
                            getActivity().finish();
                        }
                    }
                }
            }
        });


        selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        rowPresenter.setActionsBackgroundColor(ContextCompat.getColor(getActivity(), R.color.secondary_background));
        mAdapter = new ArrayObjectAdapter(selector);

        setAdapter(mAdapter);

        Resources res = getActivity().getResources();
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mStackedAdItem);
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(ACTION_OPEN, new Action(ACTION_OPEN, "Open"));
        adapter.set(ACTION_REMOVE, new Action(ACTION_REMOVE, "Remove"));
        adapter.set(ACTION_MOVE_UP, new Action(ACTION_MOVE_UP, "Move Up"));
        adapter.set(ACTION_MOVE_DOWN, new Action(ACTION_MOVE_DOWN, "Move Down"));
        detailsOverview.setActionsAdapter(adapter);

        mAdapter.add(detailsOverview);
        //Provide Stacked Ad Item
        mAdapter.add(mStackedAdItem);

    }


}
