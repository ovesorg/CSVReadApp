package com.sparkle.csvreadapp.Utils;


import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sparkle.csvreadapp.BuildConfig;
import com.sparkle.csvreadapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Admin on 26-04-2018.
 */

public class ConstantMethod {

    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

    public static boolean Checkemail(String s) {
        Pattern sPattern = Pattern.compile(Constant.EMAIL_PATTERN);

        return sPattern.matcher(s).matches();

    }

    public static int getIntColor(String color) {
        float f = Float.parseFloat(color) * 255;
        return (int) f;
    }

    private static byte char2byte(char achar) {
        byte b = (byte) "0123456789ABCDEF".indexOf(Character.toUpperCase(achar));
        return b;

    }

    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

    public static final byte[] hexStr2Bytes(String hexStr) {
        hexStr = hexStr.replace(" ", ""); //tang
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (char2byte(achar[pos]) << 4 | char2byte(achar[pos + 1]));
        }
        return result;
    }

    public static final String bytes2HexStr(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] stringToByteArray(String s) {
        byte[] bytes = s.getBytes();
//        byte[] b = new byte[s.length() / 2];
//        for (int i = 0; i < b.length; i++) {
//            int index = i * 2;
//            int v = Integer.parseInt(s.substring(index, index + 2), 16);
//            b[i] = (byte) v;
//        }
        return bytes;
    }

    public static final String bytes2Str(byte[] bArray) {
        String s = new String(bArray);
        return s.toString();
    }





    public static String decodeFromNonLossyAscii(String original) {
        Matcher matcher = UNICODE_HEX_PATTERN.matcher(original);
        StringBuffer charBuffer = new StringBuffer(original.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 16);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        String parsedUnicode = charBuffer.toString();

        matcher = UNICODE_OCT_PATTERN.matcher(parsedUnicode);
        charBuffer = new StringBuffer(parsedUnicode.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 8);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        return charBuffer.toString();
    }

    public static String encodeToNonLossyAscii(String original) {
        Charset asciiCharset = Charset.forName("US-ASCII");
        if (asciiCharset.newEncoder().canEncode(original)) {
            return original;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            if (c < 128) {
                stringBuffer.append(c);
            } else if (c < 256) {
                String octal = Integer.toOctalString(c);
                stringBuffer.append("\\");
                stringBuffer.append(octal);
            } else {
                String hex = Integer.toHexString(c);
                stringBuffer.append("\\u");
                stringBuffer.append(hex);
            }
        }
        return stringBuffer.toString();
    }

    public static void openAppRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + context.getPackageName()));

        // ComponentName componentName = new
        // ComponentName("com.android.vending",
        // "com.mobilechamps.Hi5GIF");
        // rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        // | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // rateIntent.setComponent(componentName);
        context.startActivity(rateIntent);

    }

    public static void startInstalledAppDetailsActivity(Context context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static boolean Checkpass_username(String s) {
        Pattern sPattern = Pattern.compile(Constant.PASSWORD_PATTERN);

        return sPattern.matcher(s).matches();

    }

//    public static void singOut(final Context context) {
//        if (isConnected(context)) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    ((MyApplication) context.getApplicationContext()).getDatabase().clearAllTables();
//
//                }
//            }).start();
//            new MyPref(context).clearPref();
//            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        } else {
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.no_network), true);
//        }
//    }

    public static boolean isConnected(Context ctx) {
        ConnectivityManager
                cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static String ConvertProperUl(String url) {
        if (url != null && url.length() > 5) {
            String substr = url.substring(0, 4);
            if (substr.equals("http"))
                return url;
            else
                return Constant.IMAGE_PATH + url;
        } else
            return "";
    }

    public static void ShowInstract(Context context) {
        SharedPreferences sharedpreferences = context
                .getSharedPreferences(Constant.INTRO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Constant.IS_SHOW_INTRO, true);
        editor.commit();
    }

    public static boolean IsCheckInstract(Context context) {
        SharedPreferences sharedpreferences = context
                .getSharedPreferences(Constant.INTRO_PREF, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(Constant.IS_SHOW_INTRO, false);
    }

    public static void scaleAnimation(final View startView, View finishView) {
        int startViewLocation[] = new int[2];
        startView.getLocationInWindow(startViewLocation);
        int finishViewLocation[] = new int[2];
        finishView.getLocationInWindow(finishViewLocation);
        int startX = startViewLocation[0] + startView.getWidth() / 2;
        int startY = startViewLocation[1] + startView.getHeight() / 2;
        int endX = finishViewLocation[0] + finishView.getWidth() / 2;
        int endY = finishViewLocation[1] + finishView.getHeight() / 2;
        ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0, 1f,
                Animation.ABSOLUTE, endX - startX + startView.getWidth() / 2,
                Animation.ABSOLUTE, endY - startY + startView.getHeight() / 2);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        // animation.scaleCurrentDuration(6000);
        animation.setDuration(350);
        // animation.setStartOffset(50);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                startView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        startView.startAnimation(animation);
    }

    public static void scaleAnimationReverse(final View startView, View finishView) {
        int startViewLocation[] = new int[2];
        startView.getLocationInWindow(startViewLocation);
        int finishViewLocation[] = new int[2];
        finishView.getLocationInWindow(finishViewLocation);
        int startX = startViewLocation[0] + startView.getWidth() / 2;
        int startY = startViewLocation[1] + startView.getHeight() / 2;
        int endX = finishViewLocation[0] + finishView.getWidth() / 2;
        int endY = finishViewLocation[1] + finishView.getHeight() / 2;
        ScaleAnimation animation = new ScaleAnimation(1f, 0f, 1, 0f,
                Animation.ABSOLUTE, endX - startX + startView.getWidth() / 2,
                Animation.ABSOLUTE, endY - startY + startView.getHeight() / 2);
        // animation.scaleCurrentDuration(6000);
        animation.setDuration(350);
        // animation.setStartOffset(50);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startView.setVisibility(View.INVISIBLE);
            }
        });
        startView.startAnimation(animation);
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

