package com.neuroverse.auth

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class annotated with @HiltAndroidApp.
 * Serves as the application-level dependency container.
 */
@HiltAndroidApp
class AuthApp : Application()
