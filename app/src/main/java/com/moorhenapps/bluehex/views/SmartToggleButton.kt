package com.moorhenapps.bluehex.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.support.constraint.solver.widgets.Rectangle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button

import com.moorhenapps.bluehex.R

import java.util.ArrayList

class SmartToggleButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Button(context, attrs, defStyleAttr) {
    private val otherButtons = ArrayList<SmartToggleButton>()

    var group: String? = null

    var accentColour: Int = 0
        set(value) {
            field = value
            setTextColor(value)
            highlightPaint.color = value
            invalidate()
        }

    private val highlightPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    }

    private var highlightRect = Rect()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SmartToggleButton, defStyleAttr, 0)

        try {
            group = array.getString(R.styleable.SmartToggleButton_group)
        } catch (e: Exception) {
            //?
        } finally {
            array.recycle()
        }

        viewTreeObserver.addOnGlobalLayoutListener {
            otherButtons.clear()
            val parent = parent as ViewGroup
            val count = parent.childCount
            for (i in 0 until count) {
                val child = parent.getChildAt(i)
                if (child is SmartToggleButton && child !== this@SmartToggleButton && child.group == this.group) {
                    otherButtons.add(child)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val inset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()

        highlightRect.set(inset, inset, measuredWidth - inset, measuredHeight - inset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isSelected) {
            canvas.drawRect(highlightRect, highlightPaint)
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            for (button in otherButtons) {
                button.isSelected = false
            }
        }

        paint.isFakeBoldText = isSelected
        invalidate()
    }

}