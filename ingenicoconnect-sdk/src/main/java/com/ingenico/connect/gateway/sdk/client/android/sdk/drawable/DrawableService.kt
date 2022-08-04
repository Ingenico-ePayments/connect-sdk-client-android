/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.drawable

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

internal interface DrawableService {

    @GET("{drawableUrl}")
    fun getDrawableFromUrl(@Path("drawableUrl",encoded = true) drawableUrl: String): Observable<ResponseBody>
}
