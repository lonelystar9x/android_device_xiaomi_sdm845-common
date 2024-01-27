/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2022 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.lineageos.settings.dirac.DiracUtils;
import org.lineageos.settings.thermal.ThermalUtils;
import org.lineageos.settings.preferences.FileUtils;
import org.lineageos.settings.vibration.VibratorStrengthPreference;
import org.lineageos.settings.vibration.CallVibratorStrengthPreference;
import org.lineageos.settings.vibration.NotifVibratorStrengthPreference;
import org.lineageos.settings.vibration.VibratorSettings;
import org.lineageos.settings.vibration.VibratorOverrideModeSwitch;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "XiaomiParts";

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            FileUtils.setValue(file, "1");
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        boolean enabled = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sharedPrefs.getBoolean(VibratorSettings.PREF_VMAX_OVERRIDE_SWITCH, false);
        restore(VibratorOverrideModeSwitch.getFile(), enabled);

        if (DEBUG) Log.d(TAG, "Received boot completed intent");
        try {
            DiracUtils.getInstance(context);
        } catch (Exception e) {
            Log.d(TAG, "Dirac is not present in system");
        }
        ThermalUtils.startService(context);
        VibratorStrengthPreference.restore(context);
        CallVibratorStrengthPreference.restore(context);
        NotifVibratorStrengthPreference.restore(context);
    }
}
