package com.lychee.sely.adstv.ads.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.Ad;
import com.lychee.sely.adstv.extras.FileOperation;

/**
 * Created by Sely on 18-May-17.
 */

public class CustomAdPresenter extends Presenter {

    private static final String TAG = CustomAdPresenter.class.getSimpleName();

    private static Context mContext;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    static class ViewHolder extends Presenter.ViewHolder {
        private Ad mAd;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            //mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setAd(Ad m) {
            mAd = m;
        }

        public Ad getAd() {
            return mAd;
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
        Ad ad = (Ad) item;
        ((ViewHolder) viewHolder).setAd(ad);
        ((ViewHolder) viewHolder).mCardView.setTitleText(ad.getAdName()+"");
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        //((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.imgDrawableFromFile(Resources.getSystem(),ad.getAdPath()));
        Log.d(TAG, "onBindViewHolder "+ad.getAdPath()+"Custom d");
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