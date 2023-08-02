package com.semye.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.semye.bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var model: MainViewModel

    lateinit var binding: ActivityMainBinding

    var bluetoothAdapter: BluetoothAdapter? = null

    private val myBluetoothBroadcastReceiver = MyBluetoothBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        bluetoothAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter == null) {
            Log.d("yesheng", "该设备不支持蓝牙")
        }


        Log.d("yesheng", "蓝牙是否可用" + bluetoothAdapter?.isEnabled.toString())

        val checkSelfPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        Log.d("yesheng", "定位权限" + checkSelfPermission)


        val intentFilter = IntentFilter()
        //监听开始扫描蓝牙
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        //监听结束扫描蓝牙
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(myBluetoothBroadcastReceiver, intentFilter)

        binding.get.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("yesheng", "没有BLUETOOTH权限 ")
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH
                    ), 1
                )
            } else {
                Log.i("yesheng", "有BLUETOOTH权限")
            }
        }

        binding.get2.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("yesheng", "没有BLUETOOTH_ADMIN权限 ")
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_ADMIN
                    ), 2
                )
            } else {
                Log.i("yesheng", "有BLUETOOTH_ADMIN权限")
            }
        }


        binding.open.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Log.i("yesheng", "没有BLUETOOTH_CONNECT权限 ")
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE
                        ), 123
                    )
                }
            } else {
                Log.i("yesheng", "有BLUETOOTH_CONNECT权限")
                bluetoothAdapter?.enable()
            }
        }

        binding.close.setOnClickListener {
            val result = bluetoothAdapter?.disable()
            Log.i("yesheng", "disable :$result")
        }

        binding.scan.setOnClickListener {
            if (
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED)
                ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Log.i("yesheng", "没有BLUETOOTH_SCAN权限 ")
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ), 456
                    )
                }
            } else {
                Log.i("yesheng", "有BLUETOOTH_SCAN权限")
                Log.d("yesheng", bluetoothAdapter?.state.toString())
                val result = bluetoothAdapter?.startDiscovery()//扫描12秒,需要定位权限
                Log.i("yesheng", "result:" + result)
            }
        }

        binding.get3.setOnClickListener {
            bluetoothAdapter?.bondedDevices?.forEach {
                Log.d("yesheng", it.toString())
                Log.i(
                    "yesheng",
                    "蓝牙设备列表:" + "mac地址:" + it.address + ",设备名称:" + it.name + ",设备别名:" + it.alias + " " + it.type
                )

            }
        }

        binding.get4.setOnClickListener {
            val discoverableIntent: Intent =
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                }
            startActivity(discoverableIntent)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("yesheng", "获取BLUETOOTH_CONNECT权限成功 ")
                    bluetoothAdapter?.enable()
                }
            }
        } else if (requestCode == 456) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("yesheng", "获取BLUETOOTH_SCAN权限成功 ")
                    val result = bluetoothAdapter?.startDiscovery()//扫描12秒,需要定位权限
                    Log.i("yesheng", "result:" + result)
                }
            }
        } else if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("yesheng", "获取BLUETOOTH权限成功 ")
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.i("yesheng", "获取BLUETOOTH权限失败 ")
                }
            }
        } else if (requestCode == 2) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("yesheng", "获取BLUETOOTH_ADMIN权限成功 ")
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.i("yesheng", "获取BLUETOOTH_ADMIN权限失败 ")
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(myBluetoothBroadcastReceiver)
        super.onDestroy()
    }

}