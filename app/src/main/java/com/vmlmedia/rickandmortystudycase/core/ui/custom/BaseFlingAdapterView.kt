package com.vmlmedia.rickandmortystudycase.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.Adapter
import android.widget.AdapterView

abstract class BaseFlingAdapterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AdapterView<Adapter>(context, attrs, defStyle) {

    var heightMeasureSpec: Int = 0
    var widthMeasureSpec: Int = 0

    override fun setSelection(position: Int) {
        throw UnsupportedOperationException("Not supported")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.widthMeasureSpec = widthMeasureSpec
        this.heightMeasureSpec = heightMeasureSpec
    }
}