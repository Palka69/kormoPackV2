package com.example.kormopack.framework.receivers

interface NetworkStatusListener {
    fun onNetworkStatusChanged(isConnected: Boolean)
}
