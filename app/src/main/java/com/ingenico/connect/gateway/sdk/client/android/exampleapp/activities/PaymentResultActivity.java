package com.ingenico.connect.gateway.sdk.client.android.exampleapp.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.configuration.Constants;

/**
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentResultActivity extends ShoppingCartActivity {

    private TextView detailsText;
    private Button copyButton;
    private Button showDetailsButton;
    private String encryptedFields;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_result);
		// Initialize the shoppingcart
		super.initialize(this);

		// Bind some views
        detailsText = findViewById(R.id.payment_result_details);
        copyButton = findViewById(R.id.button_copy);
        showDetailsButton = findViewById(R.id.button_show_details);
        encryptedFields = getIntent().getStringExtra(Constants.INTENT_PAYMENT_ENCRYPTED_FIELDS);

		// Set the correct result
		TextView paymentResultTitle = (TextView) findViewById(R.id.payment_result_title);
		TextView paymentResultDescription = (TextView) findViewById(R.id.payment_result_description);

		if (encryptedFields == null || encryptedFields.isEmpty()) {
			showDetailsButton.setVisibility(View.INVISIBLE);
		}

		// Retrieve error message from paymentInputIntent
		Intent paymentInputIntent = getIntent();
		String errorMessage = paymentInputIntent.getStringExtra(Constants.INTENT_ERRORMESSAGE);


		if (errorMessage == null) {
			// Show success translated texts
			String successfulTitle = getString(R.string.gc_app_result_success_title);
			String successfulDescription = getString(R.string.gc_app_result_success_bodyText);

			paymentResultTitle.setText(successfulTitle);
			paymentResultDescription.setText(successfulDescription);

		} else {

			// Show errormessage translated texts
			paymentResultTitle.setText(getString(R.string.gc_app_result_failed_title));
			paymentResultDescription.setText(getString(R.string.gc_app_result_failed_bodyText));
		}
	}

	public void backButtonPressed(View v) {
	    Intent i = new Intent(this, StartPageActivity.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(i);
	}

	public void showDetails(View view) {
	    if (encryptedFields == null) {
	        return;
        }
		detailsText.setText(encryptedFields);
		detailsText.setVisibility(View.VISIBLE);
		copyButton.setVisibility(View.VISIBLE);
		showDetailsButton.setVisibility(View.GONE);
	}

	public void copyEncryptionKey(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("EncryptedFields", encryptedFields);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "EncryptedFields copied to clipboard.", Toast.LENGTH_SHORT).show();
	}
}
