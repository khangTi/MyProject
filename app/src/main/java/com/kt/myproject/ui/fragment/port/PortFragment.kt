package com.kt.myproject.ui.fragment.port

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.util.SparseArray
import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.PortBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.utils.port.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger


class PortFragment : BaseFragment<PortBinding>(PortBinding::inflate) {

    private val adapterPortInIp = PortAdapter()

    private val adapterHostInPort = PortAdapter()

    private val listPortInIp = mutableListOf<String>()

    private val listHostInPort = mutableListOf<String>()

    private var wifi: Wireless? = null

    private val startPort = 1

    private val endPort = 1024

    override fun onViewCreated() {
        wifi = Wireless(requireContext().applicationContext)
        binding.portActionGet.actionClickListener { scanPortIp() }
        binding.portActionIp.actionClickListener { scanIpOpenPort() }
    }

    override fun onLiveDataObserve() {
    }

    private fun scanPortIp() {
        if (wifi?.isConnectedWifi == false) {
            toast("not connect network")
            return
        }
        listPortInIp.clear()
        val ip = "10.10.0.1"/*requireActivity().getIpAdress()*/
        Host.scanPorts(ip, startPort, endPort, 4000,
            object : HostAsyncResponse {
                override fun processFinish(output: Int) {}

                override fun processFinish(output: Boolean) {
                    log.d("$output")
                }

                override fun processFinish(output: SparseArray<String>?) {
                    output ?: return
                    val scannedPort = output.keyAt(0)
                    var item: String = scannedPort.toString()
                    log.d("processFinish $item")
                    listPortInIp.add(item)
                    lifecycleScope.launch(Dispatchers.Main) {
                        adapterPortInIp.set(listPortInIp)
                        adapterPortInIp.bind(binding.portRecyclerView)
                    }
                }

                override fun <T : Throwable?> processFinish(output: T) {}

            })
        log.d(ip)
    }

    private fun scanIpOpenPort() {
        if (wifi?.isEnabled == false) {
            toast("wifi not enable")
            return
        }
        if (wifi?.isConnectedWifi == false) {
            toast("wifi not connect network")
            return
        }
        /*val a = NetworkSniffTask(context).execute()*/
        val wifiManager = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress
        ScanHostsAsyncTask(object : MainAsyncResponse{
            override fun processFinish(h: Host?, i: AtomicInteger?) {
                log.e("scanIpOpenPort $h")
            }

            override fun processFinish(output: Int) {
                print("")
            }

            override fun processFinish(output: String?) {
                print("")
            }

            override fun processFinish(output: Boolean) {
                print("")
            }

            override fun <T : Throwable?> processFinish(output: T) {
                print("")
            }

        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 168427745, 24, 150);
    }

    override fun onResume() {
        super.onResume()

    }

}