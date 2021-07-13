package com.kt.myproject.ui.fragment.port

import android.util.SparseArray
import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.PortBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.utils.port.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PortFragment : BaseFragment<PortBinding>(PortBinding::inflate) {

    private val adapter = PortAdapter()

    private val listHost = mutableListOf<PortDataItem>()

    private val startPort = 445

    private val endPort = 445

    override fun onViewCreated() {
        binding.portActionGet.actionClickListener { scanIpOpenPort() }
    }

    override fun onLiveDataObserve() {
    }

    private fun scanPortIp() {
        for(model in listHost){
            Host.scanPorts(model.ip, startPort, endPort, 4000,
                object : HostAsyncResponse {
                    override fun processFinish(output: Int) {}

                    override fun processFinish(output: Boolean) {
                    }

                    override fun processFinish(output: SparseArray<String>?) {
                        output ?: return
                        model.isColor = true
                    }

                    override fun <T : Throwable?> processFinish(output: T) {}

                })
        }
        adapter.set(listHost)
        adapter.bind(binding.portRecyclerView)
    }

    private fun scanIpOpenPort() {
        SubnetDevices.fromLocalAddress().findDevices(object :
            SubnetDevices.OnSubnetDeviceFound {
            override fun onDeviceFound(device: Device) {
                lifecycleScope.launch(Dispatchers.Main) {
                    listHost.add(PortDataItem(device.ip, device.hostname))
                    adapter.set(listHost)
                    adapter.bind(binding.portRecyclerView)
                }
            }

            override fun onFinished(devicesFound: ArrayList<Device?>) {
                lifecycleScope.launch(Dispatchers.Main){
                    scanPortIp()
                }
            }
        })
    }

}