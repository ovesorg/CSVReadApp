package com.sparkle.csvreadapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.sparkle.csvreadapp.Base.BaseActivity;
import com.sparkle.csvreadapp.Utils.Constant;
import com.sparkle.csvreadapp.Utils.ConstantMethod;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends BaseActivity implements View.OnClickListener,ItemArrayAdapter.onItemClickListener {

    private ListView listView;

    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private ItemArrayAdapter itemArrayAdapter;
    private Button btn_select_file, btn_submit;
    private EditText editText,edit_oem_id;
    private TextView text_PAYG_ID;
    public static int ACTIVITY_CHOOSE_FILE1 = 5;
    List<String[]> scoreList;
    ImageButton imgbtn_close;
    MyPref myPref;
    StringBuilder stringBuilder;
    String ppidFromLUMN;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        btn_select_file = findViewById(R.id.btn_select_file);
        btn_submit = findViewById(R.id.btn_submit);
        btn_select_file.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        editText = findViewById(R.id.edit_text);
        edit_oem_id = findViewById(R.id.edit_oem_id);
        text_PAYG_ID = findViewById(R.id.text_PAYG_ID);
        imgbtn_close = findViewById(R.id.imgbtn_close);
        imgbtn_close.setOnClickListener(this);
        myPref = new MyPref(this);
        scoreList = new ArrayList<>();

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        goToPermissionCheck();

        edit_oem_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edit_oem_id.getText().toString().isEmpty())
                    imgbtn_close.setVisibility(View.VISIBLE);
                else
                    imgbtn_close.setVisibility(View.GONE);
            }
        });

    }

//
//    @Override
//    protected void onUsbReady() {
//        super.onUsbReady();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String read = ">HAND<";
//                byte[] readtmp = ConstantMethod.stringToByteArray(read);
//                Toast.makeText(MainActivity.this, "I'm passing >HAND< string into byte : "+readtmp, Toast.LENGTH_SHORT).show();
//                startReadingCard(readtmp);
//            }
//        }, 1000);
//
//    }
//
//
//    @Override
//    protected void onByteMessage(byte[] event) {
//        super.onByteMessage(event);
//        sendJsonMessage(event);
//    }
//
//    public void startReadingCard(byte[] data) {
//        sendJsonMessage(data);
//    }
//
//    public void onUsbSerialMessage(String message) {
//
//        if (stringBuilder == null) {
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(message);
//        } else
//            stringBuilder.append(message);
//
//        System.out.println("len: " + stringBuilder.toString().length());
//
//        Toast.makeText(MainActivity.this, ""+stringBuilder.toString(), Toast.LENGTH_SHORT).show();
//
//        if (stringBuilder.toString().contains("<OK>")){
//            myPref.setPref(Constant.LUMN_OK, true);
//            String read = ">INF<";
//            byte[] readtmp = ConstantMethod.stringToByteArray(read);
//            Toast.makeText(MainActivity.this, "I'm passing >INF< string into byte : "+readtmp, Toast.LENGTH_SHORT).show();
//            startReadingCard(readtmp);
//        }
//
//        if (stringBuilder.toString().length() >= 120){//138
//            //<INF:OPID:91051510000001,PPID:91051510000001 ,OCS: ENABLED,PS:FREE,RPD: 0000,TDP:0000,LVC:4F80DE119510D208,RT:0000,LCS:0000>
//            //<INF:OPID:M400180399012 ,PPID:M400180399012       ,OCS: ENABLED,PS:FREE,RPD: 1125D22H48M,TDP:9999,LVC:FB81B8B9C3725DBE,RT:00106,LCS:00004>
//            //10 14 6 20 6 7 4 4 6 11 5 4 5 16 4 5 5 5 1=138
//            String msg = stringBuilder.toString();
//            ppidFromLUMN = msg.substring(30, 50);
//            Toast.makeText(MainActivity.this, "LUMN_PPID"+ppidFromLUMN, Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_select_file:
               Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
               intent.addCategory(Intent.CATEGORY_OPENABLE);
               intent.setType("text/csv");
               intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
               startActivityForResult(Intent.createChooser(intent, "Open CSV"), ACTIVITY_CHOOSE_FILE1);
