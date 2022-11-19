package com.example.dtthouses.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base application class used for Hilt dependency injection.
 */
@HiltAndroidApp
class BaseApplication : Application()