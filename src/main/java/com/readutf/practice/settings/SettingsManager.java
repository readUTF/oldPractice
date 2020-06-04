package com.readutf.practice.settings;


import com.readutf.practice.Practice;
import com.readutf.practice.profiles.Profile;

public class SettingsManager {

    public Practice practice;
    private static SettingsManager settingsManager;

    public SettingsManager(Practice practice) {
        this.practice = practice;
        settingsManager = this;
    }

    public boolean getSettingValue(Setting setting, Profile profile) {
        if(profile.getSettings().containsKey(setting)) {
            return profile.getSettings().get(setting);
        }
        return setting.defaultValue;
    }

    public boolean toggleValue(Setting setting, Profile profile) {
        profile.getSettings().put(setting, !profile.getSettings().get(setting));
        return profile.getSettings().get(setting);
    }

    public static SettingsManager get() {
        return settingsManager;
    }


}
