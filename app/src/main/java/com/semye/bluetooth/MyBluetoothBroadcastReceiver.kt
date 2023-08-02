package com.semye.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBluetoothBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("yesheng", "MyBluetoothBroadcastReceiver收到广播:" + intent?.action.orEmpty())
        when (intent?.action) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                Log.i("yesheng", "开始扫描蓝牙设备")
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                Log.i("yesheng", "蓝牙设备扫描完成")
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                Log.d("yesheng", "蓝牙状态变化:" + state)
            }
            BluetoothDevice.ACTION_FOUND -> {
                val device =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as? BluetoothDevice
                Log.i(
                    "yesheng",
                    "蓝牙设备列表:" + "mac地址:" + device?.address + ",设备名称:" + device?.name + ",设备别名:" + device?.alias + " " + device?.type
                )
                val name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME)
                Log.i("yesheng", "蓝牙名称:" + name)
                val cla =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS) as? BluetoothClass
                Log.i("yesheng", "蓝牙class" + cla.toString())

                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, 0)
                Log.i("yesheng", "rssi:" + rssi)
            }

        }
    }
}