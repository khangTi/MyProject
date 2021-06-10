package com.kt.myproject.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kt.myproject.base.BaseDialog
import com.kt.myproject.databinding.CartDialogBinding

class CartDialog : BaseDialog<CartDialogBinding>() {

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): CartDialogBinding {
        return CartDialogBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {
    }

}