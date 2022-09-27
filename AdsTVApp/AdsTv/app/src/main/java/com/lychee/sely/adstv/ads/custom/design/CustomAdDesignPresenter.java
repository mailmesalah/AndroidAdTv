package com.lychee.sely.adstv.ads.custom.design;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.extras.FileOperation;

/**
 * Created by Sely on 03-Jun-17.
 */

public class CustomAdDesignPresenter extends Presenter {

    private static final String TAG = CustomAdDesignPresenter.class.getSimpleName();

    private static Context mContext;
    private static int CARD_WIDTH = 200;
    private static int CARD_HEIGHT = 100;

    static class ViewHolder extends Presenter.ViewHolder {
        private CustomAdItem mAd;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            //mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public CustomAdItem getAd() {
            return mAd;
        }

        public void setAd(CustomAdItem mAd) {
            this.mAd = mAd;
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
        CustomAdItem ad = (CustomAdItem) item;
        ((ViewHolder) viewHolder).setAd(ad);
        ((ViewHolder) viewHolder).mCardView.setTitleText(ad.getSerialNo()+". "+ad.getAdName());
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        if(ad.getAdType()==CustomAdItem.IMAGE_AD) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.imgDrawableFromFile(Resources.getSystem(), ad.getAdPath()));
        }else if(ad.getAdType()==CustomAdItem.VIDEO_AD) {
            ((ViewHolder) viewHolder).mCardView.setMainImage(FileOperation.getThumbnailFromVideoFile(Resources.getSystem(), ad.getAdPath()));
        }
        Log.d(TAG, "onBindViewHolder "+ad.getAdName()+"Custom Detail Ad");
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