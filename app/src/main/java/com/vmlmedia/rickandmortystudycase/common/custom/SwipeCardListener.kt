package com.vmlmedia.rickandmortystudycase.common.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.cos

/**
 * SwipeCardListener is an implementation of View.OnTouchListener that manages the swipe gestures for card views.
 * It allows cards to react to user gestures by moving and rotating based on the swipe, and triggers actions when the gestures end.
 *
 * @property frame The view that will be swiped.
 * @property dataObject An object associated with the card, for any kind of data handling.
 * @property baseRotationDegrees The base degrees of rotation during a swipe, affecting the tilt of the card.
 * @property flingListener Listener for handling swipe events and providing callbacks like card exit or selection.
 */

class SwipeCardListener(
    private val frame: View,
    private val dataObject: Any,
    private var baseRotationDegrees: Float,
    private val flingListener: FlingListener
) : View.OnTouchListener {

    private var activePointerId: Int = INVALID_POINTER_ID
    private var aPosX: Float = frame.x
    private var aPosY: Float = frame.y
    private var aDownTouchX: Float = 0f
    private var aDownTouchY: Float = 0f
    private val mActivePointerId = INVALID_POINTER_ID
    private val objectH: Int = frame.height
    private val objectW: Int = frame.width
    private val objectX: Float = frame.x
    private val objectY: Float = frame.y
    private val halfWidth: Float = objectW / 2f
    private val parentWidth: Int = (frame.parent as ViewGroup).width
    private var touchPosition: Int = 0
    private val maxCos: Float = cos(Math.toRadians(45.0)).toFloat()

    private var isAnimationRunning = false

    init {
        frame.x = aPosX
        frame.y = aPosY
    }

    /**
     * Handles touch events on the card view, managing the movement and rotation of the view based on user interaction.
     * @param view The view being touched.
     * @param event The MotionEvent object containing full details of the touch event.
     * @return A boolean value indicating if the touch event is consumed.
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activePointerId = event.getPointerId(0)
                val (x, y) = try {
                    Pair(event.getX(activePointerId), event.getY(activePointerId))
                } catch (e: IllegalArgumentException) {
                    return false
                }
                aDownTouchX = x
                aDownTouchY = y
                touchPosition = if (y < objectH / 2) TOUCH_ABOVE else TOUCH_BELOW
                view.parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = event.findPointerIndex(activePointerId)
                val xMove = event.getX(pointerIndex)
                val yMove = event.getY(pointerIndex)
                val dx = xMove - aDownTouchX
                val dy = yMove - aDownTouchY

                aPosX += dx
                aPosY += dy
                frame.x = aPosX
                frame.y = aPosY
                frame.rotation = calculateRotation(aPosX - objectX)
                flingListener.onScroll(getScrollProgressPercent())
            }
            MotionEvent.ACTION_UP -> {
                activePointerId = INVALID_POINTER_ID
                resetCardViewOnStack()
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_CANCEL -> {
                activePointerId = INVALID_POINTER_ID
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return true
    }

    /**
     * Calculates the rotation angle of the card based on its position.
     * @param distanceX The horizontal distance moved by the card from its original position.
     * @return The calculated rotation angle for the card.
     */
    private fun calculateRotation(distanceX: Float): Float {
        var rotation = baseRotationDegrees * 2f * distanceX / parentWidth
        if (touchPosition == TOUCH_BELOW) rotation = -rotation
        return rotation
    }

    /**
     * Calculates the scroll progress percentage, indicating how far the card has moved from the center.
     * @return The scroll progress percentage from -1.0 (fully left) to 1.0 (fully right).
     */
    private fun getScrollProgressPercent(): Float {
        return when {
            movedBeyondLeftBorder() -> -1f
            movedBeyondRightBorder() -> 1f
            else -> {
                val zeroToOneValue = (aPosX + halfWidth - leftBorder()) / (rightBorder() - leftBorder())
                zeroToOneValue * 2f - 1f
            }
        }
    }

    /**
     * Resets the card view back to its original position on the stack after the gesture is finished.
     */
    private fun resetCardViewOnStack() {
        if (movedBeyondLeftBorder()) {
            onSelected(true, getExitPoint(-objectW), 100)
            flingListener.onScroll(-1.0f)
        } else if (movedBeyondRightBorder()) {
            onSelected(false, getExitPoint(parentWidth), 100)
            flingListener.onScroll(1.0f)
        } else {
            aPosX = objectX
            aPosY = objectY
            frame.animate()
                .setDuration(200)
                .x(objectX)
                .y(objectY)
                .rotation(0f)
            flingListener.onScroll(0.0f)
        }
    }

    /**
     * Calculates the exit point for the card when swiped off the screen.
     * @param exitXPoint The x-coordinate to which the card moves horizontally during the exit.
     * @return The y-coordinate at the calculated exit point based on linear regression.
     */
    private fun getExitPoint(exitXPoint: Int): Float {
        val x = FloatArray(2)
        x[0] = objectX
        x[1] = aPosX
        val y = FloatArray(2)
        y[0] = objectY
        y[1] = aPosY
        val regression = LinearRegression(x, y)

        //Your typical y = ax+b linear regression
        return regression.slope() * exitXPoint + regression.intercept()
    }

    /**
     * Performs the final selection animation for the card, moving it off the screen.
     * @param isLeft Boolean indicating if the card is exiting to the left side.
     * @param exitY The y-coordinate for the card's exit.
     * @param duration The duration of the exit animation in milliseconds.
     */
    private fun onSelected(isLeft: Boolean, exitY: Float, duration: Long) {
        isAnimationRunning = true

        val exitX = if (isLeft) -objectW - getRotationWidthOffset() else parentWidth + getRotationWidthOffset()
        frame.animate()
            .setDuration(duration)
            .x(exitX)
            .y(exitY)
            .rotation(getExitRotation(isLeft))
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isAnimationRunning = false
                    flingListener.onCardExited()
                    if (isLeft) flingListener.leftExit(dataObject) else flingListener.rightExit(dataObject)
                }
            })
    }

    private fun movedBeyondLeftBorder() = aPosX + halfWidth < leftBorder()
    private fun movedBeyondRightBorder() = aPosX + halfWidth > rightBorder()
    private fun leftBorder() = parentWidth / 4f
    private fun rightBorder() = 3 * parentWidth / 4f

    private fun getExitRotation(isLeft: Boolean): Float {
        var rotation = baseRotationDegrees * 2f * (parentWidth - objectX) / parentWidth
        if (touchPosition == TOUCH_BELOW) rotation = -rotation
        if (isLeft) rotation = -rotation
        return rotation
    }

    fun isTouching(): Boolean {
        return this.mActivePointerId !== MotionEvent.INVALID_POINTER_ID
    }

    private fun getRotationWidthOffset() = objectW / maxCos - objectW

    /**
     * Starts a default left exit animation.
     */
    fun selectLeft() {
        if (!isAnimationRunning) onSelected(true, objectY, 200)
    }

    /**
     * Starts a default right exit animation.
     */
    fun selectRight() {
        if (!isAnimationRunning) onSelected(false, objectY, 200)
    }

    /**
     * Provides the interface for handling events related to card flinging.
     */
    interface FlingListener {
        fun onCardExited()
        fun leftExit(dataObject: Any)
        fun rightExit(dataObject: Any)
        fun onClick(dataObject: Any)
        fun onScroll(scrollProgressPercent: Float)
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
        private const val TOUCH_ABOVE = 0
        private const val TOUCH_BELOW = 1
    }
}