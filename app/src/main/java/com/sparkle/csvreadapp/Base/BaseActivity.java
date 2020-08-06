package com.sparkle.csvreadapp.Base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sparkle.csvreadapp.MyPref;
import com.sparkle.csvreadapp.events.MqttStringEvent;
import com.sparkle.csvreadapp.events.UsbSerialActionEvent;
import com.sparkle.csvreadapp.events.UsbSerialMessageEvent;
import com.sparkle.csvreadapp.events.UsbSerialStringEvent;
import com.sparkle.csvreadapp.services.UsbService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    MyPref myPref;
    protected UsbService mUsbService;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            mUsbService = ((UsbService.UsbBinder) arg1).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mUsbService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPref = new MyPref(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        startService(UsbService.class, usbConnection, null);

    }

    @Override
    public void onPause() {
        super.onPause();
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialMessageEvent event) {
        switch (event.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                if (event.obj == null) return;
//                IcCardMessage message = (IcCardMessage) event.obj;
                String message = (String) event.obj;
                if (!message.equals("")){
                    onUsbSerialMessage(message);
                }
                break;
            case UsbService.CTS_CHANGE:
                Toast.makeText(this, "CTS_CHANGE", Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(this, "DSR_CHANGE", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialActionEvent event) {
        switch (event.action) {
            case UsbService.ACTION_USB_PERMISSION_GRANTED:
//                Toast.makeText(this, "USB connected successfully", Toast.LENGTH_SHORT).show();
//                onUsbReady();
                break;
            case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
                Toast.makeText(this, "You denied USB permission", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_NO_USB:
//                Toast.makeText(this, "No USB connected", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_USB_DISCONNECTED:
                Toast.makeText(this, "USB disconnected", Toast.LENGTH_SHORT).show();
//                myPref.setPref3(Constant.ACCOUNT_DETAIL,null);
                break;
            case UsbService.ACTION_USB_NOT_SUPPORTED:
                Toast.makeText(this, "USB device not supported", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    protected void onUsbReady() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialStringEvent event) {
//        Log.d(TAG, "Data received (UsbSerial): " + event.data);
//        if (!event.data.equals("")){
            onUsbSerialString(event.data);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MqttStringEvent event) {
//        Log.d(TAG, "Data received (Mqtt): " + event.topic + ":" + event.data);
//        System.out.println("Data received (Mqtt): " + event.topic + ":" + event.data);
//        if (!event.data.equals("")) {
            onMqttMessage(event);
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String event) {

//        System.out.println("----------event base-----------------"+event);
        onBluetoothDevice(event);
//        Log.d(TAG, "Data received (Mqtt): " + event.topic + ":" + event.data);
//        System.out.println("Data received (Mqtt): " + event.topic + ":" + event.data);
//        onMqttMessage(event);
    }

    protected void onBluetoothDevice(String event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onbytedataEvent(byte[] event) {
//        Log.d(TAG, "Data received (Mqtt): " + event.topic + ":" + event.data);
//        System.out.println("Data received (Mqtt): " + event.topic + ":" + event.data);
        onByteMessage(event);
    }

    protected void onByteMessage(byte[] event){
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onListEvent(AccountDetail detail) {
//        Log.d(TAG, "Data received (Mqtt): " + detail.getFirstname() + ":" + detail.getLastname());
//        onListEventmessage();
//    }

    private void onListEventmessage() {
    }

    protected void onUsbSerialMessage(String message) {
    }

    protected void onMqttMessage(MqttStringEvent event) {
    }

    protected void onUsbSerialString(String data) {
    }



    protected void sendJsonMessage(byte[] message) {
        if (mUsbService != null) {
            mUsbService.write(message);
        }else {
            Toast.makeText(this, "---------", Toast.LENGTH_SHORT).show();
        }
    }




    protected void unRegisterEvent() {
        EventBus.getDefault().unregister(this);
        unbindService(usbConnection);

    }

}

