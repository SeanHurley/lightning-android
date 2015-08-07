package com.thathurleyguy.lightning.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout

import android.view.ViewGroup.LayoutParams.MATCH_PARENT

public class IconButton : ImageView {
    public constructor(context: Context) : super(context) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        setScaleType(ImageView.ScaleType.FIT_CENTER)
        setPadding(4, 4, 4, 4)
        setLayoutParams(LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1f))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> if (getDrawable() != null) {
                getDrawable().setColorFilter(-1728052993, PorterDuff.Mode.SRC_ATOP)
                setBackgroundColor(-1728052993)
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (getDrawable() != null) {
                getDrawable().clearColorFilter()
                setBackgroundColor(0)
                invalidate()
            }
        }

        return super.onTouchEvent(event)
    }
}
