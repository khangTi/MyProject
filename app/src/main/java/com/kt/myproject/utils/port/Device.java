package com.kt.myproject.utils.port;

import java.net.InetAddress;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/13
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
public class Device {
    public String ip;
    public String hostname;
    public String mac;
    public float time = 0;

    public Device(InetAddress ip) {
        this.ip = ip.getHostAddress();
        this.hostname = ip.getCanonicalHostName();
    }

    @Override
    public String toString() {
        return "Device{" +
                "ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", mac='" + mac + '\'' +
                ", time=" + time +
                '}';
    }
}

