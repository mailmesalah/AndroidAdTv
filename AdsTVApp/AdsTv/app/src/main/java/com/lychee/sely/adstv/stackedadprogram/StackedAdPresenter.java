package com.lychee.sely.adstv.stackedadprogram;

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
import com.lychee.sely.adstv.storage.tables.StackedAd;

/**
 * Created by Sely on 26-Dec-16.
 */

public class StackedAdPresenter extends Presenter {

    private static final String TAG = StackedAdPresenter.class.getSimpleName();

    private static Context mContext;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    static class ViewHolder extends Presenter.ViewHolder {
        private StackedAdItem mStackedAdItem;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            //mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setStackedAd(StackedAdItem ad) {
            mStackedAdItem = ad;
        }

        public StackedAdItem getStackedAd() {
            return mStackedAdItem;
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
        StackedAdItem stackedAdItem = (StackedAdItem) item;
        ((ViewHolder) viewHolder).setStackedAd(stackedAdItem);

        Log.d(TAG, "onBindViewHolder "+ stackedAdItem.getAdTimePeriod());
        ((ViewHolder) viewHolder).mCardView.setTitleText(stackedAdItem.getSerialNo()+". "+ stackedAdItem.getAdName());
        ((ViewHolder) viewHolder).mCardView.setContentText("Time : "+FileOperation.getHoursFromTimePeriod(stackedAdItem.getAdTimePeriod())+":"+FileOperation.getMinutesFromTimePeriod(stackedAdItem.getAdTimePeriod())+":"+FileOperation.getSecondsFromTimePeriod(stackedAdItem.getAdTimePeriod()));
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        if(stackedAdItem.getAdType()== StackedAd.AD_TYPE_IMAGE) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.imgDrawableFromFile(Resources.getSystem(), stackedAdItem.getAdPath()));
        }else if(stackedAdItem.getAdType()== StackedAd.AD_TYPE_VIDEO) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.getThumbnailFromVideoFile(Resources.getSystem(), stackedAdItem.getAdPath()));
        }else if(stackedAdItem.getAdType()== StackedAd.AD_TYPE_CUSTOM) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(null);
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