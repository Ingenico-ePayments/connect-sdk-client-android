package com.ingenico.connect.gateway.sdk.client.android.integrationtest.TestUtil;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Helper for JSON processing.
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class GsonHelper {

    private static final Gson gson = new Gson();

    /**
     * Reads the given resource, and converts it to an instance of the given class.
     */
    public static <T> T fromResourceJson(String resource, Class<T> classOfT) {
        try {
            Reader reader = new InputStreamReader(GsonHelper.class.getClassLoader().getResourceAsStream(resource));
            try {
                return gson.fromJson(reader, classOfT);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
