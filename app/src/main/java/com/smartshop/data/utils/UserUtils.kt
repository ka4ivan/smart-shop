package com.smartshop.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.auth.FirebaseAuth

object UserUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Returns the Firebase account id when the user is signed in (synced across devices),
     * otherwise falls back to a local device id (guest mode, not synced).
     */
    fun getUserId(context: Context): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: getDeviceId(context)
    }
}
