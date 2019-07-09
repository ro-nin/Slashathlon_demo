package com.EmGiuPa.androidgames.ProjectRunner;

import android.content.SharedPreferences;

public class PRSettings {

    public static void setStringSetting(SharedPreferences.Editor editor, String settingKey, String settingValue) {
        editor.putString(settingKey,settingValue);
        editor.apply(); //Si può usare anche commit(), sulla documentazione c'è scritto che però esegue una operazione sincrona
    }

    public static void setIntSetting(SharedPreferences.Editor editor, String settingKey, int settingValue) {
        editor.putInt(settingKey,settingValue);
        editor.apply(); //Si può usare anche commit(), come sopra
    }

    static void setBooleandSetting(SharedPreferences.Editor editor, String settingKey, boolean settingValue) {
        editor.putBoolean(settingKey,settingValue);
        editor.apply(); //Si può usare anche commit(), come sopra
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
        boolean defaultSettingValue = false;
        return sh.getBoolean(settingKey, defaultSettingValue);
    }
}
