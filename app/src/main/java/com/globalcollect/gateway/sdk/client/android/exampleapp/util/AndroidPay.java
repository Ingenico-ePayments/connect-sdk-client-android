package com.globalcollect.gateway.sdk.client.android.exampleapp.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.exampleapp.activities.ConfirmationActivity;
import com.globalcollect.gateway.sdk.client.android.exampleapp.activities.StartPageActivity;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.selectionview.ProductSelectionView;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductNetworksAsyncTask.OnPaymentProductNetworksCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductPublicKeyAsyncTask.OnPaymentProductPublicKeyLoadedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductNetworksResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductPublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_ANDROIDPAY;

/**
 * Class that contains the code to successfully complete an Android Pay payment
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class AndroidPay implements OnPaymentProductPublicKeyLoadedListener,
                                   OnPaymentProductNetworksCallCompleteListener {

    private static final String TAG = AndroidPay.class.getName();

    // The view in which to show error dialogs, toasts and the Android pay chooser
    private ProductSelectionView selectionView;

    // The activity used to provide context and be the OnClicklistener for error dialogs
    private Activity activity;

    // The session that is used for communication with the API
    private GcSession session;

    private ShoppingCart shoppingCart;

    private PaymentProduct paymentProduct;

    private PaymentContext paymentContext;
    private PaymentProductPublicKeyResponse publicKeyResponse;


    public AndroidPay(ProductSelectionView selectionView, Activity activity, GcSession session,
                      PaymentContext paymentContext, ShoppingCart shoppingCart, PaymentProduct paymentProduct) {
        this.selectionView = selectionView;
        this.activity = activity;
        this.session = session;
        this.paymentContext = paymentContext;
        this.shoppingCart = shoppingCart;
        this.paymentProduct = paymentProduct;
    }

    public void start() {
        session.getPaymentProductPublicKey(activity, PAYMENTPRODUCTID_ANDROIDPAY, this);
    }

    @Override
    public void onPaymentProductPublicKeyLoaded(PaymentProductPublicKeyResponse response) {
        if (response != null && response.getPublicKey() != null && !response.getPublicKey().isEmpty()) {
            // Now that we have the Public key, we also need to retrieve the available networks
            // for Android Pay
            session.getPaymentProductNetworks(activity,
                    PAYMENTPRODUCTID_ANDROIDPAY,
                    paymentContext,
                    this);
            publicKeyResponse = response;

        } else {
            selectionView.hideLoadingIndicator();
            selectionView.showTechnicalErrorDialog((DialogInterface.OnClickListener) activity);
        }

    }

    @Override
    public void onPaymentProductNetworksCallComplete(PaymentProductNetworksResponse response) {
        if (response != null && response.getNetworks() != null && !response.getNetworks().isEmpty()) {

            // Connect to the Google API in order to be able to make Android Pay calls
            GoogleApiClient googleApiClient = WalletUtil.generateGoogleApiClient(activity, session);
            googleApiClient.connect();

            // Create a MaskedWalletRequest, that will retrieve the masked wallet from Google
            MaskedWalletRequest maskedWalletRequest = WalletUtil.generateMaskedWalletRequest(paymentContext, shoppingCart, publicKeyResponse.getPublicKey(), response.getNetworks());

            // Hide the progressDialog before showing Android Pay
            selectionView.hideLoadingIndicator();

            // Load the masked wallet to start the transaction for the user, after the user has finished
            // with the chooser, or the chooser did not pop up at all, "onActivityResult" will be
            // called. This callback method is present in the Activity.
            Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, Constants.MASKED_WALLET_RETURN_CODE);
        } else {
            selectionView.hideLoadingIndicator();
            selectionView.showTechnicalErrorDialog((DialogInterface.OnClickListener) activity);
        }
    }

    public void handleOnActivityResult(int resultCode, Intent data, int errorCode) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (data != null) {
                    MaskedWallet maskedWallet =
                            data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);

                    // create the paymentRequest that can eventually be used to pay with, set
                    // Android Pay as the payment product
                    PaymentRequest paymentRequest = new PaymentRequest();
                    paymentRequest.setPaymentProduct(paymentProduct);

                    initializeAndStartConfirmationActivity(maskedWallet, paymentRequest);
                }
                break;
            case Activity.RESULT_CANCELED:
                Log.i(TAG, "Android Pay was cancelled");
                break;
            case WalletConstants.RESULT_ERROR:
            default:
                Log.e(TAG, "Something went wrong whilst retrieving the Masked Wallet; errorCode: " + errorCode);
                handleError(errorCode);
                break;
        }
    }

    private void initializeAndStartConfirmationActivity(MaskedWallet maskedWallet, PaymentRequest paymentRequest) {
        Intent confirmationPageIntent = new Intent(activity, ConfirmationActivity.class);
        confirmationPageIntent.putExtra(Constants.INTENT_GC_SESSION, session);
        confirmationPageIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
        confirmationPageIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
        confirmationPageIntent.putExtra(Constants.INTENT_MASKED_WALLET, maskedWallet);
        confirmationPageIntent.putExtra(Constants.INTENT_PAYMENT_REQUEST, paymentRequest);
        activity.startActivity(confirmationPageIntent);
    }

    private void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                // may be recoverable if the user attempts to lower the charge of the payment
                selectionView.showSpendingLimitExceededErrorDialog(createPositiveListener(), createNegativeListener());
                break;
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                // show the user that his payment failed and that (s)he should try again
                selectionView.showTechnicalErrorDialog((DialogInterface.OnClickListener) activity);
        }
    }

    private DialogInterface.OnClickListener createPositiveListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send the user back to the webstore, so (s)he will be able to update her shoppingbag
                Intent intent = new Intent(activity, StartPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        };
    }

    private DialogInterface.OnClickListener createNegativeListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Just dismiss the dialog so the user can choose another payment method
                selectionView.hideAlertDialog();
            }
        };
    }
}
