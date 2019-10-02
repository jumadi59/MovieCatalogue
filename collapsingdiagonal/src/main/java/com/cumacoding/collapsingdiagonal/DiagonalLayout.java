package com.cumacoding.collapsingdiagonal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class DiagonalLayout extends FrameLayout {

    public static final int NONE = -1;
    public static final int BOTTOM_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path  = new Path();
    private Xfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    private int mOverlap = 50;
    private int mPosition = 1;
    private int mTranslationDiagonal = 0;

    public DiagonalLayout(Context context) {
        super(context);
    }

    public DiagonalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiagonalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("CustomViewStyleable")
    void init(Context context,  AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingDiagonalLayout);

        mOverlap = a.getDimensionPixelSize(R.styleable.CollapsingDiagonalLayout_di_overlap, NONE);
        mPosition = a.getInteger(R.styleable.CollapsingDiagonalLayout_di_position, NONE);

        a.recycle();
    }

    public void setOverlap(int mOverlap) {
        this.mOverlap = mOverlap;
    }

    public int getOverlap() {
        return mOverlap;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setTranslateDiagonal(int translateDiagonal) {
        mTranslationDiagonal = translateDiagonal;
    }

    public int getTranslationDiagonal() {
        return mTranslationDiagonal;
    }

    @Override
    public void draw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);

        paint.setXfermode(pdMode);
        path.reset();

        if (mPosition == BOTTOM_LEFT) {
            path.moveTo(0, getHeight());
            path.lineTo(getWidth(), getHeight() - (mOverlap - mTranslationDiagonal));
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
        } else if (mPosition == BOTTOM_RIGHT){
            path.moveTo(0, getHeight() - (mOverlap - mTranslationDiagonal));
            path.lineTo(getWidth(), getHeight());
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
        }

        path.close();
        canvas.drawPath(path, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);

    }
}
