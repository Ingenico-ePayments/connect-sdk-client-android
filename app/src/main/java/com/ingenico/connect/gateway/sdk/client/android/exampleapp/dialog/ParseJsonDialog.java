package com.ingenico.connect.gateway.sdk.client.android.exampleapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.SessionDetails;

import org.apache.commons.lang3.StringUtils;

/**
 * Dialog with the purpose of parsing session details from Json to Pojo
 * <p>
 * Copyright 2017 Global Collect Services B.V
 */
public class ParseJsonDialog extends Dialog {

    private final JsonParsedListener jsonParsedListener;

    public ParseJsonDialog(@NonNull JsonParsedListener listener, @NonNull Context context) {
        super(context);
        jsonParsedListener = listener;
    }

    public ParseJsonDialog(@NonNull JsonParsedListener listener, @NonNull Context context, int themeResId) {
        super(context, themeResId);
        jsonParsedListener = listener;
    }

    protected ParseJsonDialog(@NonNull JsonParsedListener listener, @NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        jsonParsedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_json);

        Button buttonParseJson = findViewById(R.id.parse_json_btn);
        Button buttonDismiss = findViewById(R.id.dismiss_btn);
        EditText editTextJson = findViewById(R.id.edit_json);

        buttonDismiss.setOnClickListener(v -> {
            dismiss();
        });
        buttonParseJson.setOnClickListener(v -> {
            try {
                String json = editTextJson.getText().toString();
                SessionDetails sessionDetails = new Gson().fromJson(json, SessionDetails.class);
                jsonParsedListener.onJsonParsed(sessionDetails);

                if (sessionDetails.isFullyParsed()) {
                    dismiss();
                } else {
                    String errorText =
                            "Not all values were parsed. Missing values:\n" +
                                    StringUtils.join(sessionDetails.getMissingValues());
                    setErrorTextView(errorText);
                }

            } catch (Exception ex) {
                Log.d("ParseJsonDialog", "Error Parsing: " + ex.toString());

                setErrorTextView("Error parsing JSON:\n" + getCleanMessage(ex));
            }
        });
    }

    private void setErrorTextView(String text) {
        TextView errorTextView = findViewById(R.id.text_parse_json_errors);

        int visibility;
        if (text == null || text.length() == 0) {
            visibility = View.GONE;
        } else {
            visibility = View.VISIBLE;
        }

        errorTextView.setText(text);
        errorTextView.setVisibility(visibility);

    }

    private String getCleanMessage(Exception exception) {
        String message = exception.getMessage();
        if (message == null) {
            return "";
        }
        if (!message.contains(":")) {
            return message;
        }
        String[] splitMessage = message.split(":");

        // Change the first part which describes: com.xx.xx.SomeException to nothing
        splitMessage[0] = "";
        return StringUtils.join(splitMessage, "");
    }

    /**
     * Listener class used to report back the parsed SessionDetails object
     * <p>
     * Copyright 2017 Global Collect Services B.V
     */
    public interface JsonParsedListener {
        void onJsonParsed(SessionDetails sessionDetails);
    }
}
