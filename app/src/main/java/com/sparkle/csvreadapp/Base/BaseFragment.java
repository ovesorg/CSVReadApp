package com.sparkle.csvreadapp.Base;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkle.csvreadapp.events.IcCardMessage;
import com.sparkle.csvreadapp.events.ListEvent;
import com.sparkle.csvreadapp.events.MqttStringEvent;
import com.sparkle.csvreadapp.events.UsbSerialActionEvent;
import com.sparkle.csvreadapp.events.UsbSerialMessageEvent;
import com.sparkle.csvreadapp.events.UsbSerialStringEvent;
import com.sparkle.csvreadapp.services.UsbService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    private UsbService mUsbService;
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
    private Context mContext;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getFragmentView(inflater, container, savedInstanceState);
        Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1)
            Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.black));

        mContext = rootView.getContext();
        return rootView;

    }

    protected abstract View getFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    private View getViewFromResource(int layoutId) {
        return LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        EventBus.getDefault().register(mContext);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
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
        mContext.unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(mContext, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            mContext.startService(startService);
        }
        Intent bindingIntent = new Intent(mContext, service);
        mContext.bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialMessageEvent event) {
        switch (event.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                if (event.obj == null) return;
//                IcCardMessage message = (IcCardMessage) event.obj;
                String message = (String) event.obj;
                onUsbSerialMessage(message);
//                onUsbSerialFragmentMessage(message);
                break;
            case UsbService.CTS_CHANGE:
                Toast.makeText(mContext, "CTS_CHANGE", Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(mContext, "DSR_CHANGE", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialActionEvent event) {
        switch (event.action) {
            case UsbService.ACTION_USB_PERMISSION_GRANTED:
//                Toast.makeText(mContext, "USB Ready", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
//                Toast.makeText(mContext, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_NO_USB:
//                Toast.makeText(mContext, "No USB connected", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_USB_DISCONNECTED:
//                Toast.makeText(mContext, "USB disconnected", Toast.LENGTH_SHORT).show();
                break;
            case UsbService.ACTION_USB_NOT_SUPPORTED:
//                Toast.makeText(mContext, "USB device not supported", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialStringEvent event) {
//        Log.d(TAG, "Data received (UsbSerial): " + event.data);
//        onUsbSerialString(event.data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MqttStringEvent event) {
//        Log.d(TAG, "Data received (Mqtt): " + event.topic + ":" + event.data);
//        onMqttMessage(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListEvent(ListEvent event) {
//        Log.d(TAG, "Data received (Mqtt): " + event.topicList.get(0) + ":" + event.messageList.get(0));
//        onListEventmessage(event.messageList,event.topicList);
    }

    protected void onListEventmessage(List<String> messageList,List<String> topicList) {
    }

    protected void onUsbSerialMessage(String message) {
    }

    protected void onUsbSerialFragmentMessage(String message) {
    }


    protected void onBluetoothMessage(IcCardMessage message) {
    }

    protected void onMqttMessage(MqttStringEvent event) {
    }

    protected void onUsbSerialString(String data) {
    }

    protected void onBluetoothString(String data) {
    }


//    protected void sendJsonMessage(IcCardMessage message) {
//        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
//                Collection.class, new FocusStrategy.CollectionAdapter()).create();
//
//        System.out.println("JSON; " + gson.toJson(message).toString());
//
////        Toast.makeText(mContext, "" + gson.toJson(message), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "JSON: " + gson.toJson(message));
//
//        // if UsbService was correctly binded, send data
//        if (mUsbService != null) {
//            mUsbService.write(gson.toJson(message).getBytes());
//            mUsbService.write("\n".getBytes());
//        }
//
////        // if Bluetooth service is correctly connected, send data
////        if (BluetoothSPP.getInstance().isServiceAvailable()) {
////            BluetoothSPP.getInstance().send(gson.toJson(message).getBytes(), false);
////            BluetoothSPP.getInstance().send("\n".getBytes(), false);
////        }
//    }


    protected void sendString(byte[] data) {
        // if UsbService was correctly binded, send data
        if (mUsbService != null) {
            mUsbService.write(data);
//            mUsbService.write("\n".getBytes());
        }

//        // if Bluetooth service is correctly connected, send data
//        if (BluetoothSPP.getInstance().isServiceAvailable()) {
//            BluetoothSPP.getInstance().send(data.getBytes(), false);
//            BluetoothSPP.getInstance().send("\n".getBytes(), false);
//        }
    }
}