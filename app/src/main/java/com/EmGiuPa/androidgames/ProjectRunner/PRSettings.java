package com.EmGiuPa.androidgames.ProjectRunner;

import android.content.SharedPreferences;

public class PRSettings {

    public static void setStringSetting(SharedPreferences.Editor editor, String settingKey, String settingValue) {
        editor.putString(settingKey,settingValue);
        editor.apply();
    }

    public static void setIntSetting(SharedPreferences.Editor editor, String settingKey, int settingValue) {
        editor.putInt(settingKey,settingValue);
        editor.apply();
    }

    static void setBooleandSetting(SharedPreferences.Editor editor, String settingKey, boolean settingValue) {
        editor.putBoolean(settingKey,settingValue);
        editor.apply();
    }

    public static String getStringSetting(SharedPreferences sh, String settingKey) {
        String defaultSettingValue = "no_value";
        return sh.getString(settingKey, defaultSettingValue);
    }

    public static int getIntSetting(SharedPreferences sh, String settingKey) {
        int defaultSettingValue = -1;
        return sh.getInt(settingKey, defaultSettingValue);
    }

    static boolean getBooleanSetting(SharedPreferences sh, String settingKey) {
        boolean defaultSettingValue = true;
        return sh.getBoolean(settingKey, defaultSettingValue);
    }
}
