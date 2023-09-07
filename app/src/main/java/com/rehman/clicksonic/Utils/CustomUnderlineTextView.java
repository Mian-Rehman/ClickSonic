package com.rehman.clicksonic.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CustomUnderlineTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Paint underlinePaint;

    public CustomUnderlineTextView(Context context) {
        super(context);
        init();
    }

    public CustomUnderlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomUnderlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        underlinePaint = new Paint();
        underlinePaint.setColor(getCurrentTextColor()); // Set the underline color to match the text color
        underlinePaint.setStyle(Paint.Style.STROKE);
        underlinePaint.setStrokeWidth(4); // Adjust the thickness of the underline here
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startX = getPaddingLeft();
        int stopX = getWidth() - getPaddingRight();
        int startY = getHeight() - getPaddingBottom() - 4;
        int stopY = startY;

        canvas.drawLine(startX, startY, stopX, stopY, underlinePaint);
    }
}

