package com.kt.myproject.utils.port;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/13
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
public class PingOptions {
    private int timeoutMillis;
    private int timeToLive;

    public PingOptions() {
        timeToLive = 128;
        timeoutMillis = 1000;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = Math.max(timeoutMillis, 1000);
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = Math.max(timeToLive, 1);
    }
}
