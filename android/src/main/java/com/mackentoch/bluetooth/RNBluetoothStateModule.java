package com.mackentoch.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by tygzx on 17/7/12.
 */

public class RNBluetoothStateModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactApplicationContext;

    public RNBluetoothStateModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactApplicationContext=reactContext;
    }

    @Override
    public String getName() {
        return "RNBluetoothState";
    }

    @ReactMethod
    public void initialize(){
        String state=handleBluetoothState();
        sendEvent(state);
    }


    private String handleBluetoothState(){
        BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
        if (blueadapter==null){
            return "unsupported";
        }

        if (!blueadapter.isEnabled()){
            return "off";
        }

        if (ContextCompat.checkSelfPermission(reactApplicationContext,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED){
            return "unauthorized";
        }

        return "on";
    }

    private void sendEvent(String state){
        WritableMap map = new WritableNativeMap();
        map.putString("state",state);
        reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("centralManagerDidUpdateState", state);
    }

}
