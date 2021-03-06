/*
 * Copyright 2015 Priyesh Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.priyesh.hexatime.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.priyesh.hexatime.*
import com.priyesh.hexatime.ui.preferences.ClockPositionDialog
import com.priyesh.hexatime.ui.preferences.ColorPickerDialog
import com.priyesh.hexatime.ui.preferences.SliderPreference
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.License
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices

public class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)

        val context = activity
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val saturation = findPreference(KEY_BACKGROUND_SATURATION)
        val brightness = findPreference(KEY_BACKGROUND_BRIGHTNESS)

        fun updateHSBPrefs(hslEnabled: Boolean): Unit {
            saturation.isEnabled = hslEnabled
            brightness.isEnabled = hslEnabled
        }

        val colorMode = prefs.getString(KEY_COLOR_MODE, "0")
        updateHSBPrefs(colorMode.equals("1"))

        onPreferenceChange(KEY_COLOR_MODE, { newValue ->
            updateHSBPrefs((newValue as String).equals("1"))
        })

        fun updateCustomColorPref(customEnabled: Boolean): Unit {
            findPreference(KEY_COLOR_MODE).isEnabled = !customEnabled
            updateHSBPrefs(!customEnabled)
        }

        val customColorEnabled = prefs.getBoolean(KEY_ENABLE_CUSTOM_COLOR, false)
        updateCustomColorPref(customColorEnabled)

        onPreferenceChange(KEY_ENABLE_CUSTOM_COLOR, { newValue ->
            updateCustomColorPref(newValue as Boolean)
        })

        onPreferenceClick("clock_position", { ClockPositionDialog(context).display() })

        onPreferenceClick(KEY_CUSTOM_COLOR, { ColorPickerDialog(context).display() })

        fun displaySlider(title: String, key: String, def: Int): Unit {
            SliderPreference(title, key, def, context).display()
        }

        onPreferenceClick(KEY_BACKGROUND_SATURATION, {
            displaySlider("Saturation", KEY_BACKGROUND_SATURATION, 50)
        })

        onPreferenceClick(KEY_BACKGROUND_BRIGHTNESS, {
            displaySlider("Brightness", KEY_BACKGROUND_BRIGHTNESS, 50)
        })

        onPreferenceClick(KEY_BACKGROUND_OVERLAY_OPACITY, {
            displaySlider("Overlay opacity", KEY_BACKGROUND_OVERLAY_OPACITY, 10)
        })

        onPreferenceClick(KEY_BACKGROUND_OVERLAY_SCALE, {
            displaySlider("Overlay scale", KEY_BACKGROUND_OVERLAY_SCALE, 50)
        })

        onPreferenceClick("source", { openLink(string(R.string.url_source)) })

        onPreferenceClick("donate", { openLink(string(R.string.url_donate)) })

        onPreferenceClick("licenses", {
            val notices = Notices(); noticeList.forEach { notices.addNotice(it) }
            LicensesDialog.Builder(context).setNotices(notices).build().show()
        })

        findPreference("version").summary = "${BuildConfig.VERSION_NAME} - ${BuildConfig.BUILD_TYPE}"
    }

    private fun string(id: Int) = activity.resources.getString(id)

    private fun openLink(url: String) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun onPreferenceClick(key: String, onClick: () -> Unit) {
        findPreference(key).setOnPreferenceClickListener { onClick(); true }
    }

    private fun onPreferenceChange(key: String, onChange: (newValue: Any) -> Unit) {
        findPreference(key).setOnPreferenceChangeListener { pref, newValue ->
            onChange(newValue); true
        }
    }

    private val noticeList = arrayOf(
            Notice("Android Support Library",
                    "http://developer.android.com/tools/support-library/",
                    "Copyright (C) 2011 The Android Open Source Project",
                    ApacheSoftwareLicense20()),
            Notice("SVG Android",
                    "https://github.com/japgolly/svg-android",
                    "Copyright 2011 Larva Labs LLC and Google, Inc.",
                    ApacheSoftwareLicense20()),
            Notice("Paisley (Background Overlay)",
                    "http://subtlepatterns.com/paisley/",
                    "Copyright 2013 Atle Mo - Subtle Patterns",
                    CreativeCommonsAttributionShareAlike30()),
            Notice("Sativa (Background Overlay)",
                    "http://subtlepatterns.com/sativa/",
                    "Copyright 2013 Atle Mo - Subtle Patterns",
                    CreativeCommonsAttributionShareAlike30()),
            Notice("Skulls (Background Overlay)",
                    "http://subtlepatterns.com/skulls/",
                    "Copyright 2013 Atle Mo - Subtle Patterns",
                    CreativeCommonsAttributionShareAlike30()),
            Notice("LicensesDialog",
                    "http://psdev.de",
                    "Copyright 2013 Philip Schiffer <admin@psdev.de>",
                    ApacheSoftwareLicense20())
    )

    private class CreativeCommonsAttributionShareAlike30 : License() {
        override fun getVersion() = "3.0"
        override fun getName() = "Creative Commons Attribution-ShareAlike 3.0"
        override fun getUrl() = "https://creativecommons.org/licenses/by-sa/3.0/us/"
        override fun readFullTextFromResources(c: Context) = getContent(c, R.raw.cc_sharealike_full)
        override fun readSummaryTextFromResources(c: Context) = getContent(c, R.raw.cc_sharealike_summary)
    }

}