//    public static Location GetLocation(Context context, boolean showsettingdialog) {
//        GPSTracker gpsTracker = new GPSTracker(context);
//        if (gpsTracker.getIsGPSTrackingEnabled()) {
//            Location userCurrentLocation = new Location("Location1");
//            userCurrentLocation.setLatitude(gpsTracker.getLatitude());
//            userCurrentLocation.setLongitude(gpsTracker.getLongitude());
//            if (gpsTracker.getLatitude() == 0 && gpsTracker.getLongitude() == 0 && showsettingdialog)
//                showGPSAlert(context);
//            // gpsTracker.showSettingsAlert();
//            return userCurrentLocation;
//        } else {
//            if (showsettingdialog)
//                showGPSAlert(context);
//            // gpsTracker.showSettingsAlert();
//            return null;
//        }
//    }

    public static void replaceFragment(FragmentManager manager, Fragment fragment, int view, boolean isAnimate) {
        String fragmentTag = fragment.getClass().getName();

        boolean fragmentPopped;
        try {
            fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);
        } catch (Exception e) {
            fragmentPopped = false;
        }
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            if (isAnimate)
//                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right,
//                        R.anim.right_to_left);
            ft.replace(view, fragment, fragmentTag);
            ft.addToBackStack(fragmentTag);
            ft.commit();
        }
    }
    public static void replacesFragment(FragmentManager manager, Fragment fragment, int view, boolean isAnimate) {
//        String fragmentTag = fragment.getClass().getName();

        boolean fragmentPopped;
//        try {
//            fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);
//        } catch (Exception e) {
//            fragmentPopped = false;
//        }
//        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            if (isAnimate)
//                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right,
//                        R.anim.right_to_left);
            ft.replace(view, fragment, "");
//            ft.addToBackStack(fragmentTag);
            ft.addToBackStack(null);
            ft.commit();
//        }
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }


