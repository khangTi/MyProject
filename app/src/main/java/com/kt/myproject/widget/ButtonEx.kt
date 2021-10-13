package com.kt.myproject.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.kt.myproject.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/10/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ButtonEx : LinearLayout {

    private val FONT_AWESOME = R.font.nexa_bold

    private var borderColor = Color.TRANSPARENT
    private var borderWidth = 0
    private var radius = 0f

    private var mIsEnable = true


    private var backgroundColor = Color.parseColor("#D6D7D7")
    private var focusColor = Color.parseColor("#B0B0B0")
    private var disableColor = Color.parseColor("#D6D7D7")


    private var textSize = 37
    private var textColor = Color.parseColor("#1C1C1C")
    private var disabledTextColor = Color.parseColor("#A0A0A0")
    private var textAllCaps = false
    private var textStyle = 0

    private var fullPadding: Int = 10
    private var padding: Int = 20
    private var leftPadding = 20
    private var rightPadding = 20
    private var topPadding = 20
    private var bottomPadding = 20


    private var text: String? = ""
    private val mGravity = Gravity.CENTER


    private var drawableResource = 0
    private var drawable: Drawable? = null
    private var fontIcon: String? = ""
    private var iconPosition = POSITION_LEFT
    private var iconColor = 0
    private var iconSize = 37
    private val fixedIconPadding = 30
    private var iconPadding: Int = 0
    private var lGravity = 0

    private var awesomeIconTypeFace: Typeface? = null
    private var imageView: ImageView? = null
    private var textView: TextView? = null

    constructor(context: Context?) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        processAttributes(context, attrs)
        initializeView()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        processAttributes(context, attrs)
        initializeView()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        processAttributes(context, attrs)
        initializeView()
    }

    private fun initializeView() {
        if (!isInEditMode) {
            awesomeIconTypeFace = getAwesomeTypeface(context)
        }
        if (iconPosition == POSITION_TOP || iconPosition == POSITION_BOTTOM) {
            this.orientation = VERTICAL
        } else {
            this.orientation = HORIZONTAL
        }
        if (this.layoutParams == null) {
            val containerParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            this.layoutParams = containerParams
        }
        super.setGravity(mGravity)
        super.setEnabled(mIsEnable)
        this.isClickable = mIsEnable
        this.isFocusable = true
        setupTextView()
        setupImageView()
        setupBackground()
        super.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

        removeAllViews()
        if (iconPosition == POSITION_RIGHT || iconPosition == POSITION_BOTTOM) {
            if (textView != null) this.addView(textView)
            if (imageView != null) this.addView(imageView)
        } else {
            if (imageView != null) this.addView(imageView)
            if (textView != null) this.addView(textView)
        }
        updateGravity()
    }


    private fun processAttributes(context: Context, attrs: AttributeSet) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) initDefaultAttributes(attrs) else initDefaultAttributes17(
            attrs
        )
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.NoboButton, 0, 0)
        initAttributes(attrsArray)
        attrsArray.recycle()
    }

    @SuppressLint("ResourceType")
    private fun initDefaultAttributes(attrs: AttributeSet) {
        val defAttr = intArrayOf(
            android.R.attr.gravity,
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingTop,
            android.R.attr.paddingRight,
            android.R.attr.paddingBottom
        )
        val defAttrsArray = context?.obtainStyledAttributes(attrs, defAttr) ?: return
        padding = defAttrsArray.getDimensionPixelSize(1, padding)

        if (padding != 0) {
            bottomPadding = padding
            rightPadding = paddingBottom
            topPadding = paddingRight
            leftPadding = paddingTop
        }
        leftPadding = defAttrsArray.getDimensionPixelSize(2, paddingLeft)
        topPadding = defAttrsArray.getDimensionPixelSize(3, paddingTop)
        rightPadding = defAttrsArray.getDimensionPixelSize(4, paddingRight)
        bottomPadding = defAttrsArray.getDimensionPixelSize(5, paddingBottom)
        leftPadding = defAttrsArray.getDimensionPixelSize(6, paddingLeft)
        rightPadding = defAttrsArray.getDimensionPixelSize(7, paddingRight)
        defAttrsArray.recycle()
    }

    @SuppressLint("ResourceType")
    private fun initDefaultAttributes17(attrs: AttributeSet) {
        val defAttr = intArrayOf(
            android.R.attr.gravity,
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingTop,
            android.R.attr.paddingRight,
            android.R.attr.paddingBottom,
            android.R.attr.paddingStart,
            android.R.attr.paddingEnd
        )
        val defAttrsArray = context?.obtainStyledAttributes(attrs, defAttr) ?: return

        padding = defAttrsArray.getDimensionPixelSize(1, padding)
        bottomPadding = padding
        rightPadding = paddingBottom
        topPadding = paddingRight
        leftPadding = paddingTop
        leftPadding = defAttrsArray.getDimensionPixelSize(2, paddingLeft)
        topPadding = defAttrsArray.getDimensionPixelSize(3, paddingTop)
        rightPadding = defAttrsArray.getDimensionPixelSize(4, paddingRight)
        bottomPadding = defAttrsArray.getDimensionPixelSize(5, paddingBottom)
        leftPadding = defAttrsArray.getDimensionPixelSize(6, paddingLeft)
        rightPadding = defAttrsArray.getDimensionPixelSize(7, paddingRight)
        defAttrsArray.recycle()
    }


    /**
     * Initialize Attributes arrays
     *
     * @param attrs : Attributes array
     */
    private fun initAttributes(attrs: TypedArray) {
        radius = attrs.getDimension(R.styleable.NoboButton_nb_radius, radius)
        borderColor = attrs.getColor(R.styleable.NoboButton_nb_borderColor, borderColor)
        borderWidth =
            attrs.getDimension(R.styleable.NoboButton_nb_borderWidth, borderWidth.toFloat()).toInt()
        backgroundColor = attrs.getColor(R.styleable.NoboButton_nb_backgroundColor, backgroundColor)
        disableColor = attrs.getColor(R.styleable.NoboButton_nb_disableColor, disableColor)
        focusColor = attrs.getColor(R.styleable.NoboButton_nb_focusColor, focusColor)
        text = attrs.getString(R.styleable.NoboButton_nb_text)
        textColor = attrs.getColor(R.styleable.NoboButton_nb_textColor, textColor)
        disabledTextColor =
            attrs.getColor(R.styleable.NoboButton_nb_disabledTextColor, disabledTextColor)
        textSize = attrs.getDimensionPixelSize(R.styleable.NoboButton_nb_textSize, textSize)
        textStyle = attrs.getInt(R.styleable.NoboButton_nb_textStyle, textStyle)

        textAllCaps =
            attrs.getBoolean(R.styleable.NoboButton_nb_textAllCaps, textAllCaps)
        fontIcon = attrs.getString(R.styleable.NoboButton_nb_fontIcon)
        iconSize = attrs.getDimensionPixelSize(R.styleable.NoboButton_nb_iconSize, iconSize)
        iconColor = attrs.getColor(R.styleable.NoboButton_nb_iconColor, iconColor)
        iconPosition = attrs.getInt(R.styleable.NoboButton_nb_iconPosition, iconPosition)
        drawableResource =
            attrs.getResourceId(R.styleable.NoboButton_nb_drawableResource, drawableResource)
        iconPadding =
            attrs.getDimensionPixelSize(R.styleable.NoboButton_nb_iconPadding, iconPadding)
        lGravity = attrs.getInt(R.styleable.NoboButton_nb_gravity, lGravity)
        mIsEnable = attrs.getBoolean(R.styleable.NoboButton_nb_enabled, mIsEnable)
    }


    private fun setupBackground() {
        val defaultDrawable = GradientDrawable()
        defaultDrawable.cornerRadius = radius
        defaultDrawable.setColor(backgroundColor)

        val focusDrawable = GradientDrawable()
        focusDrawable.cornerRadius = radius
        focusDrawable.setColor(focusColor)

        val disabledDrawable = GradientDrawable()
        disabledDrawable.cornerRadius = radius
        disabledDrawable.setColor(disableColor)
        if (borderColor != 0 && borderWidth > 0) {
            defaultDrawable.setStroke(borderWidth, borderColor)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = getRippleDrawable(defaultDrawable, focusDrawable, disabledDrawable)
        } else {
            val states = StateListDrawable()
            val drawable2 = GradientDrawable()
            drawable2.cornerRadius = radius
            drawable2.setColor(focusColor)
            if (focusColor != 0) {
                states.addState(intArrayOf(android.R.attr.state_pressed), drawable2)
                states.addState(intArrayOf(android.R.attr.state_focused), drawable2)
                states.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
            }
            states.addState(intArrayOf(), defaultDrawable)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(states)
            } else {
                this.background = states
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getRippleDrawable(
        defaultDrawable: Drawable,
        focusDrawable: Drawable,
        disabledDrawable: Drawable
    ): Drawable? {
        return if (!isEnabled()) {
            disabledDrawable
        } else {
            RippleDrawable(ColorStateList.valueOf(focusColor), defaultDrawable, focusDrawable)
        }
    }


    private fun setupTextView() {
        textView = TextView(context)
        val textViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView?.layoutParams = textViewParams
        textView?.text = text
        textView?.setTextColor(if (mIsEnable) textColor else disabledTextColor)
        textView?.textSize = pxToSp(context, textSize.toFloat()).toFloat()
        textView?.isAllCaps = textAllCaps

        if (textStyle == 2) {
            textView?.setTypeface(textView?.typeface, Typeface.ITALIC)
        } else if (textStyle == 1) {
            textView?.setTypeface(textView?.typeface, Typeface.BOLD)
        } else {
            textView?.setTypeface(textView?.typeface, Typeface.NORMAL)
        }

        textView?.gravity = mGravity
    }

    private fun setupImageView() {
        imageView = ImageView(context)
        if (iconColor == 0) {
            iconColor = textColor
        }

        if (fontIcon != null && fontIcon?.isNotEmpty() == true) {
            val color = if (isEnabled()) iconColor else disabledTextColor
            imageView?.setImageBitmap(textToBitmap(fontIcon!!, iconSize.toFloat(), color))
        }

        // add drawable icon to imageview
        if (drawableResource != 0) {
            imageView?.setImageResource(drawableResource)
        }
        if (drawable != null) {
            imageView?.setImageDrawable(drawable)
        }
        updateIconPadding()
    }


    private fun pxToSp(context: Context?, px: Float): Int {
        return Math.round(px / context!!.resources.displayMetrics.scaledDensity)
    }

    private fun updateGravity() {
        if (lGravity == GRAVITY_CENTER) {
            // center
            super.setGravity(Gravity.CENTER)
        } else if (lGravity == GRAVITY_LEFT) {
            // left
            super.setGravity(Gravity.START or Gravity.CENTER_VERTICAL)
        } else if (lGravity == GRAVITY_RIGHT) {
            // right
            super.setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        } else if (lGravity == GRAVITY_TOP) {
            // top
            super.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        } else if (lGravity == GRAVITY_BOTTOM) {
            // bottom
            super.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        }
    }

    private fun updateIconPadding() {
        val imageViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        if (fontIcon == null || fontIcon!!.isEmpty() || fontIcon!!.length <= 0) {
            imageViewParams.setMargins(0, 0, 0, 0)
        } else if (iconPosition == POSITION_LEFT) {
            imageViewParams.setMargins(0, 0, getDrawablePadding(), 0)
        } else if (iconPosition == POSITION_TOP) {
            imageViewParams.setMargins(0, 0, 0, getDrawablePadding())
        } else if (iconPosition == POSITION_RIGHT) {
            imageViewParams.setMargins(getDrawablePadding(), 0, 0, 0)
        } else if (iconPosition == POSITION_BOTTOM) {
            imageViewParams.setMargins(0, getDrawablePadding(), 0, 0)
        }
        imageView!!.layoutParams = imageViewParams
    }

    private fun getDrawablePadding(): Int {
        return if (iconPadding != 0) {
            iconPadding
        } else fixedIconPadding
    }


    private fun getFontBitmap(): Bitmap? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = iconColor
        if (awesomeIconTypeFace != null && !isInEditMode) {
            paint.typeface = awesomeIconTypeFace
            paint.textSize = iconSize.toFloat()
        } else {
            fontIcon = "o"
            paint.textSize = (iconSize - 15).toFloat()
        }
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(fontIcon) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(fontIcon!!, 0f, baseline, paint)
        return image
    }

    private fun textToBitmap(text: String, iconSize: Float, textColor: Int): Bitmap? {
        var text: String? = text
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = textColor
        if (awesomeIconTypeFace != null && !isInEditMode) {
            paint.typeface = awesomeIconTypeFace
            paint.textSize = iconSize
        } else {
            text = "O"
            paint.textSize = (iconSize / 2.5).toFloat()
        }
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text!!, 0f, baseline, paint)
        return image
    }

    fun getAwesomeTypeface(context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.nexa_bold)
    }


    fun setAllCaps(allCaps: Boolean) {
        textAllCaps = allCaps
        textView!!.isAllCaps = allCaps
    }

    fun getAllCaps(): Boolean {
        return textAllCaps
    }

    fun setText(text: String?) {
        this.text = text
        if (textView != null) textView!!.text = text else setupTextView()
    }

    fun getText(): String? {
        return text
    }

    fun setTextStyle(style: Int) {
        textStyle = style
        if (textStyle == TEXT_STYLE_ITALIC) {
            textView!!.setTypeface(textView!!.typeface, Typeface.ITALIC)
        } else if (textStyle == TEXT_STYLE_BOLD) {
            textView!!.setTypeface(textView!!.typeface, Typeface.BOLD)
        } else {
            textView!!.setTypeface(textView!!.typeface, Typeface.NORMAL)
        }
    }

    fun getTextStyle(): Int {
        return textStyle
    }

    fun setTextSize(size: Int) {
        textSize = size
        textView!!.textSize = size.toFloat()
    }

    fun getTextSize(): Float {
        return textSize.toFloat()
    }

    fun setTextColor(color: Int) {
        textColor = color
        textView!!.setTextColor(if (mIsEnable) textColor else disabledTextColor)
    }

    fun getTextColor(): Int {
        return textColor
    }


    fun setBorderWidth(size: Int) {
        borderWidth = size
        setupBackground()
    }

    fun getBorderWidth(): Int {
        return borderWidth
    }


    fun setBorderColor(color: Int) {
        borderColor = color
        setupBackground()
    }

    fun getBorderColor(): Int {
        return borderColor
    }


    fun setRadius(size: Float) {
        radius = size
        setupBackground()
    }

    fun getRadius(): Float {
        return radius
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        backgroundColor = color
        setupBackground()
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun setFocusColor(@ColorInt color: Int) {
        focusColor = color
        setupBackground()
    }

    fun getFocusColor(): Int {
        return focusColor
    }

    fun setDisableColor(@ColorInt color: Int) {
        disableColor = color
        setupBackground()
    }

    fun getDisableColor(): Int {
        return disableColor
    }

    override fun setEnabled(enabled: Boolean) {
        //super.setEnabled(enabled);
        mIsEnable = enabled
        initializeView()
    }

    fun setDisabledTextColor(color: Int) {
        disabledTextColor = color
        initializeView()
    }

    fun getDisabledTextColor(): Int {
        return disabledTextColor
    }

    fun setDisabledColor(color: Int) {
        disableColor = color
        setupBackground()
    }

    fun getDisabledColor(): Int {
        return disableColor
    }

    fun setFontIcon(fontIcon: String?) {
        this.fontIcon = fontIcon
        imageView!!.setImageBitmap(getFontBitmap())
    }

    fun getIconSize(): Int {
        return iconSize
    }

    fun setIconSize(iconSize: Int) {
        this.iconSize = iconSize
        imageView!!.setImageBitmap(getFontBitmap())
    }

    fun setIconColor(color: Int) {
        iconColor = color
        imageView!!.setImageBitmap(getFontBitmap())
    }

    fun setIconPosition(position: Int) {
        iconPosition = position
        initializeView()
    }

    fun setDrawableResource(@DrawableRes resource: Int) {
        drawableResource = resource
        //initializeView();
        imageView?.setImageResource(resource)
    }

    fun setDrawable(drawable: Drawable?) {
        this.drawable = drawable
        initializeView()
    }

    fun setIconPadding(padding: Int) {
        this.iconPadding = padding
        initializeView()
    }

    fun getIconPadding(): Int {
        return this.iconPadding
    }

    fun setTextGravity(gravity: Int) {
        lGravity = gravity
        initializeView()
    }

    companion object {
        const val POSITION_LEFT = 1
        const val POSITION_RIGHT = 2
        const val POSITION_TOP = 3
        const val POSITION_BOTTOM = 4

        const val GRAVITY_CENTER = 0
        const val GRAVITY_LEFT = 1
        const val GRAVITY_RIGHT = 2
        const val GRAVITY_TOP = 3
        const val GRAVITY_BOTTOM = 4

        const val TEXT_STYLE_NORMAL = 0
        const val TEXT_STYLE_BOLD = 1
        const val TEXT_STYLE_ITALIC = 2
    }

}