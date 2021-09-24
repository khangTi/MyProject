package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.kt.myproject.R
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.WidgetAppInputBinding
import com.kt.myproject.ex.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/08
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppInputView(context: Context, attrs: AttributeSet?) :
    AppBindCustomView<WidgetAppInputBinding>(context, attrs, WidgetAppInputBinding::inflate),
    SimpleMotionTransitionListener,
    OnFocusChangeListener, SimpleTextWatcher {

    override fun onViewInit(context: Context, types: TypedArray) {
        hint = types.hint
        binding.inputEditText.setText(types.text)
        binding.inputEditText.addTextChangedListener(this)
        onIconInitialize(binding.inputImageViewIcon, types)
        onEditTextInitialize(binding.inputEditText, types)
        binding.inputViewLayout.addTransitionListener(this)
    }

    private fun onIconInitialize(it: AppCompatImageView, types: TypedArray) {
        val color = types.getColor(R.styleable.CustomView_android_tint, -1)
        if (color != -1) {
            it.setColorFilter(color)
        }
        src = types.srcRes
    }

    private fun onEditTextInitialize(it: AppCompatEditText, types: TypedArray) {
        it.onFocusChangeListener = this@AppInputView
        it.paintFlags = it.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()

        if (null == types) {
            it.maxLines = 1
            it.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(256))
            return
        }
        // Text filter
        val sFilters = arrayListOf<InputFilter>()

        val textAllCaps = types.getBoolean(R.styleable.CustomView_android_textAllCaps, false)
        if (textAllCaps) sFilters.add(InputFilter.AllCaps())

        val sMaxLength = types.getInt(R.styleable.CustomView_android_maxLength, 256)
        sFilters.add(InputFilter.LengthFilter(sMaxLength))

        val array = arrayOfNulls<InputFilter>(sFilters.size)
        it.filters = sFilters.toArray(array)

        // Input type
        val customInputType = types.getInt(
            R.styleable.CustomView_android_inputType,
            EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        )
        if (customInputType == EditorInfo.TYPE_NULL) {
            disableFocus()
        } else {
            it.inputType = customInputType or
                    EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                    EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
                    EditorInfo.TYPE_TEXT_VARIATION_FILTER or
                    EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
        }

        it.maxLines = types.getInt(R.styleable.CustomView_android_maxLines, 1)

        // Ime option
        val imeOption = types.getInt(R.styleable.CustomView_android_imeOptions, -1)
        if (imeOption != -1) it.imeOptions = imeOption

        it.privateImeOptions = "nm,com.google.android.inputmethod.latin.noMicrophoneKey"

        // Gesture
        it.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        it.setTextIsSelectable(false)
        it.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false
        }
        it.isLongClickable = false
        it.setOnCreateContextMenuListener { menu, _, _ -> menu.clear() }
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (!binding.inputEditText.hasFocus()) {
            binding.inputEditText.requestFocus(FOCUS_DOWN)
        }
        return false
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        super.setOnClickListener(listener)
        if (null == listener) {
            enableFocus()
            binding.inputTextViewHint.setOnClickListener(null)
            binding.inputEditText.setOnClickListener(null)
        } else {
            disableFocus()
            val onClick = OnClickListener {
                listener?.onClick(this@AppInputView)
            }
            binding.inputTextViewHint.addViewClickListener(onClick)
            binding.inputEditText.addViewClickListener(onClick)
        }
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        if (l != null) {
            onFocusChange.add(l)
        } else {
            onFocusChange.clear()
        }
    }

    override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {
        when (currentId) {
            R.id.focused -> binding.inputTextViewHint.setBackgroundResource(R.color.white)
            R.id.unfocused -> {
            }
        }
    }

    override fun onDetachedFromWindow() {
        binding.inputViewLayout.clearAnimation()
        onFocusChange.clear()
        super.onDetachedFromWindow()
    }

    // comment for fix bug https://app.clickup.com/t/6kw1xy
