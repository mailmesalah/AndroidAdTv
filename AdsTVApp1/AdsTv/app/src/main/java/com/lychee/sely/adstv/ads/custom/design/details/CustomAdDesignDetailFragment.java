package com.lychee.sely.adstv.ads.custom.design.details;

import android.content.ContentValues;
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
import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.ads.custom.details.CustomAdDetailPresenter;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 05-Jun-17.
 */

public class CustomAdDesignDetailFragment extends DetailsFragment {
    private static final String TAG = CustomAdDesignDetailFragment.class.getSimpleName();
    private static final int ACTION_MOVE_UP= 1;
    private static final int ACTION_MOVE_DOWN = 2;
    private static final int ACTION_REMOVE = 3;

    private ArrayObjectAdapter mAdapter;
    private long mAdID;
    private CustomAdItem mCustomAdItem;
    private long mAdItemID;

    public CustomAdDesignDetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mAdID=getActivity().getIntent().getLongExtra("AD_ID",0);
        mAdItemID=getActivity().getIntent().getLongExtra("AD_ITEM_ID",0);
        mCustomAdItem =new CustomAdItem();

        Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=? and _ID=?", new String[]{mAdID + "", mAdItemID+""}, null);
        if(c!=null&&c.getCount()>0){
            c.moveToFirst();
            mCustomAdItem.setId(mAdItemID);
            mCustomAdItem.setSerialNo(c.getInt(c.getColumnIndex("ZOrder")));
            mCustomAdItem.setAdName(c.getString(c.getColumnIndex("AdName")));
            c.close();
        }

        setupFragment();
    }

    private void setupFragment(){
        ClassPresenterSelector selector = new ClassPresenterSelector();
        // Attach your media item details presenter to the row presenter:
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(new CustomAdDesignDetailPresenter());
        rowPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_grid_item_background));
        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START);
        //action listener
        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_REMOVE) {
                    //Remove Stack Ad item
                    getActivity().getContentResolver().delete(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI,"_ID=?", new String[]{mAdItemID+""});
                    //Update All other Serial Nos
                    int serialNo = mCustomAdItem.getSerialNo();
                    ContentValues cv = new ContentValues();
                    cv.put("ZOrder",serialNo);
                    while(getActivity().getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "AdId=? And ZOrder=?", new String[]{mAdID+"",(serialNo+1) + ""})>0){
                        ++serialNo;
                        cv.put("ZOrder",serialNo);
                    }
                    //Close the activity
                    getActivity().finish();

                } else if (action.getId() == ACTION_MOVE_UP) {
                    //update serial nos
                    if(mCustomAdItem.getSerialNo()>1) {
                        ContentValues cv = new ContentValues();
                        cv.put("ZOrder", mCustomAdItem.getSerialNo());
                        getActivity().getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "AdId=? And ZOrder=?", new String[]{mAdID+"",(mCustomAdItem.getSerialNo() - 1) + ""});
                        cv.put("ZOrder", mCustomAdItem.getSerialNo() - 1);
                        getActivity().getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "_ID=?", new String[]{mCustomAdItem.getId() + ""});
                        //Close the activity
                        getActivity().finish();
                    }

                } else if (action.getId() == ACTION_MOVE_DOWN) {
                    //update serial nos
                    Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{mAdID+""}, null);
                    if(c!=null){
                        int count = c.getCount();
                        c.close();

                        if(count>mCustomAdItem.getSerialNo()){
                            ContentValues cv = new ContentValues();
                            cv.put("ZOrder", mCustomAdItem.getSerialNo());
                            getActivity().getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "AdId=? And ZOrder=?", new String[]{mAdID+"",(mCustomAdItem.getSerialNo() + 1) + ""});
                            cv.put("ZOrder", mCustomAdItem.getSerialNo() + 1);
                            getActivity().getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "_ID=?", new String[]{mCustomAdItem.getId() + ""});
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
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mCustomAdItem);
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(ACTION_REMOVE, new Action(ACTION_REMOVE, "Remove"));
        adapter.set(ACTION_MOVE_UP, new Action(ACTION_MOVE_UP, "Move Up"));
        adapter.set(ACTION_MOVE_DOWN, new Action(ACTION_MOVE_DOWN, "Move Down"));
        detailsOverview.setActionsAdapter(adapter);

        mAdapter.add(detailsOverview);

    }


}
