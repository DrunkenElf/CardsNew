package com.ilnur.cardsnew

import android.app.Application
import android.util.Log
import com.ilnur.cardsnew.utils.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CardsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("CARDSAPP", "yasduagsfu")

    }
}