//    override fun isFocused(): Boolean {
//        return inputEditText.isFocused
//    }

    override fun hasFocus(): Boolean {
        return binding.inputEditText.hasFocus()
    }

    override fun clearFocus() {
        super.clearFocus()
        binding.inputEditText.clearFocus()
    }

    /**
     * [OnFocusChangeListener] implements
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        onFocusChange.forEach { it.onFocusChange(this, hasFocus) }
        when {
            hasFocus -> {
                binding.inputViewLayout.transitionToState(R.id.focused)
                drawBackground(0)
                drawBorder(R.color.colorInputFocused)
            }
            !hasFocus && text.isNullOrEmpty() -> {
                clearBorder()
                binding.inputViewLayout.transitionToState(R.id.unfocused)
            }
            !hasFocus && !text.isNullOrEmpty() -> {
                drawBackground(0)
                drawBorder(R.color.colorInputUnfocused)
                binding.inputViewLayout.transitionToState(R.id.focused)
            }
        }
    }

    var listenerAfterChange: () -> Unit = { }

    /**
     * [SimpleTextWatcher] implements
     */
    override fun afterTextChanged(s: Editable?) {
        listenerAfterChange()
        when {
            isSilent -> {
                return
            }
            hasError -> {
                error = null
                drawBackground(0)
                drawBorder(R.color.colorInputFocused)
            }
        }
    }

    var isSilent: Boolean = false

    var hint: String?
        get() = binding.inputTextViewHint.text?.toString()
        set(value) {
            binding.inputTextViewHint.text = value
        }

    val trimText: String?
        get() = text?.replace("\\s+".toRegex(), " ")

    var text: String?
        get() {
            val s =
                binding.inputEditText.text?.toString()?.trimIndent()?.trim()?.replace("\\s+".toRegex(), " ")
            isSilent = true
            binding.inputEditText.setText(s)
            if (hasFocus()) {
                binding.inputEditText.setSelection(s?.length ?: 0)
            }
            isSilent = false
            return s
        }
        set(value) {
            isSilent = true
            binding.inputEditText.setText(value)
            error = null
            onFocusChange(null, isFocused)
            isSilent = false
        }

    var error: String?
        get() = binding.inputTextViewError.text?.toString()
        set(value) {
            binding.inputTextViewError.text = value
            if (error != null) {
                drawBorder(R.color.colorInputError)
            }
        }

    private val onFocusChange = mutableListOf<OnFocusChangeListener>()

    @DrawableRes
    var src: Int = 0
        set(value) {
            val isGone = value <= 0
            binding.inputImageViewIcon.isGone(isGone)
            binding.inputImageViewIcon.setImageResource(value)
        }

    val isTextEmpty: Boolean get() = text.isNullOrEmpty()

    val hasError: Boolean get() = !error.isNullOrEmpty()

    fun addActionNextListener(block: (String?) -> Unit) {
        binding.inputEditText.addActionNextListener(block)
    }

    fun disableFocus() {
        binding.inputEditText.apply {
            isFocusable = false
            isCursorVisible = false
        }
    }

    fun enableFocus() {
        binding.inputEditText.apply {
            isFocusable = true
            isCursorVisible = true
        }
    }

    private fun drawBackground(@DrawableRes res: Int) {
        binding.inputViewBackground.setBackgroundResource(res)
    }

    fun drawBorder(@ColorRes res: Int) {
        if (!text.isNullOrEmpty() || hasFocus()) {
            binding.inputTextViewHint.textColor(res)
        }
        binding.inputEditText.backgroundTintRes(res)
    }

    private fun clearBorder() {
        binding.inputTextViewHint.textColor(R.color.colorInputUnfocused)
        binding.inputEditText.backgroundTintRes(R.color.colorInputDefault)
        drawBackground(R.drawable.drw_app_input_bg)
        binding.inputTextViewHint.background = null
    }

    fun clear() {
        binding.inputEditText.text = null
        error = null
        clearBorder()
    }

}