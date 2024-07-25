package com.khodchenko.mafiaapp.helpers


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BluetoothHelper(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter?
    private val bluetoothReceiver: BluetoothReceiver
    private val connectedDevices: MutableList<BluetoothDevice>

    init {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        connectedDevices = ArrayList()
        bluetoothReceiver = BluetoothReceiver()
    }

    fun registerBluetoothReceiver() {
        if (bluetoothAdapter != null) {
            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            context.registerReceiver(bluetoothReceiver, filter)
        }
    }

    fun unregisterBluetoothReceiver() {
        context.unregisterReceiver(bluetoothReceiver)
    }

    fun updateConnectedDevices() {
        if (bluetoothAdapter != null) {
            connectedDevices.clear()
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                connectedDevices.addAll(bluetoothAdapter.bondedDevices)
                return
            }

        }
    }

    fun isBluetoothDeviceConnected(device: BluetoothDevice): Boolean {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            for (connectedDevice in connectedDevices) {
                if (connectedDevice == device) {
                    return true
                }
            }
        }
        return false
    }

    val isBluetoothSupported: Boolean
        get() = bluetoothAdapter != null
    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter != null && bluetoothAdapter.isEnabled
    val isLocationPermissionGranted: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
        } else {
            true // For SDK versions below S, no runtime permission check is needed
        }

    fun getBluetoothDevices(): List<BluetoothDevice>{
        return  connectedDevices
    }
    private inner class BluetoothReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                if (bluetoothAdapter!!.state == BluetoothAdapter.STATE_ON) {
                    updateConnectedDevices()
                }
            }
        }
    }
}