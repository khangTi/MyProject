package com.kt.myproject.ui.activity

import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseActivity
import com.kt.myproject.base.activityVM
import com.kt.myproject.databinding.ActivityMainBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.repository.store.PreferencesStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainVM by lazy { activityVM(MainVM::class) }

    override fun viewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {
        mainVM.tokenMessageEvent.observe {
            saveToken(it)
        }
    }

    private fun saveToken(it: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            PreferencesStore.saveToken(it)
        }
    }

}