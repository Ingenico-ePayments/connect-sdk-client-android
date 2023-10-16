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
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.subscribeAndMapNetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.GetCustomerDetails
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.GetPaymentItems
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.GetPaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.GetPaymentProductDirectory
import com.ingenico.connect.gateway.sdk.client.android.sdk.product.GetPaymentProducts
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
     * Gets the [BasicPaymentProducts] from the gateway.
     *
     * @param onSuccess calls this parameter when [BasicPaymentProducts] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the [PaymentProduct] from the gateway.
     *
     * @param paymentProductId the paymentProductId of the product which needs to be retrieved from the server
     * @param onSuccess calls this parameter when a [PaymentProduct] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the [BasicPaymentProductGroups] from the gateway.
     *
     * @param onSuccess calls this parameter when [BasicPaymentProductGroups] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the [PaymentProductGroup] from the gateway.
     *
     * @param paymentProductGroupId the paymentProductGroupId of the product which needs to be retrieved from the server
     * @param onSuccess calls this parameter when a [PaymentProductGroup] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the [BasicPaymentItems] from the gateway.
     *
     * @param onSuccess calls this parameter when [BasicPaymentItems] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the customer details as a [CustomerDetailsResponse] from the gateway.
     *
     * @param paymentProductId the paymentProductId of the product
     *        for which the customer details need to be retrieved from the server
     * @param countryCode the code of the country where the customer resides
     * @param values a list of keys with a value used to retrieve the details of a customer.
     *        Depending on the country code, different keys are required
     * @param onSuccess calls this parameter when customer details
     *        are successfully fetched as a [CustomerDetailsResponse]
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
     */
    @Suppress("LongParameterList")
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
     * Gets payment product directories as a [PaymentProductDirectoryResponse] from the gateway.
     * Mainly used for IDEAL payments.
     *
     * @param onSuccess calls this parameter when payment product directories
     *        are successfully fetched as a [PaymentProductDirectoryResponse]
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the IinDetails as a [IinDetailsResponse] for a given Bank Identification Number.
     *
     * @param bin Bank Identification Number, first six digits of a bank card number or payment cards number
     * @param onSuccess calls this parameter when the details of an IIN
     *        are successfully fetched as a [IinDetailsResponse]
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the public key as a [PublicKeyResponse] from the gateway.
     * Use this method only in combination with an encryption method.
     *
     * @param onSuccess calls this parameter when the public key is successfully fetched as a [PublicKeyResponse]
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * Gets the [ThirdPartyStatus] from the gateway.
     * Supported payment products for this call are Bancontact and WeChat Pay.
     *
     * @param paymentId the payment id for this payment
     * @param onSuccess calls this parameter when the [ThirdPartyStatus] is successfully fetched
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
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
     * @param source the currency which the amount currently is
     * @param target the currency to which the amount should be converted
     * @param amount the amount in cents to be converted
     * @param onSuccess calls this parameter when the amount is successfully converted
     * @param onApiError calls this parameter when an api error is returned by the server
     * @param onFailure calls this parameter when an unexpected error thrown
     */
    @Suppress("LongParameterList")
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
     * Gets a drawable from the gateway.
     *
     * @param drawableUrl the path to the image, the base URL should be omitted
     * @param onSuccess calls this parameter when the drawable is successfully fetched
     * @param onFailure calls this parameter when an unexpected error thrown
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

object ClientApiNotInitializedException :
    Exception("Initialise the ConnectSDK first before you use the ClientApi class.")