//               addcsvfile();
               break;

           case R.id.btn_submit:
               String filterText = edit_oem_id.getText().toString();
               if (itemArrayAdapter != null){
                   if (filterText.equals("")){
                       Toast.makeText(MainActivity.this, "Please enter OEM_ID.", Toast.LENGTH_SHORT).show();
                   }else{
                       itemArrayAdapter.filter(filterText);
                   }
               }else{
                   Toast.makeText(MainActivity.this, "Please select csv file.", Toast.LENGTH_SHORT).show();
               }
               break;

           case R.id.imgbtn_close:
               edit_oem_id.setText("");
               break;
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                addcsvfile();
            }
        }

        switch(requestCode) {
            case 5: {
                if (resultCode == RESULT_OK){
                    uri = data.getData();
                    File file = new File(data.getData().getPath());
                    editText.setText(file.getName());
                    myPref.setPref("FILE_NAME",file.getName());
                    myPref.setPref("FILE_PATH",data.getData().getPath());
                    proImportCSV(uri);
                }
            }
            break;

        }
    }

    private void proImportCSV(Uri uri){
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.single_list_item,this);
//               listView.setAdapter(itemArrayAdapter);
        try {
            System.out.println("------------uri----------"+uri);
            CSVReader reader = new CSVReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            scoreList = reader.readAll();
            for (String[] scoreData : scoreList){
                itemArrayAdapter.add(scoreData);
            }
//                   itemArrayAdapter.notifyList(scoreList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changevalue(String string) {
        text_PAYG_ID.setText(string);
        if (!string.equals("PAYG_ID not found")){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(string,string);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addcsvfile(){
        String filepath = myPref.getPref("FILE_PATH","");
        String filename = myPref.getPref("FILE_NAME","");
        if (!filename.equals("")){
            editText.setText(filename);
            String path = filepath.replace("/external_files","");
            System.out.println("-----------path-----------"+path);
            addcsvfile1(path);
        }else {
            addcsvfile1("/Download/OVES.csv");
        }

    }

    public void addcsvfile1(String string){
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.single_list_item,this);
//               listView.setAdapter(itemArrayAdapter);
        try {
            File csvfile = new File(Environment.getExternalStorageDirectory() + string);
            if (csvfile.exists()){
                editText.setText("OVES.csv");
                CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
                scoreList = reader.readAll();
                for (String[] scoreData : scoreList){
                    itemArrayAdapter.add(scoreData);
                }
//                   itemArrayAdapter.notifyList(scoreList);
            }else {
                InputStream inputStream = getResources().openRawResource(R.raw.oves);
                CSVReader1 csvFile = new CSVReader1(inputStream);
                scoreList = csvFile.read();
                editText.setText("OVES.csv");

                for(String[] scoreData : scoreList ) {
                    itemArrayAdapter.add(scoreData);
                }
//                   itemArrayAdapter.notifyList(scoreList);

//                Toast.makeText(MainActivity.this, "Please select csv file.", Toast.LENGTH_SHORT).show();
//                itemArrayAdapter = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
    }
//450  1351
    private void goToPermissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {

                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission

                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs Storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.setCancelable(false);
                builder.show();
            }
            else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs Storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions and grant permission to let App works properly.", Toast.LENGTH_LONG).show();
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.setCancelable(false);
                builder.show();
            }
            else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            addcsvfile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                readFile();
//            } else {
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                addcsvfile();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs Storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/
                builder.setCancelable(false);
                builder.show();
            } else {
                this.finish();
                Toast.makeText(getBaseContext(), "Sorry, you have denied permissions.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                addcsvfile();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

}
