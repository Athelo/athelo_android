package com.athelohealth.mobile.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.extensions.update
import com.athelohealth.mobile.presentation.ui.theme.gray


class TabLayout : LinearLayout {


    private var _selectedTextColor: Int = Color.BLACK
    private var _unselectedTextColor: Int = Color.BLACK

    private var _selectedTypeface: Typeface? = null
    private var _unselectedTypeface: Typeface? = null

    private var _currentSelection: Int = 0

    private var _clickListener: OnClickListener = OnClickListener { view ->
        _currentSelection = view?.id ?: 0
        updateChildren()
        indicateWidth = view.width
        animation.end()
        animation.setIntValues(indicatorRect.left.toInt(), view.x.toInt())
        animation.start()
        if (view != null) {
            onTabSelectedCallback?.invoke(view.id)
        }
    }

    private var indicateWidth = 0
    private val indicatorRect = RectF()
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = gray.toArgb()
        it.style = Paint.Style.FILL_AND_STROKE
        it.strokeWidth = 5.dp.value
    }

    private val animation = ValueAnimator.ofInt(0, 1).apply {
        duration = 300
        addUpdateListener {
            val left = (it.animatedValue as Int).toFloat()
            indicatorRect.set(left, height - 5.dp.value, left + indicateWidth, height - 5.dp.value)
            postInvalidate()
        }
    }

    private val indicatorYPosition: Float
        get() = height - 10.dp.value

    var onTabSelectedCallback: ((id: Int) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        gravity = HORIZONTAL
        orientation = HORIZONTAL
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount > 0) {
            val target = getChildAt(0)
            indicateWidth = target.width
            indicatorRect.update(
                top = indicatorYPosition,
                bottom = indicatorYPosition
            )
            debugPrint("Measure rect: $indicatorRect")
            postInvalidate()
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        updateView(child)
        child?.setOnClickListener(_clickListener)
        super.addView(child, params)
    }

    fun initStyle(
        @FontRes selectedFontRes: Int,
        @ColorRes selectedColorRes: Int,
        @FontRes unselectedFontRes: Int,
        @ColorRes unselectedColorRes: Int
    ) {
        _selectedTypeface = ResourcesCompat.getFont(context, selectedFontRes)
        _unselectedTypeface = ResourcesCompat.getFont(context, unselectedFontRes)

        _selectedTextColor = ContextCompat.getColor(context, selectedColorRes)
        _unselectedTextColor = ContextCompat.getColor(context, unselectedColorRes)
        updateChildren()
        postInvalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawScrollbar(canvas)
    }

    fun selectTab(@IdRes id: Int) {
        post {
            _currentSelection = id
            updateChildren()
        }
    }

    private fun updateChildren() {
        post {
            for (i in 0 until childCount) {
                updateView(get(i))
            }
        }
    }

    private fun updateView(view: View?) {
        post {
            if (view is AppCompatTextView) {
                if (_currentSelection == 0) {
                    return@post
                }
                if (_currentSelection == view.id) {
                    selectTab(view)
                } else {
                    unselectTab(view)
                }
            }
        }
    }


    private fun selectTab(tab: AppCompatTextView) {
        post {
            tab.apply {
                setTextColor(_selectedTextColor)
                _selectedTypeface?.let {
                    typeface = it
                }
            }
        }
    }

    private fun unselectTab(tab: AppCompatTextView) {
        post {
            tab.apply {
                setTextColor(_unselectedTextColor)
                _unselectedTypeface?.let {
                    typeface = it
                }
            }
        }
    }

    private fun drawScrollbar(canvas: Canvas?) {
        debugPrint("Print rect: $indicatorRect")
        canvas?.drawRoundRect(
            indicatorRect.update(top = indicatorYPosition, bottom = indicatorYPosition),
            5.dp.value / 2f,
            5.dp.value / 2f,
            indicatorPaint
        )
    }
}