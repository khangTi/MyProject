package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import com.kt.myproject.R
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.WidgetRangeBinding
import com.kt.myproject.ex.SimpleMotionTransitionListener
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class RangeView(context: Context, attrs: AttributeSet) :
    AppBindCustomView<WidgetRangeBinding>(context, attrs, WidgetRangeBinding::inflate),
    SimpleMotionTransitionListener {

    private var rangeType: Int = 0 // 0 -> money

    private var step: Long = 1

    private var mTransformText: (Long) -> String = { it.toString() }

    var transformText: (Long) -> String
        get() = mTransformText
        set(value) {
            binding.rangeTextViewMin.text = "$min $unit"
            binding.rangeTextViewMax.text = "$max $unit"
            mTransformText = value
        }

    override fun onViewInit(context: Context, types: TypedArray) {
    }

    override fun initAttrs(context: Context, types: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(types, R.styleable.RangeView, 0, 0)

        rangeType = types.getInt(R.styleable.RangeView_range_type, 0)
        unit = types.getString(R.styleable.RangeView_range_unit) ?: ""
        step = types.getInt(R.styleable.RangeView_range_step, 1).toLong()
        min = types.getInt(R.styleable.RangeView_range_min, 0).toLong()
        max = types.getInt(R.styleable.RangeView_range_max, 0).toLong()

        binding.rangeTextViewMax.text = configRange(max.toInt())
        binding.rangeTextViewMin.text = configRange(min.toInt())
        binding.rangeMotionLayout.setTransitionListener(this)

        value = min
    }

    override fun onTransitionChange(
        layout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        Log.d("progressMotionLayout", progress.toString())
        bindViewByProgress(progress)
    }

    override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {
        super.onTransitionCompleted(layout, currentId)
        bindViewByProgress(layout?.progress ?: 1f)
    }

    private fun configRange(number: Int): String {
        return when (rangeType == 0) {
            true -> formatMoneyRange(number)
            else -> "$number $min"
        }
    }

    /**
     * [RangeView] properties
     */
    private var unit: String = ""

    var min: Long = 0

    var max: Long = 100

    var onValueChanged: (Long) -> Unit = {}

    private var mValue: Long = 0

    var value: Long
        get() = mValue
        set(value) {
            if (value !in min..max) throw IllegalArgumentException("value must be in range $min to $max")
            post {
                mValue = value
                val transValue = mTransformText(value)
                Log.d("rangeBubbleValue", "$mValue")
                bindValueRange(mValue)
                onValueChanged(mValue)
            }
        }

    private fun bindValueRange(value: Long) {
        when (rangeType == 0) {
            true -> binding.rangeTextViewValue.text = formatMoneyRange(value.toInt())
            else -> binding.rangeTextViewValue.text = "$value $unit"
        }
    }

    private fun bindViewByProgress(progress: Float) {
        if (progress !in 0f..1f) throw IllegalArgumentException("progress must be in range 0 to 100")
        value = getValueByProgress(progress)
    }

    private fun getValueByProgress(progress: Float): Long {
        return (progress * (max - min)).roundToLong() + min
    }

    private fun getProgressByValue(value: Long): Float {
        if (value !in min..max) throw IllegalArgumentException("range must be in range $min to $max")
        val progress = (value - min) * 1f / (max - min)
        if (progress < 0) return 0f
        if (progress > 1f) return 1f
        return progress
    }

    fun setDataRange(data: Long) {
        binding.rangeMotionLayout.post {
            var nextValue = data + step
            if (nextValue > max) nextValue = max
            value = nextValue - 1
            binding.rangeMotionLayout.progress = getProgressByValue(nextValue)
        }
    }

    private fun formatMoneyRange(money: Int): String {
        val newValue = (money.toDouble() / step).roundToInt() * step
        return when (newValue >= 1000) {
            true -> "${newValue / 1000} Tỷ"
            else -> "$newValue Triệu"
        }
    }

}