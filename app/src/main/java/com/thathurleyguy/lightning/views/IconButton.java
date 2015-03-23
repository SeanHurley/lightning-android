package com.thathurleyguy.lightning.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class IconButton extends ImageView {
    public IconButton(Context context) {
        super(context);
        init(null, 0);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setScaleType(ScaleType.FIT_CENTER);
        setPadding(4, 4, 4, 4);
        setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getDrawable().setColorFilter(0x990000ff, PorterDuff.Mode.SRC_ATOP);
                setBackgroundColor(0x990000ff);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                getDrawable().clearColorFilter();
                setBackgroundColor(0);
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }
}
