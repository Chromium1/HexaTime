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

package com.priyesh.hexatime.core

import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import android.support.v4.graphics.ColorUtils
import com.priyesh.hexatime.log

public class Background(clock: Clock) : PreferenceDelegate {

    private val clock = clock
    private var dimAmount: Int = 0

    init {
        initializeFromPrefs(PreferenceManager.getDefaultSharedPreferences(clock.getContext()))
    }

    private fun initializeFromPrefs(prefs: SharedPreferences) {
      //  dimAmount = prefs.getInt(KEY_BACKGROUND_DIM, 0)
    }

    private fun getDimColor(): Int {
        log("Dim amount: " + dimAmount)
        return Color.argb(dimAmount / 100 * 255, 0, 0, 0)
    }

    override fun onPreferenceChange(prefs: SharedPreferences, key: String) {
        when (key) {
           // KEY_BACKGROUND_DIM -> dimAmount = prefs.getInt(KEY_BACKGROUND_DIM, 0)
        }
    }

    public fun getColor(): Int = clock.getColor()

}