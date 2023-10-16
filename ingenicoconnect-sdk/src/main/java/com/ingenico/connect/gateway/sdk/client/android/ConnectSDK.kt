/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi
import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApiNotInitializedException
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.PaymentConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.EncryptDataException
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.EncryptedPaymentRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.subscribeAndMapNetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.support.EncryptPaymentRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Entrypoint for the ConnectSDK.
 * Can only be used with a valid Client Session that was obtained via the Server to Server Create Client Session API.
 */
object ConnectSDK {

    private lateinit var clientApi: ClientApi

    private lateinit var connectSDKConfiguration: ConnectSDKConfiguration

    private lateinit var paymentConfiguration: PaymentConfiguration

    private var disposable: Disposable? = null

    /**
     * Initializes the ConnectSDK.
     * This method has to be called before doing anything else with the SDK.
     *
     * @param connectSDKConfiguration the ConnectSDK configuration properties,
     *        such as communication parameters obtained from the Server to Server Create Client Session API
     *
     * @param paymentConfiguration the payment configuration properties,
     *        such as the amount for the payment, countryCode and other settings
     */
    fun initialize(
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentConfiguration: PaymentConfiguration,
    ) {
        clientApi = ClientApi(connectSDKConfiguration, paymentConfiguration)
        ConnectSDK.connectSDKConfiguration = connectSDKConfiguration
        ConnectSDK.paymentConfiguration = paymentConfiguration
    }

    /**
     * Returns an instance of ClientApi. Use the ClientApi class to make requests to the Connect gateway.
     *
     * @return ClientApi instance
     *
     * @throws ClientApiNotInitializedException if ClientApi has not been initialized
     */
    fun getClientApi(): ClientApi {
        if (this::clientApi.isInitialized) {
            return clientApi
        }

        throw ClientApiNotInitializedException
    }

    /**
     * Returns the ConnectSDKConfiguration that was provided in the initialize method.
     *
     * @return ConnectSDKConfiguration instance
     *
     * @throws ConnectSDKNotInitializedException if the ConnectSDK has not been initialized.
     */
    fun getConnectSdkConfiguration(): ConnectSDKConfiguration {
        if (this::connectSDKConfiguration.isInitialized) {
            return connectSDKConfiguration
        }

        throw ConnectSDKNotInitializedException
    }

    /**
     * Returns the [PaymentConfiguration] that was provided in the initialize method.
     *
     * @return [PaymentConfiguration] instance
     *
     * @throws ConnectSDKNotInitializedException if the ConnectSDK has not been initialized.
     */
    fun getPaymentConfiguration(): PaymentConfiguration {
        if (this::paymentConfiguration.isInitialized) {
            return paymentConfiguration
        }

        throw ConnectSDKNotInitializedException
    }

    /**
     * Encrypts the data in the provided [PaymentRequest] instance.
     *
     * @param paymentRequest the [PaymentRequest] which contains the values that were entered by the user
     * @param onSuccess called when the [PaymentRequest] is successfully encrypted
     * @param onFailure called when an unexpected error occurred
     */
    fun encryptPaymentRequest(
        paymentRequest: PaymentRequest,
        onSuccess: Success<EncryptedPaymentRequest>,
        onFailure: Failure,
    ) {
        if (!this::connectSDKConfiguration.isInitialized) {
            throw ConnectSDKNotInitializedException
        }

        disposable?.dispose()
        disposable = EncryptPaymentRequest().invoke(
            connectSDKConfiguration,
            paymentRequest
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onFailure::failure)(EncryptDataException("Error while encrypting fields")) },
                { (onFailure::failure)(it) }
            )
    }

    /**
     * Closes the ConnectSDK and stops any ongoing activity.
     * Can be used when closing your payment Activity or app.
     */
    fun close() {
        clientApi.disposeApiClient()
        disposable?.dispose()
    }
}

object ConnectSDKNotInitializedException: Exception("ConnectSDK must be initialized before performing this operation.")
