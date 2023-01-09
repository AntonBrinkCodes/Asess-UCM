package com.example.asessucm;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.asessucm.Model.SensorReading;
import com.example.asessucm.Model.SensorResultList;
import com.example.asessucm.uiutils.MsgUtils;
import com.example.asessucm.utils.FileHandler;
import com.example.asessucm.utils.TypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SensorActivity extends FragmentActivity implements SensorEventListener {
    // Movesense 2.0 UUIDs (should be placed in resources file)
    public static final UUID MOVESENSE_2_0_SERVICE =
            UUID.fromString("34802252-7185-4d5d-b431-630e7050e8f0");
    public static final UUID MOVESENSE_2_0_COMMAND_CHARACTERISTIC =
            UUID.fromString("34800001-7185-4d5d-b431-630e7050e8f0");
    public static final UUID MOVESENSE_2_0_DATA_CHARACTERISTIC =
            UUID.fromString("34800002-7185-4d5d-b431-630e7050e8f0");
    // UUID for the client characteristic, which is necessary for notifications
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private final String IMU_COMMAND = "Meas/IMU6/52"; // see documentation
    private final byte MOVESENSE_REQUEST = 1, MOVESENSE_RESPONSE = 2, REQUEST_ID = 99;

    // Bluetooth
    private BluetoothDevice selectedDevice = null;
    private BluetoothGatt bluetoothGatt = null;

    // Internal sensors
    SensorManager sensorManager;
    Sensor sensor;
    private int samplingRate = 19231;

    // Saving
    ArrayList<SensorReading> internalSensorReadingList = new ArrayList<>();
    ArrayList<SensorReading> BTSensorReadingList = new ArrayList<>();
    ArrayList<SensorReading> intAll = new ArrayList<>();
    ArrayList<SensorReading> BTAll = new ArrayList<>();
    SensorResultList anglesResultList;
    FileHandler fileHandler;
    double comAcc = 0;
    double IntAngle = 0;
    float IntTimestamp,BTTimestamp;
    int counter = 0;
    boolean saving = false;
    private String SAVE_TAG = "Saving";

    //UI
    //TextView BTAngleView,IntAngleView,BTTimestampView,IntTimestampView,deviceView;
    AppCompatButton startTestBtn;

    private String INT_TAG = "InternalSensor";
    private String BT_TAG = "BTSensor";
    private Handler handler;

    // Fragments
    ConnectFragment connectFragment;
    GraphFragment graphFragment;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor);

        startTestBtn = findViewById(R.id.start_test_btn);
        startTestBtn.setOnClickListener(this::startTestBtn);
        startTestBtn.setActivated(false);


        Intent intent = getIntent();
        selectedDevice = intent.getParcelableExtra(BluetoothScanActivity.SELECTED_DEVICE);

        if (selectedDevice == null) {
            //Show connectFragment with instructions if no device is connected
            connectFragment = new ConnectFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.graphFragmentContainer, connectFragment);
            ft.commit();
            Toast.makeText(this, "No device found, click Connect to find device", Toast.LENGTH_SHORT).show();
        } else {
            graphFragment = new GraphFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.graphFragmentContainer, graphFragment);
            ft.commit();
            Toast.makeText(this, selectedDevice.getName(), Toast.LENGTH_SHORT).show();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        handler = new Handler();

        internalSensorReadingList = new ArrayList<>();
        BTSensorReadingList = new ArrayList<>();
        fileHandler = new FileHandler();


    }

    private void startTestBtn(View view) {

    }

    private void startSaving(View view) {
        saving = true;
        Toast.makeText(this, "Saving started", Toast.LENGTH_SHORT).show();
    }

    private void stopSaving(View view) {
        saving = false;
        Toast.makeText(this, "Saving stopped", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (selectedDevice != null) {
            // Connect and register call backs for bluetooth gatt
            bluetoothGatt =
                    selectedDevice.connectGatt(this, false, BTGattCallback);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStop() {
        super.onStop();
        anglesResultList = new SensorResultList(internalSensorReadingList,BTSensorReadingList);
        fileHandler.saveAnglesResults(anglesResultList,this);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            SensorResultList list = fileHandler.loadAnglesResults(this);
            try {
                bluetoothGatt.close();
            } catch (Exception e) {
                // ugly, but this is to handle a bug in some versions in the Android BLE API
            }
        }
    }

    private final BluetoothGattCallback BTGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                bluetoothGatt = gatt;
                handler.post(new Runnable() {
                    public void run() {
                        MsgUtils.showToast("Connected",getApplicationContext());
                    }
                });
                // Discover services
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Close connection and display info in ui
                bluetoothGatt = null;
                handler.post(new Runnable() {
                    public void run() {
                        MsgUtils.showToast("Disconnected",getApplicationContext());
                    }
                });
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Debug: list discovered services
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    Log.i(BT_TAG, service.getUuid().toString());
                }

                // Get the Movesense 2.0 IMU service
                BluetoothGattService movesenseService = gatt.getService(MOVESENSE_2_0_SERVICE);
                if (movesenseService != null) {
                    // debug: service present, list characteristics
                    List<BluetoothGattCharacteristic> characteristics =
                            movesenseService.getCharacteristics();
                    for (BluetoothGattCharacteristic chara : characteristics) {
                        Log.i(BT_TAG, chara.getUuid().toString());
                    }

                    // Write a command, as a byte array, to the command characteristic
                    // Callback: onCharacteristicWrite
                    BluetoothGattCharacteristic commandChar =
                            movesenseService.getCharacteristic(
                                    MOVESENSE_2_0_COMMAND_CHARACTERISTIC);
                    // command example: 1, 99, "/Meas/Acc/13"
                    byte[] command =
                            TypeConverter.stringToAsciiArray(REQUEST_ID, IMU_COMMAND);
                    commandChar.setValue(command);
                    @SuppressLint("MissingPermission") boolean wasSuccess = bluetoothGatt.writeCharacteristic(commandChar);
                    Log.i("writeCharacteristic", "was success=" + wasSuccess);
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            /*
                            MsgUtils.createDialog("Alert!",
                                            getString(R.string.service_not_found),
                                            DeviceActivity.this)
                                    .show();


                            Log.i(BT_TAG,"Service not found");

                             */
                        }
                    });
                }
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.i(BT_TAG, "onCharacteristicWrite " + characteristic.getUuid().toString());

            // Enable notifications on data from the sensor. First: Enable receiving
            // notifications on the client side, i.e. on this Android device.
            BluetoothGattService movesenseService = gatt.getService(MOVESENSE_2_0_SERVICE);
            BluetoothGattCharacteristic dataCharacteristic =
                    movesenseService.getCharacteristic(MOVESENSE_2_0_DATA_CHARACTERISTIC);
            // second arg: true, notification; false, indication
            @SuppressLint("MissingPermission") boolean success = gatt.setCharacteristicNotification(dataCharacteristic, true);
            if (success) {
                Log.i(BT_TAG, "setCharactNotification success");
                // Second: set enable notification server side (sensor). Why isn't
                // this done by setCharacteristicNotification - a flaw in the API?
                BluetoothGattDescriptor descriptor =
                        dataCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor); // callback: onDescriptorWrite
            } else {
                Log.i(BT_TAG, "setCharacteristicNotification failed");
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, BluetoothGattDescriptor
                descriptor, int status) {
            Log.i(BT_TAG, "onDescriptorWrite, status " + status);

            if (CLIENT_CHARACTERISTIC_CONFIG.equals(descriptor.getUuid()))
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // if success, we should receive data in onCharacteristicChanged
                    handler.post(new Runnable() {
                        public void run() {
                            MsgUtils.showToast("Notifications enabled",getApplicationContext());
                        }
                    });
                }
        }

        /**
         * Callback called on characteristic changes, e.g. when a sensor data value is changed.
         * This is where we receive notifications on new sensor data.
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            // debug
            // Log.i(LOG_TAG, "onCharacteristicChanged " + characteristic.getUuid());

            // if response and id matches
            if (MOVESENSE_2_0_DATA_CHARACTERISTIC.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                if (data[0] == MOVESENSE_RESPONSE && data[1] == REQUEST_ID) {
                    startTestBtn.setActivated(true); //We can start test since we have a package with correct ID and therefore movesense device is streaming.

                    // NB! use length of the array to determine the number of values in this
                    // "packet", the number of values in the packet depends on the frequency set(!)
                    int len = data.length;
                    // ...

                    // parse and interpret the data, ...
                    int time = TypeConverter.fourBytesToInt(data, 2);
                    /*
                    float accX = TypeConverter.fourBytesToFloat(data, 6);
                    float accY = TypeConverter.fourBytesToFloat(data, 10);
                    float accZ = TypeConverter.fourBytesToFloat(data, 14);

                     */
                    float[] accX = new float[4];
                    float[] accY = new float[4];
                    float[] accZ = new float[4];
                    int j = 0;

                    BTTimestamp = time;

                    for(int i = 6;i<43;i+=12) {
                        accX[j] = TypeConverter.fourBytesToFloat(data, i);
                        accY[j] = TypeConverter.fourBytesToFloat(data, i+4);
                        accZ[j] = TypeConverter.fourBytesToFloat(data, i+8);
                        comAcc = Math.sqrt(Math.pow(accX[j],2)+Math.pow(accY[j],2)+Math.pow(accZ[j],2))-9.81;

                        BTAll.add(new SensorReading(comAcc,time));
                        //intAll.add(new SensorReading(IntAngle,IntTimestamp));
                        if (saving) {
                            BTSensorReadingList.add(new SensorReading(comAcc,time));
                            internalSensorReadingList.add(new SensorReading(IntAngle,IntTimestamp));
                            counter = 0;
                        }
                        j++;
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            /**
                             * This is where we show data to user!
                              */

                            //BTTimestampView.setText("" + time + " ms");
                            //BTAngleView.setText("" + (int) BTAngle);
                        }
                    });
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.i(BT_TAG, "onCharacteristicRead " + characteristic.getUuid().toString());
        }
    };

    // Internal sensors
    @Override
    public void onSensorChanged(SensorEvent event) {
        double xGyro = event.values[0]*180/Math.PI;
        double yGyro = event.values[1];
        double zGyro = event.values[2];
        IntTimestamp = event.timestamp;

        double dt = 1.0/52.0;
        IntAngle = IntAngle + dt*xGyro;
        //IntAngleView.setText(""+(int) -IntAngle);
        //IntTimestampView.setText(""+event.timestamp+" ns");

        intAll.add(new SensorReading(-IntAngle,IntTimestamp));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i(INT_TAG,"onAccuracyChanged");
    }

    // InternalAccActivity
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,samplingRate);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public ArrayList<SensorReading> getIntReadingList() {
        return internalSensorReadingList;
    }
}