//    public static <T> void addToHistory(Context context, String prefName, T myObject) {
//        Gson gson = new Gson();
//        if (context != null) {
//            MyPref myPref = new MyPref(context);
//            String json = gson.toJson(myObject);
//            myPref.setPref(prefName, json);
//        }
//    }

    public static JSONArray getJsonArray(String string) {
        if (string == null)
            return null;
        try {
            return new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static List<PhoneNumber> getPhoneNumbers(String phonenumber) {
//        List<PhoneNumber> numbers = new ArrayList<>();
//        if (phonenumber == null) {
//            numbers.add(new PhoneNumber("", ""));
//            return numbers;
//        }
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(phonenumber);
//            Iterator<String> iter = jsonObject.keys();
//            while (iter.hasNext()) {
//                String key = iter.next();
//                numbers.add(new PhoneNumber(key, jsonObject.getString(key)));
//
//            }
//        } catch (JSONException e) {
//            numbers.add(new PhoneNumber("Contacts", phonenumber));
//            return numbers;
//        }
//
//        return numbers;
//    }

    public static String getNumber(String phonenumber) {
        if (phonenumber == null)
            return "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(phonenumber);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                return jsonObject.getString(key);

            }
        } catch (JSONException e) {
            return "";
        }

        return "";
    }

//    public static boolean validatePhone(String code, String editText) {
//
//
//        try {
//
//            PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
////            code = tv_country_code.getText().toString().trim();
//            Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(
//                    code
//                            + editText, null);
//            // StringBuilder sb = new StringBuilder(16);
//            // sb.append('+').append(p.getCountryCode()).append(p.getNationalNumber());
//
//            return mPhoneNumberUtil.isValidNumber(p);
//            // phone = sb.toString();
//            // region = mPhoneNumberUtil.getRegionCodeForNumber(p);
//        } catch (NumberParseException ignore) {
//            return false;
//        }
//
//    }

//    public static void showGPSAlert(final Context mContext) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setCancelable(true);
//        builder.setTitle(R.string.GPSAlertError);
//        builder.setMessage(R.string.GPSAlertDialogMessage);
//        builder.setInverseBackgroundForced(true);
//        builder.setPositiveButton(R.string.action_settings,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        mContext.startActivity(intent);
//                    }
//                });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    public static String GooglePlaces(String locationstring) {

        return "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + locationstring.replaceAll(" ", "%20") + "&types=geocode"
                + "&key=AIzaSyAjShnMt7fIXolGE0Wz6j3yJ1TVlLZ9Fh0&mode=driving&transit_mode=bus";

    }

    public static String GooglePlacesDetail(String places) {

        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + places
                + "&key=AIzaSyAjShnMt7fIXolGE0Wz6j3yJ1TVlLZ9Fh0";
    }

//    public static Bitmap getMapMarker(Context context) {
//        // Bitmap bmp = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_map_marker);
//        return Bitmap.createScaledBitmap(largeIcon, 111, 177, true);
//    }

//    public static Bitmap getBusinessMarker(Context context) {
//        // Bitmap bmp = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_business_marker);
////        return largeIcon;
//        return Bitmap.createScaledBitmap(largeIcon, 73, 73, true);
//    }

//    public static Bitmap getSelectedBusMarker(Context context) {
//        // Bitmap bmp = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_business_marker_selected);
////        return largeIcon;
//        return Bitmap.createScaledBitmap(largeIcon, 73, 117, true);
//    }

