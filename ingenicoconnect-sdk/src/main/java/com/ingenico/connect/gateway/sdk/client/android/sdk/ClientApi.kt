/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk

import android.graphics.drawable.Drawable
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.PaymentConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.drawable.GetDrawableFromUrl
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.*
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.subscribeAndMapNetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.*
import com.ingenico.connect.gateway.sdk.client.android.sdk.productsgroup.GetPaymentProductGroup
import com.ingenico.connect.gateway.sdk.client.android.sdk.productsgroup.GetPaymentProductGroups
import com.ingenico.connect.gateway.sdk.client.android.sdk.support.ConvertAmount
import com.ingenico.connect.gateway.sdk.client.android.sdk.support.GetIINDetails
import com.ingenico.connect.gateway.sdk.client.android.sdk.support.GetPublicKey
import com.ingenico.connect.gateway.sdk.client.android.sdk.support.GetThirdPartyStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Use this class to perform API calls to the Connect gateway.
 */
class ClientApi(
    private val connectSDKConfiguration: ConnectSDKConfiguration,
    private val paymentConfiguration: PaymentConfiguration
) {

    private var compositeDisposable = CompositeDisposable()

    /**
     * Gets the payment products from the gateway.
     *
     * @param onSuccess Calls this parameter when payment products successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentProducts(
        onSuccess: Success<BasicPaymentProducts>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentProducts().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the payment product from the gateway.
     *
     * @param paymentProductId The paymentProductId of the product which needs to be retrieved from the server.
     * @param onSuccess Calls this parameter when payment a product successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentProduct(
        paymentProductId: String,
        onSuccess: Success<PaymentProduct>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentProduct().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration,
            paymentProductId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the payment products from the gateway.
     *
     * @param onSuccess Calls this parameter when payment product groups successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentProductGroups(
        onSuccess: Success<BasicPaymentProductGroups>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentProductGroups().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the payment product group from the gateway.
     *
     * @param paymentProductGroupId The paymentProductGroupId of the product which needs to be retrieved from the server.
     * @param onSuccess Calls this parameter when payment product group successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentProductGroup(
        paymentProductGroupId: String,
        onSuccess: Success<PaymentProductGroup>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentProductGroup().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration,
            paymentProductGroupId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the payment items from the gateway.
     *
     * @param onSuccess Calls this parameter when payment items successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentItems(
        onSuccess: Success<BasicPaymentItems>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentItems().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration,
            paymentConfiguration.groupPaymentProducts
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the customer details from the gateway.
     *
     * @param paymentProductId The paymentProductId of the product which needs to be retrieved from the server.
     * @param countryCode The code of the country where the customer should reside.
     * @param values A list of keys with a value used to retrieve the details of a customer.
     * Depending on the country code, different keys are required
     * @param onSuccess Calls this parameter when customer details successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getCustomerDetails(
        paymentProductId: String,
        countryCode: String,
        values: List<KeyValuePair>,
        onSuccess: Success<CustomerDetailsResponse>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetCustomerDetails().invoke(
            connectSDKConfiguration,
            paymentProductId,
            countryCode,
            values
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets payment product directories from the gateway.
     * Mainly used for IDEAL payments.
     *
     * @param onSuccess Calls this parameter when payment product directories successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getPaymentProductDirectory(
        paymentProductId: String,
        onSuccess: Success<PaymentProductDirectoryResponse>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPaymentProductDirectory().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration,
            paymentProductId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the IinDetails for a given Bank Identification Number
     *
     * @param bin Bank Identification Number, first six digits of a bank card number or payment cards number
     * @param onSuccess Calls this parameter when the details of a IIN successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getIINDetails(
        bin: String,
        onSuccess: Success<IinDetailsResponse>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetIINDetails().invoke(
            paymentConfiguration.paymentContext,
            connectSDKConfiguration,
            bin
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the public key from the gateway.
     * Use this method only in combination with an encryption method.
     */
    fun getPublicKey(
        onSuccess: Success<PublicKeyResponse>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetPublicKey().invoke(
            connectSDKConfiguration
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets the third party status from the gateway.
     * supported payment products for this call is Bancontact and WeChat Pay.
     *
     * @param paymentId The payment id for this payment.
     * @param onSuccess Calls this parameter when the third party status successful fetched.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getThirdPartyStatus(
        paymentId: String,
        onSuccess: Success<ThirdPartyStatus>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = GetThirdPartyStatus().invoke(
            connectSDKConfiguration,
            paymentId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Converts a given amount in cents from the given source currency to the given target currency
     *
     * @param source Source currency.
     * @param target Target currency.
     * @param amount The amount in cents to be converted.
     * @param onSuccess Calls this parameter when the amount is successful converted.
     * @param onApiError Calls this parameter when an api error is returned by the server.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun convertAmount(
        source: String,
        target: String,
        amount: Long,
        onSuccess: Success<Long>,
        onApiError: ApiError,
        onFailure: Failure
    ) {
        val disposable = ConvertAmount().invoke(
            connectSDKConfiguration,
            source,
            target,
            amount
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeAndMapNetworkResponse(
                { (onSuccess::success)(it) },
                { (onApiError::apiError)(it) },
                { (onFailure::failure)(it) }
            )

        compositeDisposable.add(disposable)
    }

    /**
     * Gets an drawable from the gateway.
     *
     * @param drawableUrl the path to the image, the base URL should be omitted.
     * @param onSuccess Calls this parameter when the drawable is successful fetched.
     * @param onFailure Calls this parameter when an unexpected error thrown.
     */
    fun getDrawableFromUrl(
        drawableUrl: String,
        onSuccess: Success<Drawable>,
        onFailure: Failure
    ) {
        val disposable = GetDrawableFromUrl().invoke(connectSDKConfiguration, drawableUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (onSuccess::success)(it)
            }, {
                (onFailure::failure)(it)
            })

        compositeDisposable.add(disposable)
    }

    internal fun disposeApiClient() {
        compositeDisposable.dispose()
    }
}

object ClientApiNotInitializedException: Exception("Initialise the ConnectSDK first before you use the ClientApi class.")
