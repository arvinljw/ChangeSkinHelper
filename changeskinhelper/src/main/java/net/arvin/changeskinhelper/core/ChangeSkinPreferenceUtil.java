package net.arvin.changeskinhelper.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by arvinljw on 2019-08-09 17:44
 * Function：
 * Desc：
 */
public class ChangeSkinPreferenceUtil {
    public static String PREFERENCE_NAME = "net.arvin.changeskinhelper.core";

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static Map<String, ?> getAll(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getAll();
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.contains(key);
    }

    public static boolean removeSomething(Context context, String... keys) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if (keys == null)
            return false;
        for (String k : keys) {
            editor.remove(k);
        }
        editor.commit();
        return true;
    }

    public static boolean clearSP(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        return true;
    }

    public static void put(Context context, String key, Object object) {
        if (object instanceof String) {
            putString(context, key, (String) object);
        } else if (object instanceof Integer) {
            putInt(context, key, (Integer) object);
        } else if (object instanceof Boolean) {
            putBoolean(context, key, (Boolean) object);
        } else if (object instanceof Float) {
            putFloat(context, key, (Float) object);
        } else if (object instanceof Long) {
            putLong(context, key, (Long) object);
        } else {
            putString(context, key, object.toString());
        }
    }

    public static Object get(Context context, String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return getString(context, key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return getInt(context, key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return getBoolean(context, key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return getFloat(context, key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return getLong(context, key, (Long) defaultObject);
        }
        return null;
    }
}
