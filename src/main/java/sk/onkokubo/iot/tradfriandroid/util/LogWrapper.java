package sk.onkokubo.iot.tradfriandroid.util;


import android.util.Log;

/**
 * Created by ondrejkubo on 04/06/2017.
 */

public final class LogWrapper {

    public static final void i(final String tag, final String message) {
        Log.i(tag, message);
    }

    public static final void d(final String tag, final String message) {
        Log.d(tag, message);
    }

    public static final void w(final String tag, final String message) {
        Log.w(tag, message);
    }

    public static final void e(final String tag, final String message) {
        Log.e(tag, message);
    }

    public static final void e(final String tag, final String message, final Throwable throwable) {
        Log.e(tag, message, throwable);
    }

}
