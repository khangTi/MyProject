package com.kt.myproject.ui.fragment.store.preferences

import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.PreferencesBinding
import com.kt.myproject.ex.post
import com.kt.myproject.ex.toast
import com.kt.myproject.repository.store.PreferencesStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PreferencesFragment : BaseFragment<PreferencesBinding>(PreferencesBinding::inflate) {

    override fun onViewCreated() {
        getValueStore()
        binding.preferencesAction.actionClickListener {
            val input = binding.preferencesInput.text
            when (input.isNullOrEmpty()) {
                true -> toast("text is NullOrEmpty")
                else -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        PreferencesStore.saveData(input.toString())
                    }
                    binding.preferencesInput.setText("")
                    getValueStore()
                }
            }
        }
    }

    override fun onLiveDataObserve() {}

    private fun getValueStore() {
        lifecycleScope.launch(Dispatchers.IO) {
            PreferencesStore.value.collect {
                post { binding.preferencesLabel.text = it }
            }
        }
    }

}