package com.kt.myproject.utils.port;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
public class Host implements Serializable {

    private String hostname;
    private final String ip;
    private final byte[] address;
    private final String mac;
    private String vendor;

    /**
     * Constructs a host with a known IP and MAC.
     *
     * @param ip
     * @param mac
     */
    public Host(String ip, String mac) throws UnknownHostException {
        this.ip = ip;
        this.address = InetAddress.getByName(ip).getAddress();
        this.mac = mac;
    }

    /**
     * Returns this host's hostname
     *
     * @return
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets this host's hostname to the given value
     *
     * @param hostname Hostname for this host
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;

    }

    /**
     * Gets this host's MAC vendor.
     *
     * @return
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Returns this host's IP address
     *
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns this host's address in byte representation.
     *
     * @return
     */
    public byte[] getAddress() {
        return this.address;
    }

    /**
     * Returns this host's MAC address
     *
     * @return
     */
    public String getMac() {
        return mac;
    }

    /**
     * Starts a port scan
     *
     * @param ip        IP address
     * @param startPort The port to start scanning at
     * @param stopPort  The port to stop scanning at
     * @param timeout   Socket timeout
     * @param delegate  Delegate to be called when the port scan has finished
     */
    public static void scanPorts(String ip, int startPort, int stopPort, int timeout, HostAsyncResponse delegate) {
        new ScanPortsAsyncTask(delegate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ip, startPort, stopPort, timeout);
    }

    /**
     * Searches for the MAC vendor based on the provided MAc address.
     *
     * @param mac
     * @return
     * @throws IOException
     * @throws SQLiteException
     */
    public static String findMacVendor(String mac) throws SQLiteException {
        String prefix = mac.substring(0, 8);
        String notInDb = "Vendor not in database";
        char identifier = mac.charAt(1);
        if ("26ae".indexOf(identifier) != -1) {
            return notInDb + " (private address)";
        }

        if ("13579bdf".indexOf(identifier) != -1) {
            return notInDb + " (multicast address)";
        }

        return notInDb;
    }
}