package com.example.asessucm;

/**
 * Based on Ble-Gatt-Movesense-2.0 from https://gits-15.sys.kth.se/anderslm/Ble-Gatt-Movesense-2.0
 * Updated with currently 2012-12-14 non-deprecated methods to get permissions :)
 */


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.asessucm.uiutils.DeviceRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
public class BluetoothScanActivity extends AppCompatActivity {

    public static final String MOVESENSE = "Movesense";

    private static final long SCAN_PERIOD = 5000;
    private static final int REQUEST_ENABLE_BT = 1000;
    private static final int REQUEST_ACCESS_LOCATION = 1001;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
    };

    private ActivityResultLauncher<String[]> activityResultLauncher;

    public static String SELECTED_DEVICE = "Selected device";


    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> devices;
    DeviceRecyclerAdapter deviceRecyclerAdapter;
    private boolean scanning = false;
    private Handler handler;

    Button scanbtn;
    TextView bluetoothTextView;
    RecyclerView recyclerView;


    public BluetoothScanActivity() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Log.i("activityResultLauncher", ""+result.toString());
            boolean areAllGranted = true;
            for(Boolean b : result.values()) {
                areAllGranted = areAllGranted && b;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        devices = new ArrayList<>();
        deviceRecyclerAdapter = new DeviceRecyclerAdapter(devices, this::onDeviceSelected);
        handler = new Handler();


        scanbtn = (Button) findViewById(R.id.scanBtn);
        scanbtn.setOnClickListener(this::scanBtn);

        recyclerView = findViewById(R.id.devices_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deviceRecyclerAdapter);

        bluetoothTextView = (TextView) findViewById(R.id.BluetoothTextView);
    }


    private void onDeviceSelected(int position) {
        BluetoothDevice selectedDevice = devices.get(position);

        Intent intent = new Intent(BluetoothScanActivity.this, SensorActivity.class);
        intent.putExtra(SELECTED_DEVICE, selectedDevice);
        startActivity(intent);
        finish();
    }



    private void scanBtn(View view) {
        devices.clear();
        deviceRecyclerAdapter.notifyDataSetChanged();
        final BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        if (!scanning) {
            //Stop scanning after 5 seconds.
            handler.post(new Runnable(){

                @Override
                public void run() {
                    bluetoothTextView.setText("Searching for Movesense...");
                }
            });
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    if (scanning) {
                        scanning = false;
                        scanner.stopScan(scanCallback);
                        String tmp = "FOUND " +devices.size() +" DEVICES";
                        Log.i("BLUETOOTH", tmp );
                        bluetoothTextView.setText(tmp);
                    }
                }
            }, SCAN_PERIOD);
            scanning = true;
            scanner.startScan(scanCallback);
            Log.i("BLUETOOTH", "SCAN STARTED");


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.activityResultLauncher.launch(PERMISSIONS_STORAGE);
        initBLE();

    }

    @Override
    protected void onStop(){
        super.onStop();
        devices.clear();
    }

    private void initBLE() {
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.i("BLUETOOTH ERROR", "BLE NOT SUPPORTED");
        }


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice device = result.getDevice();
            @SuppressLint("MissingPermission") final String name = device.getName();

            handler.post(new Runnable() {

                @Override
                public void run() {
                    Log.i("BLUETOOTH", "SEARCHING...");
                    if (name != null && name.contains(MOVESENSE) && !devices.contains(device)) {
                        devices.add(device);
                        deviceRecyclerAdapter.notifyDataSetChanged();
                        Log.i("BLUETOOTH", "Found device!: " + name);
                    } else if (name!=null){
                        Log.i("BLUETOOTH", name);
                    }
                }
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i("BLUETOOTH", "onBatchScanResult");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i("BLUETOOTH", "onScanFailed");
        }


    };

    // callback for Activity.requestPermissions
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            // if request is cancelled, the results array is empty
            if (grantResults.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // stop this activity
                this.finish();
            }
        }
    }

    // callback for request to turn on BT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user chooses not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
