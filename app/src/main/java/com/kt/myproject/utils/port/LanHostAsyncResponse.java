package com.kt.myproject.utils.port;

import android.util.SparseArray;

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface LanHostAsyncResponse {

    /**
     * Delegate to handle integer outputs
     *
     * @param output
     */
    void processFinish(int output);

    /**
     * Delegate to handle boolean outputs
     *
     * @param output
     */
    void processFinish(boolean output);

    /**
     * Delegate to handle Map outputs
     *
     * @param output
     */
    void processFinish(SparseArray<String> output);
}
