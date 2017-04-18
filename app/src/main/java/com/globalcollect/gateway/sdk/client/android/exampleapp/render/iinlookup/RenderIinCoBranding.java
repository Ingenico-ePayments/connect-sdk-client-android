package com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

import java.security.InvalidParameterException;
import java.util.List;

/**
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderIinCoBranding {

    // Errormessage layout dimensions
    private final int TOOLTIP_TEXT_MARGIN = 9;

    // Prefix for validationmessage views
    private final String VALIDATION_MESSAGE_TAG_PREFIX = "cobrand_message_";

    // Prefix for the coBrand tooltip view
    private final String COBRAND_TOOLTIP_TAG_PREFIX = "cobrand_tooltip_";

    // Strings needed to retrieve the correct translations from Strings.xml
    private final String DETECTED = "detected";
    private final String INTRO_TEXT = "introText";


    /**
     * renders the coBrand notification text underneath the Credit Card number field
     * @param coBrands The coBrands that are allowed in context and are rendered in the coBrand tooltip
     * @param parentView The over arching view that contains the Credit Card edit text under which the notification will be rendered
     * @param fieldId The Id of the Credit Card edit text
     * @param logoManager Manager responsible for retrieving Logo's
     * @param listener The OnClick listener that will be attached to the coBrands
     */
    public void renderIinCoBrandNotification(final Context context, final List<BasicPaymentItem> coBrands, final ViewGroup parentView, final String fieldId, final AssetManager logoManager, final View.OnClickListener listener) {

        if (context == null) {
            throw new InvalidParameterException("Error rendering CoBrand tooltip, context may not be null");
        }
        if (coBrands == null) {
            throw new InvalidParameterException("Error rendering CoBrand tooltip, coBrands may not be null");
        }
        if (parentView == null ) {
            throw new InvalidParameterException("Error rendering CoBrand tooltip, rowView may not be null");
        }
        if (fieldId == null ) {
            throw new InvalidParameterException("Error rendering CoBrand tooltip, fieldId may not be null");
        }
        if (logoManager == null) {
            throw new InvalidParameterException("Error rendering CoBrand tooltip, logoManager may not be null");
        }

        // Check if there is not already a message of this kind showing in the ViewGroup
        if (parentView.findViewWithTag(VALIDATION_MESSAGE_TAG_PREFIX + fieldId) != null) {
            return;
        }

        // Create a new TextView and add it to the rowView
        final ViewGroup rowView = (ViewGroup) parentView.findViewWithTag(fieldId).getParent();
        TextView iinCoBrandNotificationView = new TextView(rowView.getContext());
        iinCoBrandNotificationView.setText(translate(DETECTED, context));
        iinCoBrandNotificationView.setPaintFlags(iinCoBrandNotificationView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        iinCoBrandNotificationView.setGravity(Gravity.END);
        iinCoBrandNotificationView.setTag(VALIDATION_MESSAGE_TAG_PREFIX + fieldId);
        iinCoBrandNotificationView.setTextAppearance(rowView.getContext(), R.style.IinCoBrandNotification);

        // Set an onclick listener for the notification text, so the coBrand tooltip can toggle
        iinCoBrandNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the clicked notification is already showing it's body
                // If so remove it, or else show it
                View parentViewGroup = (ViewGroup)rowView.getParent();
                View tooltipTextView = parentViewGroup.findViewWithTag(COBRAND_TOOLTIP_TAG_PREFIX + fieldId);
                if (tooltipTextView == null) {
                    addCoBrandToolTip(coBrands, rowView, fieldId, logoManager, listener, context);
                } else {
                    removeCoBrandTooltip(tooltipTextView);
                }
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN);

        ViewGroup parentViewGroup = (ViewGroup)rowView.getParent();
        parentViewGroup.addView(iinCoBrandNotificationView, parentViewGroup.indexOfChild(rowView) +1, layoutParams);
    }

    /**
     * Removes the coBrand notification as wel as the body if it they are visible
     * @param rowView The parent row View in wich the notification sits
     * @param fieldId The id of the field, used to find the correct view in the parent
     */
    public void removeIinCoBrandNotification(ViewGroup rowView, String fieldId) {
        View view = rowView.findViewWithTag(VALIDATION_MESSAGE_TAG_PREFIX + fieldId);
        if (view != null)
            ((ViewGroup) view.getParent()).removeView(view);
        removeCoBrandTooltip(rowView.findViewWithTag(COBRAND_TOOLTIP_TAG_PREFIX + fieldId));
    }

    private void addCoBrandToolTip(List<BasicPaymentItem> paymentProductsAllowedInContext, ViewGroup rowView, String fieldId, AssetManager logoManager, View.OnClickListener listener, Context context) {
        // Create a new LinearLayout and add it under the rowView.
        LinearLayout coBrandTooltipLayout = new LinearLayout(rowView.getContext());
        coBrandTooltipLayout.setOrientation(LinearLayout.VERTICAL);
        coBrandTooltipLayout.setBackgroundResource(R.drawable.tooltip_text_background);
        coBrandTooltipLayout.setTag(COBRAND_TOOLTIP_TAG_PREFIX + fieldId);
        LinearLayout.LayoutParams coBrandTooltipLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        coBrandTooltipLayoutParams.setMargins(TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN, TOOLTIP_TEXT_MARGIN);


        // Create a new TextView and add it to the coBrandToolTipLayout
        TextView tooltipTextView = new TextView(rowView.getContext());
        tooltipTextView.setText(translate(INTRO_TEXT, context));
        coBrandTooltipLayout.addView(tooltipTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Set the correct translation for the text in the textView

        // Render the paymentproducts the user can choose from in the tooltip box
        for (BasicPaymentItem basicPaymentItem : paymentProductsAllowedInContext) {
            // Inflate the activity_select_payment_product_render layout
            LayoutInflater inflater  = (LayoutInflater)coBrandTooltipLayout.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View paymentProductLayout = inflater.inflate(R.layout.activity_render_cobrand_payment_product, coBrandTooltipLayout, false);

            // Set a tag, so we can find it back after it has been clicked
            paymentProductLayout.setTag(basicPaymentItem);

            // Get the TextView and ImageView which will be filled
            TextView paymentProductNameTextView         = (TextView) paymentProductLayout.findViewById(R.id.coBrandPaymentProductName);
            ImageView paymentProductNameLogoImageView   = (ImageView)paymentProductLayout.findViewById(R.id.coBrandPaymentProductLogo);

            // Set the translated value
            Translator translator = Translator.getInstance(coBrandTooltipLayout.getContext());
            paymentProductNameTextView.setText(translator.getPaymentProductName(basicPaymentItem.getId()));

            // Get the logo for the product and set it as the background
            BitmapDrawable drawable = (BitmapDrawable)logoManager.getLogo(basicPaymentItem.getId());

            if (Build.VERSION.SDK_INT < 16) {
                paymentProductNameLogoImageView.setBackgroundDrawable(drawable);
            } else {
                paymentProductNameLogoImageView.setBackground(drawable);
            }

            paymentProductLayout.setOnClickListener(listener);
            coBrandTooltipLayout.addView(paymentProductLayout);
        }

        // Add the tooltip view to the ViewGroup
        ViewGroup parentViewGroup = (ViewGroup)rowView.getParent();
        parentViewGroup.addView(coBrandTooltipLayout, parentViewGroup.indexOfChild(rowView) +1, coBrandTooltipLayoutParams);
    }

    private void removeCoBrandTooltip(View tooltipTextView) {
        if (tooltipTextView != null) {
            ViewGroup parentView = ((ViewGroup) tooltipTextView.getParent());
            parentView.removeView(tooltipTextView);
        }
    }

    private String translate (String coBrandMessageId, Context context) {
        Translator translator = Translator.getInstance(context);
        return translator.getCoBrandNotificationText(coBrandMessageId);
    }
}
