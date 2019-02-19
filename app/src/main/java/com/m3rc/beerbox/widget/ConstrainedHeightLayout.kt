package com.m3rc.beerbox.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.m3rc.beerbox.R

class ConstrainedHeightLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    var maxHeight: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ConstrainedHeightLayout, defStyleAttr, 0)
        maxHeight = a.getDimensionPixelSize(R.styleable.ConstrainedHeightLayout_android_maxHeight, 0)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val constrainedHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, constrainedHeightMeasureSpec)
    }

}