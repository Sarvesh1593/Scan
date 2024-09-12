package com.mack.docscan.ui.onboard

import android.content.Context

class OnboardingPrefs(context : Context) {
    private val prefs = context.getSharedPreferences(
        "onboarding_prefs",
        Context.MODE_PRIVATE
    )
    private val KEY_COMPLETED = "onboarding_completed"

    fun isOnboardingCompleted(): Boolean{
        return prefs.getBoolean(
            KEY_COMPLETED,false
        )
    }
    fun setOnboardingCompleted(){
        prefs.edit().putBoolean(KEY_COMPLETED
            ,true)
            .apply()
    }

}