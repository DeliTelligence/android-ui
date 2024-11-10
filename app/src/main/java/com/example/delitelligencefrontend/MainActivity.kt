/* https://www.youtube.com/watch?v=qyG-SDfYNBE&t=2981s&ab_channel=KApps
Android Bluetooth Low Energy Guide (Connect to a BLE sensor) - Android Studio Tutorial with Kotlin
Date 6/11/2024 accessed */

package com.example.delitelligencefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.delitelligencefrontend.presentation.Navigation
import com.example.delitelligencefrontend.ui.theme.DeliTelligenceFrontEndTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeliTelligenceFrontEndTheme {
                Navigation(
                    onBluetoothStateChanged = {
                        // No operation or show some fallback UI
                    }
                )
            }
        }
    }
}