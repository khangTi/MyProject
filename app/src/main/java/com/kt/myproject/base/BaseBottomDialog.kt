package com.kt.myproject.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kt.myproject.R
import com.kt.myproject.ex.Inflate
import com.kt.myproject.utils.Logger

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/05
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseBottomDialog<VB : ViewBinding>(val inflate: Inflate<VB>) : BottomSheetDialogFragment(),
    BaseView {

    val log by lazy {
        Logger(this::class.simpleName.toString())
    }

    lateinit var binding: VB

    /**
     * [BottomSheetDialogFragment] implements
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupHeight(it as BottomSheetDialog) }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        val view = binding.root
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController()

    /**
     * [BaseFragment] required implements
     */
    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
    }

    private fun setupHeight(bottomSheetDialog: BottomSheetDialog) {
        val frameLayout = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        val behavior = frameLayout?.let { BottomSheetBehavior.from(it) }
        frameLayout?.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_bottomsheet_dialog, null)
        behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

}