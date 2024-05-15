package com.vmlmedia.rickandmortystudycase.core.ui.custom

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.FrameLayout;
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import com.vmlmedia.rickandmortystudycase.R

class SwipeFlingAdapterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : BaseFlingAdapterView(context, attrs, defStyle) {

    private var maxVisible = 4
    private var minAdapterStack = 6
    private var rotationDegrees = 15f
    private var adapter: Adapter? = null
    private var lastObjectInStack = 0
    private var flingListener: onFlingListener? = null
    private var dataSetObserver: AdapterDataSetObserver? = null
    private var inLayout = false
    private var activeCard: View? = null
    private var onItemClickListener: OnItemClickListener? = null
    private var flingCardListener: FlingCardListener? = null
    private var lastTouchPoint: PointF? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView, defStyle, 0).apply {
            maxVisible = getInt(R.styleable.SwipeFlingAdapterView_max_visible, maxVisible)
            minAdapterStack = getInt(R.styleable.SwipeFlingAdapterView_min_adapter_stack, minAdapterStack)
            rotationDegrees = getFloat(R.styleable.SwipeFlingAdapterView_rotation_degrees, rotationDegrees)
            recycle()
        }
    }

    fun init(context: Context, adapter: Adapter) {
        this.adapter = adapter
        if (context is onFlingListener) {
            flingListener = context
        } else {
            throw RuntimeException("Activity does not implement SwipeFlingAdapterView.onFlingListener")
        }
        if (context is OnItemClickListener) {
            onItemClickListener = context
        }
        setAdapter(adapter)
    }

    override fun getSelectedView(): View? = activeCard

    override fun requestLayout() {
        if (!inLayout) super.requestLayout()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        adapter?.let { adapter ->
            inLayout = true
            val adapterCount = adapter.count
            if (adapterCount == 0) {
                removeAllViewsInLayout()
            } else {
                val topCard = getChildAt(lastObjectInStack)
                if (activeCard != null && topCard != null && topCard == activeCard) {
                    if (flingCardListener?.isTouching() == true) {
                        lastTouchPoint?.let {
                            removeViewsInLayout(0, lastObjectInStack)
                            layoutChildren(1, adapterCount)
                        }
                    }
                } else {
                    removeAllViewsInLayout()
                    layoutChildren(0, adapterCount)
                    setTopView()
                }
            }
            inLayout = false
            if (adapterCount <= minAdapterStack) flingListener?.onAdapterAboutToEmpty(adapterCount)
        }
    }

    private fun layoutChildren(startingIndex: Int, adapterCount: Int) {
        var index = startingIndex
        while (index < Math.min(adapterCount, maxVisible)) {
            val newUnderChild = adapter?.getView(index, null, this)
            newUnderChild?.let { child ->
                if (child.visibility != View.GONE) {
                    makeAndAddView(child)
                    lastObjectInStack = index
                }
            }
            index++
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun makeAndAddView(child: View) {
        val lp = child.layoutParams as FrameLayout.LayoutParams
        addViewInLayout(child, 0, lp, true)
        val needToMeasure = child.isLayoutRequested
        if (needToMeasure) {
            val childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin,
                lp.width)
            val childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin,
                lp.height)
            child.measure(childWidthSpec, childHeightSpec)
        } else {
            cleanupLayoutState(child)
        }
        val w = child.measuredWidth
        val h = child.measuredHeight
        val gravity = if (lp.gravity == -1) Gravity.TOP or Gravity.START else lp.gravity
        val layoutDirection = layoutDirection
        val absoluteGravity = GravityCompat.getAbsoluteGravity(gravity, layoutDirection)
        val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
        val childLeft = when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.CENTER_HORIZONTAL -> (width + paddingLeft - paddingRight - w) / 2 + lp.leftMargin - lp.rightMargin
            Gravity.END -> width + paddingRight - w - lp.rightMargin
            Gravity.START -> paddingLeft + lp.leftMargin
            else -> paddingLeft + lp.leftMargin
        }
        val childTop = when (verticalGravity) {
            Gravity.CENTER_VERTICAL -> (height + paddingTop - paddingBottom - h) / 2 + lp.topMargin - lp.bottomMargin
            Gravity.BOTTOM -> height - paddingBottom - h - lp.bottomMargin
            Gravity.TOP -> paddingTop + lp.topMargin
            else -> paddingTop + lp.topMargin
        }
        child.layout(childLeft, childTop, childLeft + w, childTop + h)
    }

    private fun setTopView() {
        if (childCount > 0) {
            activeCard = getChildAt(lastObjectInStack)
            activeCard?.let { card ->
                flingCardListener = FlingCardListener(card, adapter!!.getItem(0), rotationDegrees, object : FlingCardListener.FlingListener {
                    override fun onCardExited() {
                        activeCard = null
                        flingListener?.removeFirstObjectInAdapter()
                    }

                    override fun leftExit(dataObject: Any) {
                        flingListener?.onLeftCardExit(dataObject)
                    }

                    override fun rightExit(dataObject: Any) {
                        flingListener?.onRightCardExit(dataObject)
                    }

                    override fun onClick(dataObject: Any) {
                        onItemClickListener?.onItemClicked(0, dataObject)
                    }

                    override fun onScroll(scrollProgressPercent: Float) {
                        flingListener?.onScroll(scrollProgressPercent)
                    }
                })
                card.setOnTouchListener(flingCardListener)
            }
        }
    }

    fun getTopCardListener() = flingCardListener ?: throw NullPointerException()

    fun setMaxVisible(maxVisible: Int) {
        this.maxVisible = maxVisible
    }

    fun setMinStackInAdapter(minAdapterStack: Int) {
        this.minAdapterStack = minAdapterStack
    }

    override fun getAdapter(): Adapter? = adapter

    override fun setAdapter(adapter: Adapter?) {
        if (this.adapter != null && dataSetObserver != null) {
            this.adapter!!.unregisterDataSetObserver(dataSetObserver!!)
            dataSetObserver = null
        }
        this.adapter = adapter
        if (adapter != null && dataSetObserver == null) {
            dataSetObserver = AdapterDataSetObserver()
            adapter.registerDataSetObserver(dataSetObserver!!)
        }
    }

    fun setFlingListener(onFlingListener: onFlingListener) {
        this.flingListener = onFlingListener
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return FrameLayout.LayoutParams(context, attrs)
    }

    private inner class AdapterDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            requestLayout()
        }

        override fun onInvalidated() {
            requestLayout()
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(itemPosition: Int, dataObject: Any)
    }

    interface onFlingListener {
        fun removeFirstObjectInAdapter()
        fun onLeftCardExit(dataObject: Any)
        fun onRightCardExit(dataObject: Any)
        fun onAdapterAboutToEmpty(itemsInAdapter: Int)
        fun onScroll(scrollProgressPercent: Float)
    }
}