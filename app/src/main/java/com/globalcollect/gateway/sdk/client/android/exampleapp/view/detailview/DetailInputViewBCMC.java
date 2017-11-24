package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.graphics.Bitmap;

/**
 * Interface for a DetailInputView, that has extra functionality for rendering the BCMC
 * payment product
 *
 * Copyright 2017 Global Collect Services B.V
 */
public interface DetailInputViewBCMC extends DetailInputView {


    void renderBCMCIntroduction(Bitmap qrCode);
}
