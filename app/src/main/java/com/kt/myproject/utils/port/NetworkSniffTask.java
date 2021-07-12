package com.kt.myproject.utils.port;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
public class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "NetworkSniffTask";

    private WeakReference<Context> mContextRef;

    public NetworkSniffTask(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "Let's sniff the network");

        try {
            Context context = mContextRef.get();

            if (context != null) {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);


                Log.d(TAG, "activeNetwork: " + activeNetwork);
                Log.d(TAG, "ipString: " + ipString);

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);

                for (int i = 0; i < 255; i++) {
                    String testIp = prefix + i;

                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(100);
                    String hostName = address.getCanonicalHostName();

                    if (reachable) {
                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                    } else {
                        Log.i(TAG, i + "");
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
        }

        return null;
    }


}