//    public static Bitmap getCommonMarker(Context context) {
//        // Bitmap bmp = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_common_marker);
////        return largeIcon;
//        return Bitmap.createScaledBitmap(largeIcon, 117, 117, true);
//    }

    public static double MeasureDistance(float latA, float lngA, float latB, float lngB) {
        double distance;
        Location locationA = new Location("");
        locationA.setLatitude(latA);
        locationA.setLongitude(lngA);

        Location locationB = new Location("");
        locationB.setLatitude(latB);
        locationB.setLongitude(lngB);

        distance = locationA.distanceTo(locationB) / 1000;   // in meters
        return distance;
    }


    public static String getFullAddress(Context context, double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Toast.makeText(context, "Service Not available", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Toast.makeText(context, "Invalid Parameters", Toast.LENGTH_LONG).show();
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            return null;
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            return TextUtils.join(", ", addressFragments);
        }
    }

    //for kuwait
    public static String getFormatAddress(String string, String zipcode) {
        if (string == null) {
            if (zipcode == null)
                return "";
            else
                return zipcode;
        }
        String addresStr = "";
        JSONObject jsonObject = null;
        String temp = "";
        try {
            jsonObject = new JSONObject(string);
        } catch (JSONException e) {
            return string;
        }
        try {

            String businessArea = "" + jsonObject.get("bussiness_area");
            temp = businessArea.replaceAll("\\s", "");
            if (!businessArea.isEmpty() && !businessArea.equals("") && temp != null && !temp.equals(""))
                addresStr = "" + businessArea;
        } catch (Exception ex) {
            return addresStr;
        }
        try {
            String bussiness_block = jsonObject.getString("bussiness_block");
            temp = bussiness_block.replaceAll("\\s", "");
            if (!bussiness_block.isEmpty() && !bussiness_block.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_block);
                else
                    addresStr = "" + bussiness_block;
            }
        } catch (Exception e) {
        }
        try {
            String bussiness_street = jsonObject.getString("bussiness_street");
            temp = bussiness_street.replaceAll("\\s", "");
            if (!bussiness_street.isEmpty() && !bussiness_street.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_street);
                else
                    addresStr = "" + bussiness_street;
            }
        } catch (Exception e) {
        }


        try {
            String bussiness_street2 = jsonObject.getString("bussiness_street2");
            temp = bussiness_street2.replaceAll("\\s", "");
            if (!bussiness_street2.isEmpty() && !bussiness_street2.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_street2);
                else
                    addresStr = "" + bussiness_street2;
            }
        } catch (Exception e) {
        }

        try {
            String bussiness_lane = jsonObject.getString("bussiness_lane");
            temp = bussiness_lane.replaceAll("\\s", "");
            if (!bussiness_lane.isEmpty() && !bussiness_lane.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_lane);
                else
                    addresStr = "" + bussiness_lane;
            }
        } catch (Exception e) {
        }

        try {
            String bussiness_floor = jsonObject.getString("bussiness_floor");
            temp = bussiness_floor.replaceAll("\\s", "");
            if (!bussiness_floor.isEmpty() && !bussiness_floor.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_floor);
                else
                    addresStr = "" + bussiness_floor;
            }
        } catch (Exception e) {
        }

        try {
            String bussiness_in = jsonObject.getString("bussiness_in");
            temp = bussiness_in.replaceAll("\\s", "");
            if (!bussiness_in.isEmpty() && !bussiness_in.equals("") && temp != null && !temp.equals("")) {
                if (!addresStr.isEmpty() && !addresStr.equals(""))
                    addresStr += (", " + bussiness_in);
                else
                    addresStr = "" + bussiness_in;
            }
        } catch (Exception e) {
        }


        if (zipcode != null && !zipcode.equals(""))
            addresStr += ("-" + zipcode);

        return addresStr;
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String countryName = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }

        return countryName;
    }

    public static String getCityName(Context context, double latitude, double longitude) {
        List<Address> addresses = null;

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.

        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
        } else {
            Address address = addresses.get(0);
            String cityName;

            cityName = address.getSubAdminArea();
            if (cityName == null || cityName.isEmpty())
                cityName = address.getLocality();

            if (cityName == null || cityName.isEmpty())
                cityName = address.getAdminArea();

            return cityName;
        }
        return null;

    }

    public static String getImageUrl(String url) {
        if (url != null) {
            if (url.startsWith("http://") || url.startsWith("https://"))
                return url;
            else
                return Constant.BASE_URL + url;
        }
        return null;
    }

    public static String getDistance(String distance) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        String mDistance = "";
        try {
            mDistance = decimalFormat.format(Double.parseDouble(distance)) + " km";
            return mDistance;
        } catch (NumberFormatException e) {
            return mDistance + "0 km";
        }
    }


    public static int getPxFromDp(Context context, float dp) {
        if(context!=null) {
            Resources r = context.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
        return 0;
    }

    public static void dialNumber(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        context.startActivity(intent);
    }

    public static void sendDefultsharingintent(Context context, String text,
                                               String subject) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");

        // if (filename != null)
        // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filename));

        if (text != null && !text.equals("")) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text));
        } else
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    Html.fromHtml("https://play.google.com/store/apps/details?id="
                            + context.getPackageName()));

        try {
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            context.startActivity(Intent.createChooser(shareIntent, "Share"));
        } catch (android.content.ActivityNotFoundException ex) {

        }

    }

