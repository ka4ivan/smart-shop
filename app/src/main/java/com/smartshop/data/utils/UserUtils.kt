package com.smartshop.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object UserUtils {
    @SuppressLint("HardwareIds")
    fun getUserId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
