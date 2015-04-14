package com.alex.totoapp.totoapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi <= DisplayMetrics.DENSITY_LOW) {

                return PHONE;
            }

            if (metrics.densityDpi >= DisplayMetrics.DENSITY_LOW &&
                    metrics.densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {

                return PHONE;
            }

            if (metrics.densityDpi >= DisplayMetrics.DENSITY_MEDIUM &&
                    metrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {

                return TABLET;
            }

            if (metrics.densityDpi >= DisplayMetrics.DENSITY_MEDIUM) {

                return TABLET;
            }

            return PHONE;
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
