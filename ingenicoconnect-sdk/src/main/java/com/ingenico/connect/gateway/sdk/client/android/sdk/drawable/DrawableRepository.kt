/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.drawable

import android.graphics.drawable.Drawable
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import io.reactivex.rxjava3.core.Observable

internal interface DrawableRepository {

    fun getDrawableFromUrl(connectSDKConfiguration: ConnectSDKConfiguration, drawableUrl: String): Observable<Drawable>
}
