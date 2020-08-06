//package com.sparkle.roam.services;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//
//import com.sparkle.roam.events.MqttStringEvent;
//
//import org.eclipse.paho.android.service.MqttAndroidClient;
//import org.eclipse.paho.client.mqttv3.IMqttActionListener;
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.IMqttToken;
//import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.MqttCallback;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.greenrobot.eventbus.EventBus;
//
//public class MqttService extends Service {
//    public static final String TAG = MqttService.class.getSimpleName();
//    public static final String MQTT_HOST = "tcp://mqtt.omnivoltaic.com";
//    public static final String USERNAME = "admin";
//    public static final String PASSWORD = "admin123";
//    private static final int CONNECT_TIMEOUT = 2000;
//    private final IBinder mBinder = new LocalBinder();
//    private MqttAsyncClient mMqttClient = null;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public void onDestroy() {
//        if (isConnected()) disconnect();
//    }
//
//    public void connect(final String uri, final String clieintId, final String username, final String password) {
//        String clientId = MqttClient.generateClientId();
////            mMqttClient = new MqttAsyncClient(MQTT_HOST, clientId, null);
//        mMqttClient.setCallback(new MyMqttCallback());
//
//        MqttConnectOptions options = new MqttConnectOptions();
////        options.setUserName(username);
////        options.setPassword(password.toCharArray());
//
//        try {
////            options = new MqttConnectOptions();
//            options.setUserName(USERNAME);
//            options.setPassword(PASSWORD.toCharArray());
//            final IMqttToken connectToken = mMqttClient.connect(options);
//            connectToken.waitForCompletion(CONNECT_TIMEOUT);
//        } catch (MqttException e) {
//            Log.d(TAG, "Connection attempt failed with reason code: " + e.getReasonCode() + ":" + e.getCause());
//            e.printStackTrace();
//        }
//    }
//
//    public boolean connect() {
//        try {
//            String clientId = MqttClient.generateClientId();
//            mMqttClient = new MqttAsyncClient(MQTT_HOST, clientId, null);
////            mMqttClient = new MqttAndroidClient(getApplicationContext(), MQTT_HOST, clientId);
//            mMqttClient.setCallback(new MyMqttCallback());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        MqttConnectOptions options = new MqttConnectOptions();
//
//        try {
//            options.setUserName(USERNAME);
//            options.setPassword(PASSWORD.toCharArray());
//            final IMqttToken connectToken = mMqttClient.connect(options);
////            final IMqttToken connectToken = mMqttClient.connect();
////            connectToken.waitForCompletion(CONNECT_TIMEOUT);
//
//            connectToken.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    subscribe("ROAM/PPID", 0);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                }
//            });
//        } catch (MqttException e) {
//            Log.d(TAG, "Connection attempt failed with reason code: " + e.getReasonCode() + ":" + e.getCause());
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public void publish(final String topic, final String payload) {
//        try {
//            MqttMessage mqttMessage = new MqttMessage();
//            mqttMessage.setPayload(payload.getBytes());
//            mMqttClient.publish(topic, mqttMessage);
//            Toast.makeText(getApplicationContext(),"published message on ROAM/PPID topic",Toast.LENGTH_SHORT).show();
//        } catch (MqttException e) {
//            Log.d(TAG, "Publish failed with reason code: " + e.getReasonCode());
//            e.printStackTrace();
//        }
//    }
//
//    public void subscribe(final String topic, int qos) {
//        try {
////            Toast.makeText(getApplicationContext(),"True",Toast.LENGTH_SHORT).show();
//            mMqttClient.subscribe(topic, qos);
//        } catch (MqttException e) {
//            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Subscribe failed with reason code: " + e.getReasonCode());
//            e.printStackTrace();
//        }
//    }
//
//    public void disconnect() {
//        try {
//            mMqttClient.disconnect();
//        } catch (MqttException e) {
//            Log.d(TAG, "Disconnect failed with reason code: " + e.getReasonCode());
//            e.printStackTrace();
//        }
//    }
//
//    public boolean isConnected() {
//        if (mMqttClient == null) return false;
//        return mMqttClient.isConnected();
//    }
//
//    public class LocalBinder extends Binder {
//        public MqttService getService() {
//            return MqttService.this;
//        }
//    }
//
//    public class MyMqttCallback implements MqttCallback {
//
//        @Override
//        public void connectionLost(Throwable cause) {
//            Log.d(TAG, "MQTT Server connection lost: " + cause.toString());
//            cause.printStackTrace();
//        }
//
//        @Override
//        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            Log.d(TAG, "Message arrived: " + topic + ":" + message.toString());
//            EventBus.getDefault().post(new MqttStringEvent(topic, message.toString()));
//            System.out.println("Data  --------"+message.toString());
//        }
//
//        @Override
//        public void deliveryComplete(IMqttDeliveryToken token) {
//            Log.d(TAG, "Delivery complete");
//        }
//
//
////        public void connectionLost(Throwable cause) {
////
////        }
////
////        public void messageArrived(String topic, MqttMessage message) {
////
////
////
////        }
////
////        public void deliveryComplete(IMqttDeliveryToken token) {
////
////        }
//    }
//
//    public void setCallback(){
//        mMqttClient.setCallback(new MyMqttCallback());
//    }
//}


package com.sparkle.csvreadapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.sparkle.csvreadapp.events.MqttStringEvent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

public class MqttService extends Service {
    public static final String TAG = MqttService.class.getSimpleName();
    private static final int CONNECT_TIMEOUT = 2000;
    private final IBinder mBinder = new LocalBinder();
    private MqttAsyncClient mMqttClient = null;

    public static final String MQTT_HOST = "tcp://mqtt.omnivoltaic.com";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin123";

    public class LocalBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if (isConnected()) disconnect();
    }


    public boolean connect() {
        try {
            String clientId = MqttClient.generateClientId();
            mMqttClient = new MqttAsyncClient(MQTT_HOST, clientId, null);

        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            final IMqttToken connectToken = mMqttClient.connect(options);
            connectToken.waitForCompletion(CONNECT_TIMEOUT);

//            Toast.makeText(this, "connection: "+mMqttClient.isConnected(), Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Toast.makeText(this, "Mqtt Connection failed ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Connection attempt failed with reason code: " + e.getReasonCode() + ":" + e.getCause());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void publish(final String topic, final String payload) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            mMqttClient.publish(topic, mqttMessage);
            Toast.makeText(this, "publish: "+mqttMessage, Toast.LENGTH_SHORT).show();
        }
        catch (MqttException e) {
            Log.d(TAG, "Publish failed with reason code: " + e.getReasonCode());
            e.printStackTrace();
        }
    }

    public void subscribe(final String topic, int qos) {
        try {
            mMqttClient.subscribe(topic, qos);
//            Toast.makeText(this, "subscribe: "+topic, Toast.LENGTH_SHORT).show();
            mMqttClient.setCallback(new MyMqttCallback());
        }
        catch (MqttException e) {
            Log.d(TAG, "Subscribe failed with reason code: " + e.getReasonCode());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mMqttClient.disconnect();
        } catch (MqttException e) {
            Log.d(TAG, "Disconnect failed with reason code: " + e.getReasonCode());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (mMqttClient == null) return false;
        return mMqttClient.isConnected();
    }

    public class MyMqttCallback implements MqttCallback {
        public void connectionLost(Throwable cause) {
            System.out.println("---------------------"+"MQTT Server connection lost: " + cause.toString());
            cause.printStackTrace();
        }

        public void messageArrived(String topic, MqttMessage message) {
            System.out.println("---------------------"+"Message arrived: " + topic + ":" + message.toString());
            EventBus.getDefault().post(new MqttStringEvent(topic, message.toString()));
        }

        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("---------------------"+"Delivery complete "+ Arrays.toString(token.getTopics()));
        }
    }
}
