package com.avinashpatil.app.youtube.extensions

import android.icu.text.CompactDecimalFormat
import com.avinashpatil.app.youtube.helpers.LocaleHelper

fun Long?.formatShort(): String = CompactDecimalFormat
    .getInstance(LocaleHelper.getAppLocale(), CompactDecimalFormat.CompactStyle.SHORT)
    .format(this ?: 0)