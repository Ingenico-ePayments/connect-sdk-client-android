package com.ingenico.connect.gateway.sdk.client.android.exampleapp.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class DialogUtil {

	private DialogUtil () {

	}
	
	/**
	 * Shows an AlertDialog with the given parameters
	 * @param context, Context where the AlertDialog is added to
	 * @param title, the title of the AlertDialog
	 * @param message, the message of the AlertDialog
	 * @param buttonText, the buttontext of the AlertDialog
	 */
	public static AlertDialog showAlertDialog(Context context, String title, String message, String buttonText) {
		return showAlertDialog(context, title, message, buttonText, null);
	}
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, String buttonText, OnClickListener listener) {
		
		// Create a new AlertDialog and set all texts
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		
		// If there is no listener, add empty one
		if (listener == null) {
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonText, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
		} else {
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonText, listener);	
		}
		alertDialog.show();
		return alertDialog;
	}
	
	
	/**
	 * Shows a ProgressDialog with the given parameters
	 * @param context, Context where the ProgressDialog is added to
	 * @param title, the title of the ProgressDialog
	 * @param message, the message of the ProgressDialog
	 * @return ProgressDialog which you can use to hide it when neccessary
	 */
	public static ProgressDialog showProgressDialog(Context context, String title, String message) {
		
		// Create a new ProgressDialog and set all texts
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		return progressDialog;
	}
	
	
	/**
	 * Hides the given dialog when its showing
	 * @param dialog
	 */
	public static void dismissDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		
	}
	
}