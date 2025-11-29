package com.smartwardrobe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.smartwardrobe.app.data.WardrobeRepository




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WardrobeRepository.loadAll(this)
        setContent {
            SmartWardrobeApp()
        }
    }
}
