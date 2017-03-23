package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.globalcollect.gateway.sdk.client.android.exampleapp.fragments.FullWalletConfirmationButtonFragment;
import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

/**
 * Activity that shows the Android Pay confirmation page
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ConfirmationActivity extends ShoppingCartActivity implements DialogInterface.OnClickListener {

    private static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 998;

    private PaymentContext paymentContext;

    private ShoppingCart shoppingCart;

    private MaskedWallet maskedWallet;

    private PaymentRequest paymentRequest;

    private SupportWalletFragment mWalletFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        // Get the payment object for this paymentProduct, given by the retailer
        Intent intent = getIntent();
        paymentContext = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_CONTEXT);
        shoppingCart = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
        maskedWallet = (MaskedWallet) intent.getParcelableExtra(Constants.INTENT_MASKED_WALLET);
        paymentRequest = (PaymentRequest) intent.getParcelableExtra(Constants.INTENT_PAYMENT_REQUEST);;

        // Render the shoppingcart details
        shoppingCartRenderer = new RenderShoppingCart(paymentContext, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());

        createAndAddWalletFragment();
    }

    private void createAndAddWalletFragment() {
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setMaskedWalletDetailsTextAppearance(
                        R.style.WalletFragmentDetailsTextAppearance)
                .setMaskedWalletDetailsHeaderTextAppearance(
                        R.style.WalletFragmentDetailsHeaderTextAppearance)
                .setMaskedWalletDetailsBackgroundColor(Color.WHITE)
                .setMaskedWalletDetailsButtonBackgroundResource(
                        R.drawable.disabled_button_style)
                .setMaskedWalletDetailsButtonTextAppearance(
                        R.style.PrimaryButton);

        // [START wallet_fragment_options]
        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.SELECTION_DETAILS)
                .build();
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
        // [END wallet_fragment_options]

        // Now initialize the Wallet Fragment
        String accountName = "Android Pay Demo User";
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWallet(maskedWallet)
                .setMaskedWalletRequestCode(REQUEST_CODE_CHANGE_MASKED_WALLET)
                .setAccountName(accountName);
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_masked_wallet_fragment, mWalletFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHANGE_MASKED_WALLET:
                if (resultCode == Activity.RESULT_OK &&
                        data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                    maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                    ((FullWalletConfirmationButtonFragment) getResultTargetFragment())
                            .updateMaskedWallet(maskedWallet);
                }
                // you may also want to use the new masked wallet data here, say to recalculate
                // shipping or taxes if shipping address changed
                break;
            case WalletConstants.RESULT_ERROR:
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                break;
            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
                Fragment fragment = getResultTargetFragment();
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // When back button is pressed, finish this Activity
        finish();
    }

    public Fragment getResultTargetFragment() {
        return getSupportFragmentManager().findFragmentById(
                R.id.full_wallet_confirmation_button_fragment);
    }
}
