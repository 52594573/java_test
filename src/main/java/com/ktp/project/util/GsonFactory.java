package com.ktp.project.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * Gson工厂
 *
 * @author djcken
 * @date 2017/8/7
 */
public final class GsonFactory {

    private static final Gson STATIC;
    private static final Gson STATIC_TRANSIENT;

    static {
        STATIC = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
        STATIC_TRANSIENT = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC).create();
    }

    public static Gson newGson() {
        return STATIC;
    }

    public static Gson newGsonExcludeTransient() {
        return STATIC_TRANSIENT;
    }
}