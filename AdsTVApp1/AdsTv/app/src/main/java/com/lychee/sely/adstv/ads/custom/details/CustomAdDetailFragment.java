package com.lychee.sely.adstv.ads.custom.details;

import android.app.Activity;
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
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.Ad;
import com.lychee.sely.adstv.ads.custom.design.CustomAdDesignActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 18-May-17.
 */

public class CustomAdDetailFragment extends DetailsFragment {
    private static final String TAG = CustomAdDetailFragment.class.getSimpleName();
    private static final int ACTION_SELECT= 1;
    private static final int ACTION_OPEN = 2;
    private static final int ACTION_REMOVE = 3;

    private ArrayObjectAdapter mAdapter;
    private long mAdID;
    private Ad mAd;

    public CustomAdDetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mAdID=getActivity().getIntent().getLongExtra("AD_ID",0);
        mAd=new Ad();
        Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.CUSTOM_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
        if(c!=null&&c.getCount()>0){
            c.moveToFirst();
            mAd.setId(mAdID);
            mAd.setAdName(c.getString(c.getColumnIndex("AdName")));
            c.close();
        }

        setupFragment();
    }

    private void setupFragment(){
        ClassPresenterSelector selector = new ClassPresenterSelector();
        // Attach your media item details presenter to the row presenter:
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(new CustomAdDetailPresenter());
        rowPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_grid_item_background));
        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START);
        //action listener
        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if(action.getId()==ACTION_SELECT){
                    Log.i(TAG, "SELECT");
                    //Select Ad id and return to the VideoAdActivity
                    Intent i =  new Intent();
                    i.putExtra("AD_ID",mAdID);
                    getActivity().setResult(Activity.RESULT_OK,i);
                    //Close this detail window
                    getActivity().finish();
                } if(action.getId()==ACTION_OPEN){
                    Log.i(TAG, "OPEN");
                    Intent i = new Intent(getActivity(), CustomAdDesignActivity.class);
                    i.putExtra("AD_ID",mAdID);
                    startActivity(i);
                    //close this detail window
                    getActivity().finish();
                } else if(action.getId()==ACTION_REMOVE){
                    Log.i(TAG, "REMOVE");
                    //Remove if the ad is not used in programs
                    Cursor c = getActivity().getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "AdCode=?", new String[]{mAdID + ""}, null);
                    if(c!=null && c.getCount()>0){
                        //shouldnt delete
                        c.close();
                        Toast.makeText(getActivity(),"The Ad is already Used, Please Remove the program first",Toast.LENGTH_LONG).show();
                    }else{
                        getActivity().getContentResolver().delete(ProgramAdContentProvider.IMAGE_AD_URI,"_ID=?",new String[]{mAdID+""});
                    }

                    //close this detail window
                    getActivity().finish();
                }
            }
        });


        selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        rowPresenter.setActionsBackgroundColor(ContextCompat.getColor(getActivity(), R.color.secondary_background));
        mAdapter = new ArrayObjectAdapter(selector);

        setAdapter(mAdapter);

        Resources res = getActivity().getResources();
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mAd);
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(ACTION_SELECT, new Action(ACTION_SELECT, "Select"));
        adapter.set(ACTION_OPEN, new Action(ACTION_OPEN, "Open"));
        adapter.set(ACTION_REMOVE, new Action(ACTION_REMOVE, "Remove"));
        detailsOverview.setActionsAdapter(adapter);

        mAdapter.add(detailsOverview);

    }


}
