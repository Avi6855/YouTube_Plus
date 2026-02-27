package com.avinashpatil.app.youtube.helpers

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.PreferenceKeys
import com.avinashpatil.app.youtube.ui.adapters.IconsSheetAdapter
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors

object ThemeHelper {
    /**
     * Set the theme, including accent color and night mode
     */
    fun updateTheme(activity: AppCompatActivity) {
        var accentColor = PreferenceHelper.getString(PreferenceKeys.ACCENT_COLOR, "")
        if (accentColor.isEmpty()) {
            accentColor = if (DynamicColors.isDynamicColorAvailable()) "my" else "blue"
            PreferenceHelper.putString(PreferenceKeys.ACCENT_COLOR, accentColor)
        }

        activity.setTheme(getTheme(accentColor))
        if (accentColor == "my") DynamicColors.applyToActivityIfAvailable(activity)

        val pureThemeEnabled = PreferenceHelper.getBoolean(
            PreferenceKeys.PURE_THEME,
            false
        )
        if (pureThemeEnabled) activity.theme.applyStyle(R.style.Pure, true)
    }

    /**
     * Update the accent color of the app and apply dynamic colors if needed.
     *
     * For now all supported and legacy accent values resolve to BaseTheme; any
     * unknown value also falls back to BaseTheme instead of crashing.
     */
    private fun getTheme(accentColor: String): Int {
        return when (accentColor) {
            "my",
            "blue",
            "red",
            "yellow",
            "green",
            "purple",
            "monochrome",
            "violet" -> R.style.BaseTheme
            else -> R.style.BaseTheme
        }
    }

    fun applyDialogActivityTheme(activity: Activity) {
        activity.theme.applyStyle(R.style.DialogActivity, true)
    }

    /**
     * set the theme mode (light, dark, auto)
     */
    fun getThemeMode(themeMode: String): Int {
        return when (themeMode) {
            "A" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "L" -> AppCompatDelegate.MODE_NIGHT_NO
            "D" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    /**
     * change the app icon
     */
    fun changeIcon(context: Context, newLogoActivityAlias: String) {
        // Disable Old Icon(s)
        for (appIcon in IconsSheetAdapter.availableIcons) {
            val activityClass = context.packageName.removeSuffix(".debug") + "." + appIcon.activityAlias

            // remove old icons
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context.packageName, activityClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        // set the class name for the activity alias
        val newLogoActivityClass = context.packageName.removeSuffix(".debug") + "." + newLogoActivityAlias
        // Enable New Icon
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context.packageName, newLogoActivityClass),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * Get a color by a color resource attr
     */
    fun getThemeColor(context: Context, colorCode: Int) =
        MaterialColors.getColor(context, colorCode, Color.TRANSPARENT)

    /**
     * Get the styled app name
     */
    fun getStyledAppName(context: Context): Spanned {
        val colorPrimary = getThemeColor(context, androidx.appcompat.R.attr.colorPrimaryDark)
        val hexColor = "#%06X".format(0xFFFFFF and colorPrimary)
        return "Libre<span  style='color:$hexColor';>Tube</span>"
            .parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    fun isDarkMode(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}
