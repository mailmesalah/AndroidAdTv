package com.lychee.sely.adstv.launcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lychee.sely.adstv.R;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdProgramCardPresenter extends Presenter {

    private static final String TAG = ScheduledAdProgramCardPresenter.class.getSimpleName();

    private static Context mContext;
    private static int CARD_WIDTH = 300;
    private static int CARD_HEIGHT = 176;

    static class ViewHolder extends Presenter.ViewHolder {
        private Program mProgram;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            //mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setProgram(Program m) {
            mProgram = m;
        }

        public Program getProgram() {
            return mProgram;
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
        mContext = parent.getContext();

        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.selected_grid_item_background));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Program program = (Program) item;
        ((ViewHolder) viewHolder).setProgram(program);

        ((ViewHolder) viewHolder).mCardView.setTitleText(program.getTitle());
        ((ViewHolder) viewHolder).mCardView.setContentText(program.getDescription());
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        //((ViewHolder) viewHolder).mCardView.setMainImage(((ViewHolder) viewHolder).getDefaultCardImage());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
        // TO DO
    }

}