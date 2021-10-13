package com.kt.myproject.ui.fragment.button

import com.kt.myproject.R
import com.kt.myproject.base.BaseDialog
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.FragmentButtonBinding

class ButtonFragment : BaseDialog<FragmentButtonBinding>(FragmentButtonBinding::inflate) {

    override fun style(): Int {
        return R.style.App_Dialog_FullScreen
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {}

}