package com.kt.myproject.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.kt.myproject.ex.Inflate
import com.kt.myproject.ex.packageName
import com.kt.myproject.ui.activity.MainActivity
import com.kt.myproject.utils.Logger

abstract class BaseFragment<VB : ViewBinding>(val inflate: Inflate<VB>) : Fragment(), BaseView {

    val log by lazy {
        Logger(this::class.simpleName.toString())
    }

    protected open fun permission(): Array<String> {
        return arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            android.Manifest.permission.RECORD_AUDIO
        )
    }

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [BaseFragment] required implements
     */
    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()


    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController()

    /**
     * handle permission
     */
    open fun onPermissionGranted(granted: String) {}

    fun checkPermissionAndRequest() {
        var isCheck = true
        for (model in permission()) {
            if (checkSelfPermission(
                    requireContext(),
                    model
                ) != PackageManager.PERMISSION_GRANTED
            ) isCheck = false
        }
        if (isCheck) {
            onPermissionGranted("")
        } else {
            requestPermissions(permission(), 1000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 1000) return
        for (i in permissions.indices) {
            val permission = permissions[i]
            when (grantResults[i]) {
                PackageManager.PERMISSION_GRANTED -> {
                    onPermissionGranted(permission)
                }
                PackageManager.PERMISSION_DENIED -> {
                    val permissionAgain = shouldShowRequestPermissionRationale(permission)
                    if (!permissionAgain) {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null)
                        )
                        startActivity(intent)
                    }
                }
            }
        }
    }

    /**
     * [BaseFragment] properties
     */

    open fun activity(): MainActivity {
        if (activity !is MainActivity) throw ClassCastException("BaseFragment must be owned in BaseActivity")
        return activity as MainActivity
    }

    fun startClear(cls: Class<*>) {
        activity().run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }
    }

    fun <T> MutableLiveData<T>.obsever(block: (T) -> Unit) {
        this.observe(viewLifecycleOwner, {
            it?.let { it1 -> block(it1) }
        })
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer(block))
    }

    fun <T> NonNullLiveData<T?>.observe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            block(it)
        })
    }

}