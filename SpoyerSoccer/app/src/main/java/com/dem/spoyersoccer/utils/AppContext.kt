package com.dem.spoyersoccer.utils

import com.dem.spoyersoccer.data.network.NetworkManager

class AppContext private constructor() {
    val networkManager = NetworkManager()

    companion object {
        @Volatile
        private var instance: AppContext? = null

        fun getInstance(): AppContext {
            return instance ?: synchronized(this) {
                instance ?: AppContext().also { instance = it }
            }
        }
    }
}