//    public static void redirectSMS(Context context, String number, String message) {
//        try {
//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.setData(Uri.parse("sms:" + number));
//            sendIntent.putExtra("sms_body", message);
//            context.startActivity(sendIntent);
//        } catch (Exception e) {
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.sms_not_available), true);
////            new WarningDialog(context, context.getResources().getString(R.string.oops),
////                    context.getResources().getString(R.string.sms_not_available),
////                    Constant.Dialogcode.WARNING).show();
//        }
//
//    }
//
//    public static void redirectWhatsapp(Context context, String number) {
//        String formattedNumber = formatToE164(number);
//        try {
//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
//            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
//            sendIntent.setPackage("com.whatsapp");
//            context.startActivity(sendIntent);
//        } catch (Exception e) {
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.whatsapp_not_available), true);
////            new WarningDialog(context, context.getResources().getString(R.string.oops),
////                    context.getResources().getString(R.string.whatsapp_not_available),
////                    Constant.Dialogcode.WARNING).show();
//        }
//    }

    public static String formatToE164(String number) {
        if (number.startsWith("+")) {
            return number.substring(1);
        }
        return number;
    }

//    public static void redirectViber(Context context, String number) {
//        Intent intent = context.getPackageManager()
//                .getLaunchIntentForPackage("com.viber.voip");
//        if (intent != null) {
//            Intent skyintent = new Intent();
//            skyintent.setAction(Intent.ACTION_SEND);
//            skyintent.setPackage("com.viber.voip");
//            skyintent.setType("text/plain");
//            skyintent.putExtra(Intent.EXTRA_TEXT, "" + number);
//            context.startActivity(skyintent);
//        } else
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.viber_not_available), true);
////        new WarningDialog(context, context.getResources().getString(R.string.oops),
////                    context.getResources().getString(R.string.viber_not_available),
////                    Constant.Dialogcode.WARNING).show();
//    }

//    public static void redirectSkype(Context context, String number) {
//        Intent intent = context.getPackageManager()
//                .getLaunchIntentForPackage("com.skype.raider");
//        if (intent != null) {
//            Intent skyintent = new Intent();
//            skyintent.setAction(Intent.ACTION_VIEW);
//            skyintent.setPackage("com.skype.raider");
//            skyintent.setData(Uri.parse("skype:" + number));
//            context.startActivity(skyintent);
//        } else
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.skype_not_available), true);
////        new WarningDialog(context, context.getResources().getString(R.string.oops),
////                    context.getResources().getString(R.string.skype_not_available),
////                    Constant.Dialogcode.WARNING).show();
//
//    }

//    public static void redirectSnapchat(Context context) {
//        Intent intent = context.getPackageManager()
//                .getLaunchIntentForPackage("com.snapchat.android");
//        if (intent != null) {
//            Intent snapchat_intent = new Intent(Intent.ACTION_SEND);
//            snapchat_intent.setType("*/*");
//            snapchat_intent.setType("text/plain");
//            snapchat_intent.setPackage("com.snapchat.android");
//            context.startActivity(snapchat_intent);
//        } else
//            ((MainActivity) context).ShowPopupFromTop(context.getResources().getString(R.string.snapchat_not_available), true);
//
////        new WarningDialog(context, context.getResources().getString(R.string.oops),
////                    context.getResources().getString(R.string.snapchat_not_available),
////                    Constant.Dialogcode.WARNING).show();
//
//    }

    public static void sendEmail(String email, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show();
        }
    }




