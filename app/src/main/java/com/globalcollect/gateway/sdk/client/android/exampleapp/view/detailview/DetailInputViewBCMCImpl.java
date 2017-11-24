package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;

/**
 * View for the DetailInputActivity with added functionality for the BCMC payment product
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputViewBCMCImpl extends  DetailInputViewImpl implements DetailInputViewBCMC {

    public DetailInputViewBCMCImpl(Activity activity, @IdRes int id) {
        super (activity, id);
    }

    @Override
    public void renderBCMCIntroduction(Bitmap qrCode) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        View bcmcLayout = inflater.inflate(R.layout.layout_bcmc, null);
        ImageView qrCodeView = (ImageView) bcmcLayout.findViewById(R.id.qrcode);
        if (qrCodeView != null) {
            qrCodeView.setImageBitmap(qrCode);
        }
        renderInputFieldsLayout.addView(bcmcLayout, 0);
    }
}
