package com.lychee.sely.adstv.launcher.detail;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.launcher.Program;

/**
 * Created by Sely on 28-Dec-16.
 */

public class ProgramDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Program p = (Program)item;

        /*viewHolder.getBody().setText("Lorem ipsum dolor sit amet, consectetur "
                + "adipisicing elit, sed do eiusmod tempor incididunt ut labore "
                + " et dolore magna aliqua. Ut enim ad minim veniam, quis "
                + "nostrud exercitation ullamco laboris nisi ut aliquip ex ea "
                + "commodo consequat.");*/
        viewHolder.getTitle().setText(p.getTitle());
        viewHolder.getSubtitle().setText(p.getDescription());

    }
}