//    @SuppressWarnings("deprecation")
//    public static Spanned checkCommentMention(String comment, List<ChatMember> memberList) {
//        if (comment.contains("@")) {
////            SparseIntArray sparseIntArray = new SparseIntArray();
//            for (ChatMember chatMember : memberList) {
//                String mention = "@" + chatMember.getCustomer_fullname();
//                int pos = comment.indexOf(mention);
////                if (pos >= 0) {
////
////                }
//                while (pos >= 0) {
//                    String before = comment.substring(0, pos);
//                    String after = comment.substring(pos + mention.length());
//                    int length = comment.length();
//                    comment = before + "<font color=\"#114499\">" + mention + "</font>" + after;
//                    pos = comment.indexOf(mention, pos + (comment.length() - length) + 1);
//                }
////                while (pos >= 0) {
////                    pos = comment.indexOf(mention, pos + 1);
////
////                }
//            }
//
////            Log.d(ConstantMethod.class.getSimpleName(), comment);
////            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(comment);
////            for (int i = 0; i < sparseIntArray.size(); i++) {
////                int start = sparseIntArray.keyAt(i);
////                int end = sparseIntArray.get(start);
//
////                String before = comment.substring(0, start);
////                String mention = comment.substring(start, end);
////                String after = comment.substring(start + end);
////                comment = before + "<font color=\"#114499\">" + mention + "</font>" + after;
////                Log.d(ConstantMethod.class.getSimpleName(), "start: " + start + ", end: " + end);
////                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                spannableStringBuilder.setSpan(
////                        new ForegroundColorSpan(Color.parseColor("#114499")),
////                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////            }
//
//
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return Html.fromHtml(comment, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            return Html.fromHtml(comment);
//        }
//    }

    public static List<Integer> searchComment(Pattern pattern, String matcher) {
        Matcher m = pattern.matcher(matcher.toLowerCase());
        List<Integer> matchList = new ArrayList<>();
        while (m.find()) {
            matchList.add(m.start());
        }
        return matchList;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static String getMilliseconds(String mDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date d = simpleDateFormat.parse(mDate);
            return String.valueOf(d.getTime());
        } catch (ParseException e) {
            return mDate;
        }
    }

    public static String getChatTime(String milliseconds) {
        long millis = Long.parseLong(milliseconds);

        if (DateUtils.isToday(millis)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(millis);
        } else if (isYesterday(millis)) {
            return "Yesterday";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(millis);
        }

    }

    public static String getBoardDate(String source) {
        if(source!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date sDate = simpleDateFormat.parse(source);
                SimpleDateFormat dSimpleDateFormat = new SimpleDateFormat("MMMM dd");
                dSimpleDateFormat.setTimeZone(TimeZone.getDefault());
                return dSimpleDateFormat.format(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
            return "";
    }

    public static String getNotificationYear(String milliseconds) {
        long millis = Long.parseLong(milliseconds);

        if (DateUtils.isToday(millis)) {
            return "Today";
        } else if (isYesterday(millis)) {
            return "Yesterday";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(millis);
        }
    }

    public static String getNotificationDate(String currentDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date d = simpleDateFormat.parse(currentDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(d);
        } catch (ParseException e) {
            return currentDate;
        }
    }

    public static String getCommentDay(String milliseconds) {
        long millis = Long.parseLong(milliseconds);

        if (DateUtils.isToday(millis)) {
            return "Today";
        } else if (isYesterday(millis)) {
            return "Yesterday";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(millis);
        }
    }

    public static String getCommentTime(String milliseconds) {
        long millis = Long.parseLong(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(millis);
    }

    public static String convertArchiveDate(String milliseconds) {
        long millis = Long.parseLong(milliseconds);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return simpleDateFormat.format(millis);
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeZone(TimeZone.getDefault());
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static String getCurrentServerTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(System.currentTimeMillis());
    }


    public static final Map<String, Long> times = new LinkedHashMap<>();

    static {
        times.put("year", TimeUnit.DAYS.toMillis(365));
        times.put("month", TimeUnit.DAYS.toMillis(30));
        times.put("week", TimeUnit.DAYS.toMillis(7));
        times.put("day", TimeUnit.DAYS.toMillis(1));
        times.put("hour", TimeUnit.HOURS.toMillis(1));
        times.put("minute", TimeUnit.MINUTES.toMillis(1));
        times.put("second", TimeUnit.SECONDS.toMillis(1));
    }

    public static String toRelative(long duration, int maxLevel) {
        StringBuilder res = new StringBuilder();
        int level = 0;
        for (Map.Entry<String, Long> time : times.entrySet()) {
            long timeDelta = duration / time.getValue();
            if (timeDelta > 0) {
                res.append(timeDelta)
                        .append(" ")
                        .append(time.getKey())
                        .append(timeDelta > 1 ? "s" : "")
                        .append(", ");
                duration -= time.getValue() * timeDelta;
                level++;
            }
            if (level == maxLevel) {
                break;
            }
        }
        if ("".equals(res.toString())) {
            return "Just Now";
        } else {
            res.setLength(res.length() - 2);
            res.append(" ago");
            return res.toString();
        }
    }

    public static String toRelative(long duration) {
        return toRelative(duration, 1);
    }

    public static String toRelative(Date start, Date end) {
        assert start.after(end);
        return toRelative(end.getTime() - start.getTime());
    }

    public static String getRelativeTime(String input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date start = simpleDateFormat.parse(input);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date end = calendar.getTime();
            return toRelative(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return input;
    }


}
