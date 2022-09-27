package com.lychee.sely.adstv.scheduledadprogram;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.stackedadprogram.StackedAdPresenter;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.StackedAd;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdPresenter extends Presenter {

    private static final String TAG = ScheduledAdPresenter.class.getSimpleName();

    private static Context mContext;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    static class ViewHolder extends Presenter.ViewHolder {
        private ScheduledAdItem mScheduledAdItem;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            //mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setScheduledAd(ScheduledAdItem ad) {
            mScheduledAdItem = ad;
        }

        public ScheduledAdItem getScheduledAd() {
            return mScheduledAdItem;
        }

        public ImageCardView getCardView() {
            return mCardView;
        }

        public Drawable getDefaultCardImage() {
            return mDefaultCardImage;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");
        mContext = parent.getContext();

        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.selected_grid_item_background));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        ScheduledAdItem scheduledAdItem = (ScheduledAdItem) item;
        Log.d("Scheduled item",scheduledAdItem.getAdPath());
        ((ViewHolder) viewHolder).setScheduledAd(scheduledAdItem);
        ((ViewHolder) viewHolder).mCardView.setTitleText(scheduledAdItem.getAdName()+"");
        //Date time Showing
        Date dateTime = FileOperation.convertStringToDateTime(scheduledAdItem.getAdDateTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);

        ((ViewHolder) viewHolder).mCardView.setContentText(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a",cal));
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        if(scheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_IMAGE) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.imgDrawableFromFile(Resources.getSystem(), scheduledAdItem.getAdPath()));
        }else if(scheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_VIDEO) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.getThumbnailFromVideoFile(Resources.getSystem(), scheduledAdItem.getAdPath()));
        }else if(scheduledAdItem.getAdType()== ScheduledAd.AD_TYPE_CUSTOM) {
            ((ScheduledAdPresenter.ViewHolder) viewHolder).mCardView.setMainImage(null);
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
        // TO DO
    }

}