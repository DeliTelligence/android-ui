/* https://www.youtube.com/watch?v=qyG-SDfYNBE&t=2981s&ab_channel=KApps
Android Bluetooth Low Energy Guide (Connect to a BLE sensor) - Android Studio Tutorial with Kotlin
Date 6/11/2024 accessed */

package com.example.delitelligencefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.delitelligencefrontend.model.Session
import com.example.delitelligencefrontend.presentation.Navigation
import com.example.delitelligencefrontend.presentation.viewmodel.ScalesViewModel
import com.example.delitelligencefrontend.ui.theme.DeliTelligenceFrontEndTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var session: Session

    // Inject the SystemStartViewModel using Hilt by viewModels()
    private val systemStartViewModel: ScalesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeliTelligenceFrontEndTheme {
                // Pass ViewModel to Navigation or directly use Compose functions that require it
                Navigation(session, systemStartViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Optional additional logic when the application is destroyed
        cleanupApp()
    }

    private fun initializeApp() {
        // Any additional initialization logic
        systemStartViewModel.connectToScaleAndEnableNotifications()
    }

    private fun cleanupApp() {
        // This function runs when the application is destroyed
        systemStartViewModel.disableNotificationsOnly()
    }
}