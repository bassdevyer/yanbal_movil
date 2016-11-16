package com.movil.tesis.yanbal.util;

import android.content.Context;
import android.content.SharedPreferences;

import static com.movil.tesis.yanbal.util.Constants.PREFERENCES_FILE_NAME;


/**
 * General preferences access class
 */
public class Preferences {

    private final SharedPreferences preferences;
    private static Preferences instance = null;

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    private Preferences(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean writeIntPreference(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();

    }

    public boolean writeLongPreference(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public int readIntPreference(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public String readStringPreference(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public boolean writeStringPreference(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public long readLongPreference(String key) {
        return preferences.getLong(key, -1);
    }

    public boolean readBooleanPreference(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean writeBooleanPreference(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }


    public boolean writeBulkPreferences(String[] keys, Object[] values) {
        if (keys.length != values.length) {
            throw new UnsupportedOperationException("The size of keys is not equals to values size");
        }
        SharedPreferences.Editor editor = preferences.edit();
        for (int index = 0; index < values.length; index++) {
            Object value = values[index];
            String key = keys[index];
            if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
                continue;
            }
            if (value instanceof String) {
                editor.putString(key, value.toString());
                continue;
            }
            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
                continue;
            }
            if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
                continue;
            }
            if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            }
        }
        return editor.commit();
    }

    boolean removePreference(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        return editor.commit();
    }
}
