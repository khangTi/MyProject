package com.kt.myproject.ui.fragment.store

import android.view.inputmethod.EditorInfo
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
            updateDataStore()
        }
        binding.preferencesInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId == EditorInfo.IME_ACTION_DONE) {
                true -> updateDataStore()
                else -> ""
            }
            true
        }
    }

    override fun onLiveDataObserve() {}

    private fun updateDataStore() {
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

    private fun getValueStore() {
        lifecycleScope.launch(Dispatchers.IO) {
            PreferencesStore.value.collect {
                launch(Dispatchers.Main) { binding.preferencesLabel.text = it }
            }
        }
    }

}