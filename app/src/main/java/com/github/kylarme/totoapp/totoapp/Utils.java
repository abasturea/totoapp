package com.github.kylarme.totoapp.totoapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.kylarme.totoapp.R;

public final class Utils {

    private static Toast sToastMessage;

    /**
     * Hides the soft keyboard.
     *
     * @param activity the activity.
     */
    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Checks for internet connection.
     *
     * @param context the context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Displays a message.
     *
     * @param context  The context to use. Usually your Application or Activity object.
     * @param text     The text to show. Can be formatted text.
     * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        if (sToastMessage != null) {
            sToastMessage.cancel();
        }

        sToastMessage = Toast.makeText(context, text, duration);

        sToastMessage.show();
    }

    /**
     *
     */
    public static class UpdateService {

        /**
         *
         */
        public static final int STOPPED = 0;

        /**
         *
         */
        public static final int RUNNING = 1;

        /**
         *
         */
        public static final int UPDATE_TIME_15_MINUTES = 15 * 60 * 1000;

        /**
         *
         */
        public static final int UPDATE_TIME_30_MINUTES = 30 * 60 * 1000;

        /**
         *
         */
        public static final int UPDATE_TIME_60_MINUTES = 1 * 60 * 60 * 1000;

        /**
         *
         */
        public static final int UPDATE_TIME_180_MINUTES = 3 * 60 * 60 * 1000;

        /**
         * @param activity
         * @param serviceClass
         * @return
         */
        public static int getServiceStatus(Activity activity, Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {

                    return RUNNING;
                }
            }

            return STOPPED;
        }

        /**
         * @param context
         * @return
         */
        public static int getCurrentUpdateTime(Context context) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            String refreshRatePref = context.getResources().getString(R.string.pref_refresh_rate);

            String updateTime = sharedPreferences.getString(refreshRatePref, "60");

            return mapUpdateTime(context, updateTime);
        }

        /**
         * @param context
         * @param updateTime
         * @return
         */
        public static int mapUpdateTime(Context context, String updateTime) {

            if (updateTime == null) {
                return -1;
            }

            String[] time = context.getResources().getStringArray(R.array.pref_refresh_rate_elements_values);

            if (updateTime.equals(time[0]))
                return UPDATE_TIME_15_MINUTES;

            if (updateTime.equals(time[1]))
                return UPDATE_TIME_30_MINUTES;

            if (updateTime.equals(time[2]))
                return UPDATE_TIME_60_MINUTES;

            if (updateTime.equals(time[3]))
                return UPDATE_TIME_180_MINUTES;

            return -1;
        }
    }

    /**
     *
     */
    public static class Device {

        /**
         * Device phone type.
         */
        public static final int PHONE = 0;

        /**
         * Device tablet type.
         */
        public static final int TABLET = 1;

        /**
         * Gets the device type based on the dpi density.
         *
         * @param activity the activity.
         * @return the device type.
         */
        public static int getDeviceType(Activity activity) {

            // TODO: change returned values to correspond the Configuration

            int screen = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

            switch (screen) {
                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED: {
                    return PHONE;
                }

                case Configuration.SCREENLAYOUT_SIZE_SMALL: {
                    return PHONE;
                }

                case Configuration.SCREENLAYOUT_SIZE_NORMAL: {
                    return PHONE;
                }

                case Configuration.SCREENLAYOUT_SIZE_LARGE: {
                    return TABLET;
                }

                case Configuration.SCREENLAYOUT_SIZE_XLARGE: {
                    return TABLET;
                }
                default: {
                    return PHONE;
                }
            }
        }

        /**
         * Sets the screen orientation mode.
         *
         * @param activity    the activity.
         * @param orientation the ActivityInfo type param
         */
        public static void setScreenOrientation(Activity activity, int orientation) {
            activity.setRequestedOrientation(orientation);
        }

        /**
         * Gets the device screen orientation.
         *
         * @param activity the activity.
         * @return the device screen orientation.
         */
        public static int getDeviceScreenOrientation(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int orientation;

            if (metrics.widthPixels < metrics.heightPixels) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }

            return orientation;
        }
    }
}

