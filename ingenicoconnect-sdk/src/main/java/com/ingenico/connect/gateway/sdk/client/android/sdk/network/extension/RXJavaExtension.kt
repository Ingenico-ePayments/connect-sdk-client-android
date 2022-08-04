/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension

import com.google.gson.Gson
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Response

internal fun <T> Observable<NetworkResponse<T>>.subscribeAndMapNetworkResponse(
    onSuccess: Success<T>,
    onApiError: ApiError,
    onFailure: Failure
): Disposable =
    subscribe(
        { networkResponse ->
            when (networkResponse) {
                is NetworkResponse.ApiError -> networkResponse.apiErrorResponse?.let {
                    (onApiError::apiError)(
                        it
                    )
                } ?: (onFailure::failure)(UnknownNetworkResponseException)
                is NetworkResponse.Success -> networkResponse.data?.let {
                    (onSuccess::success)(
                        it
                    )
                } ?: (onFailure::failure)(UnknownNetworkResponseException)
            }
        }, {
            (onFailure::failure)(it)
        }
    )

internal fun <T> mapRetrofitResponseToNetworkResponse(response: Response<T>): ObservableSource<out NetworkResponse<T>> {
    return if (response.isSuccessful) {
        Observable.just(NetworkResponse.Success(response.body()!!))
    } else {
        val errorResponse: ApiErrorResponse = Gson().fromJson(
            response.errorBody()?.charStream(),
            ApiErrorResponse::class.java
        )
        Observable.just(NetworkResponse.ApiError(errorResponse))
    }
}
