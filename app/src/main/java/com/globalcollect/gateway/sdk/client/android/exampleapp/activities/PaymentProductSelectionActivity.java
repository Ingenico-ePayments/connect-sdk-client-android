package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.CheckCommunication;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.MerchantAction;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.util.AndroidPay;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.selectionview.ProductSelectionView;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.selectionview.ProductSelectionViewImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicatorConfiguration;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Size;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTGROUPID_CARDS;
import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_AFTERPAY_INSTALLMENTS;
import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_AFTERPAY_INVOICE;
import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_ANDROIDPAY;
import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_BanContact;
import static com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_BOLETOBANCARIO;

/**
 * Activity that lists all the available payment options
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class PaymentProductSelectionActivity extends ShoppingCartActivity implements OnBasicPaymentItemsCallCompleteListener,
                                                                                     OnPaymentProductCallCompleteListener,
                                                                                     OnPaymentProductGroupCallCompleteListener, DialogInterface.OnClickListener {

    private static final String TAG = PaymentProductSelectionActivity.class.getName();

    // The view belonging to this activity
    private ProductSelectionView selectionView;

    private ShoppingCart shoppingCart;

    // The session object that is used to connect to the API
    private GcSession session;

    // Parameters used to initialize the connection
    private String clientSessionId;
    private String customerId;
    private String clientApiUrl;
    private String assetUrl;
    private boolean environmentIsProduction;

    // Variables required to retrieve the payment items that are available for payment
    private PaymentContext paymentContext;
    private boolean groupPaymentProducts;

    // Loaded payment product and selected Account On File information
    private BasicPaymentItems paymentItems;
    private AccountOnFile accountOnFile;

    private AndroidPay androidPay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_product);
        // Initialize the shoppingcart
        super.initialize(this);

        selectionView = new ProductSelectionViewImpl(this, R.id.payment_product_selection_view_layout);

        // This activity won't work without internet, so show an error message if there is no
        // connection
        if (!CheckCommunication.isOnline(this)) {
            selectionView.showNoInternetDialog(this);
            return;
        }

        loadIntentData();

        retrieveClientSessionAndCustomerId();

        if (savedInstanceState != null) {
            initializeSavedInstanceStateData(savedInstanceState);
        }

        if (paymentItems == null) {
            try {
                // Instantiate the GcSession
                session = C2sCommunicatorConfiguration.initWithClientSessionId(clientSessionId, customerId, clientApiUrl, assetUrl, environmentIsProduction, Constants.APPLICATION_IDENTIFIER);
            } catch(InvalidParameterException e) {
                Log.e(TAG, e.getMessage());
                selectionView.showTechnicalErrorDialog(this);
                return;
            }

            selectionView.showLoadingIndicator();
            session.getBasicPaymentItems(getApplicationContext(), paymentContext, this, groupPaymentProducts);
        } else {
            selectionView.renderDynamicContent(paymentItems);
        }
    }

    private void retrieveClientSessionAndCustomerId() {
        // Send a call to your payment Server to have it retrieve a ClientSession and CustomerId via
        // the createSession call. These ID's are needed to initialize the GcSession that
        // communicates with the API.
        // In order to make the call via the Server2Server API, please include the Metadata that is
        // rendered with the method below.
        @SuppressWarnings("unused")
        Map<String, String> metadata = GcUtil.getMetadata(getApplicationContext(), Constants.APPLICATION_IDENTIFIER, null);

        // We will not make the call here, but use the values that were provided on the
        // start-screen instead.
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        clientSessionId = intent.getStringExtra(Constants.MERCHANT_CLIENT_SESSION_IDENTIFIER);
        customerId = intent.getStringExtra(Constants.MERCHANT_CUSTOMER_IDENTIFIER);
        clientApiUrl = intent.getStringExtra(Constants.MERCHANT_CLIENT_API_URL);
        assetUrl = intent.getStringExtra(Constants.MERCHANT_ASSET_URL);
        environmentIsProduction = intent.getBooleanExtra(Constants.MERCHANT_ENVIRONMENT_IS_PRODUCTION, false);
        paymentContext = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_PAYMENT_CONTEXT);
        groupPaymentProducts = intent.getBooleanExtra(Constants.INTENT_GROUP_PAYMENTPRODUCTS, false);
        shoppingCart = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
    }

    private void initializeSavedInstanceStateData(Bundle savedInstanceState) {
        paymentItems = (BasicPaymentItems) savedInstanceState.getSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS);
        shoppingCart = (ShoppingCart) savedInstanceState.getSerializable(Constants.BUNDLE_SHOPPING_CART);
        session = (GcSession) savedInstanceState.getSerializable(Constants.BUNDLE_GC_SESSION);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBasicPaymentItemsCallComplete(BasicPaymentItems paymentItems) {
        if (paymentItems != null
                && paymentItems.getBasicPaymentItems() != null
                && !paymentItems.getBasicPaymentItems().isEmpty()) {
            this.paymentItems = paymentItems;

            selectionView.renderDynamicContent(paymentItems);

            updateLogos(assetUrl, paymentItems.getBasicPaymentItems());
        } else {
            selectionView.showTechnicalErrorDialog(this);
        }
        selectionView.hideLoadingIndicator();
    }

    private void updateLogos(String assetUrl, List<BasicPaymentItem> basicPaymentItems) {
        // if you want to specify the size of the logos you update, set resizedLogo to a size (width, height)
        Size resizedLogo = new Size(100, 100);

        // if you just want to get the default images, set
        // resizedLogo = null;

        AssetManager manager = AssetManager.getInstance(getApplicationContext());
        manager.updateLogos(assetUrl, basicPaymentItems, resizedLogo);
    }

    // The callback method for when a user selects a payment product
    public void onPaymentProductSelected(View v) {
        selectionView.showLoadingIndicator();
        if (v.getTag() instanceof BasicPaymentProduct) {
            String paymentProductId = ((BasicPaymentProduct) v.getTag()).getId();
            session.getPaymentProduct(getApplicationContext(), paymentProductId, paymentContext, this);

        } else if (v.getTag() instanceof BasicPaymentProductGroup) {
            String paymentProductGroupId = ((BasicPaymentProductGroup) v.getTag()).getId();
            session.getPaymentProductGroup(getApplicationContext(), paymentProductGroupId, paymentContext, this);

        } else if (v.getTag() instanceof AccountOnFile) {
            // Store the Account on file so it can be added to the intent later on.
            accountOnFile = (AccountOnFile) v.getTag();
            session.getPaymentProduct(getApplicationContext(), accountOnFile.getPaymentProductId(), paymentContext, this);
        } else {
            throw new InvalidParameterException("Tag in view is not of a valid type");
        }
    }

    @Override
    public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
        handlePaymentItemCallBack(paymentProduct);
    }

    @Override
    public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup) {
        handlePaymentItemCallBack(paymentProductGroup);
    }

    // Determine what view should be served next, based on whether the product has inputfields.
    // For some products special rules apply, which are also handled here.
    private void handlePaymentItemCallBack(PaymentItem paymentItem) {
        if (paymentItem == null) {
            selectionView.hideLoadingIndicator();
            selectionView.showTechnicalErrorDialog(this);

        } else if (PAYMENTPRODUCTID_ANDROIDPAY.equals(paymentItem.getId())) {
            // Android pay requires a different flow even though it has fields. The fields should
            // not be shown to the user, but they will be filled after the user has completed the
            // Android pay flow.
            startAndroidPay((PaymentProduct) paymentItem);

        } else if (PAYMENTPRODUCTID_BanContact.equals(paymentItem.getId())) {
            // The getPaymentProduct call does not yield fields for the BCMC payment product,
            // however there are inputfields for BCMC, that should be retrieved via your payment
            // server.
            retrievePaymentProductFieldsBcmc(paymentItem);

        } else if (paymentItem.getPaymentProductFields().isEmpty()) {
            // For payment products that do not have fields configured other actions are required to
            // complete the payment. These actions may be a redirect to an external payment products'
            // website, or showing instructions to complete the payment.
            // Currently this app does not contain examples for these kinds of products. Instead we
            // will go to the result activity directly.
            startNextActivity(new Intent(this, PaymentResultActivity.class));

        } else {
            // Ask the user for its payment details in the next activity
            determineAndStartDetailInputActivity(paymentItem);

        }
    }

    private void startAndroidPay(PaymentProduct paymentItem) {
        androidPay = new AndroidPay(selectionView, this, session, paymentContext, shoppingCart, paymentItem);
        androidPay.start();
    }

    private void retrievePaymentProductFieldsBcmc(PaymentItem bcmc) {
        if (PAYMENTPRODUCTID_BanContact.equals(bcmc.getId())) {
            // Because the BanContact payment product (BCMC, PPid 3012) has, apart from regular
            // credit card inputFields, other ways for the customer to complete the transaction, it
            // is required that a payment is created first, before rendering the Input Fields. A create
            // payment call cannot be made via this Client 2 Server API, but should be made from your
            // payment server instead. You should add code here that triggers that payment and then
            // have the result sent back to the Client.
            // Since this is an example application, we will simply pretend that the call has been
            // made and load an example JSON response from the resources. This resource is not the
            // full createPaymentResponse, but only the MerchantAction, which contains the fields and
            // showdata required to render the qrCode/button for BCMC.
            Reader reader = new InputStreamReader(getResources().openRawResource(R.raw.bcmc_merchantaction_example));
            MerchantAction bcmcMerchantActionExample = new Gson().fromJson(reader, MerchantAction.class);

            // Add the payment product fields from the merchantAction to the payment item, so that the
            // DetailInputActivity can render them as usual.
            ((PaymentProduct) bcmc).setPaymentProductFields(bcmcMerchantActionExample.getFormFields());
            Intent bcmcDetailInputActivityIntent = new Intent(this, DetailInputActivityBCMC.class);
            bcmcDetailInputActivityIntent.putExtra(Constants.INTENT_SELECTED_ITEM, bcmc);
            bcmcDetailInputActivityIntent.putExtra(Constants.INTENT_BCMC_SHOWDATA, ((Serializable) bcmcMerchantActionExample.getShowData()));
            startNextActivity(bcmcDetailInputActivityIntent);

        } else {
            throw new InvalidParameterException("Can not retrieve fields for BCMC, payment item is not BCMC");
        }
    }

    private void determineAndStartDetailInputActivity(PaymentItem paymentItem) {
        // Determine what DetailInputActivity to load. The base-class just renders the fields and
        // performs default validation on the fields. In some cases this is not enough however. In
        // these cases a subclass of the DetailInputActivity will be loaded that has additional
        // functionality for these specific products/methods.
        Intent detailInputActivityIntent = null;
        if (paymentItem instanceof PaymentProductGroup && PAYMENTPRODUCTGROUPID_CARDS.equals(paymentItem.getId())
                || ((PaymentProduct) paymentItem).getPaymentMethod().equals("card")) {
            detailInputActivityIntent = new Intent(this, DetailInputActivityCreditCards.class);

        } else if (PAYMENTPRODUCTID_BOLETOBANCARIO.equals(paymentItem.getId())) {
            detailInputActivityIntent = new Intent(this, DetailInputActivityBoletoBancario.class);

        } else if (PAYMENTPRODUCTID_AFTERPAY_INSTALLMENTS.equals(paymentItem.getId()) ||
                PAYMENTPRODUCTID_AFTERPAY_INVOICE.equals(paymentItem.getId())){
            detailInputActivityIntent = new Intent(this, DetailInputActivityAfterpay.class);

        } else {
            detailInputActivityIntent = new Intent(this, DetailInputActivity.class);

        }
        detailInputActivityIntent.putExtra(Constants.INTENT_SELECTED_ITEM, paymentItem);
        startNextActivity(detailInputActivityIntent);
    }

    private void startNextActivity(Intent detailInputActivityIntent) {
        // Add data to intent for the detail activity input
        detailInputActivityIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
        detailInputActivityIntent.putExtra(Constants.INTENT_SELECTED_ACCOUNT_ON_FILE, accountOnFile);
        detailInputActivityIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
        detailInputActivityIntent.putExtra(Constants.INTENT_GC_SESSION, session);

        // Start the intent
        startActivity(detailInputActivityIntent);
        selectionView.hideLoadingIndicator();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
            Log.e(this.getClass().getName(), "Errorcode: " + errorCode);
        }
        switch (requestCode) {
            case Constants.MASKED_WALLET_RETURN_CODE:
                androidPay.handleOnActivityResult(resultCode, data, errorCode);
                break;
            case WalletConstants.RESULT_ERROR:
                Log.e(TAG, "Something went wrong whilst retrieving the Masked Wallet; errorCode: " + errorCode);
                selectionView.showTechnicalErrorDialog(this);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS, paymentItems);
        outState.putSerializable(Constants.BUNDLE_SHOPPING_CART, shoppingCart);
        outState.putSerializable(Constants.BUNDLE_GC_SESSION, session);

        super.onSaveInstanceState(outState);
    }

    @Override
    // Callback for the the ok button on the error dialogs
    public void onClick(DialogInterface dialogInterface, int which) {
        // When an error has occurred the Activity is no longer valid. Destroy it and go one step back
        finish();
    }
}
