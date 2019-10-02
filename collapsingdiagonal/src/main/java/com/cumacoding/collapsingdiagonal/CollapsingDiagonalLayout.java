package com.cumacoding.collapsingdiagonal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class CollapsingDiagonalLayout extends CollapsingToolbarLayout {

    public static String TAG = CollapsingDiagonalLayout.class.getSimpleName();
    public static final int NONE = -1;
    public static final int BOTTOM_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    private final int mToolbarId;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path  = new Path();
    private Xfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    private int mOverlap;
    private int mPosition;
    private int mTranslateDiagonal = 0;
    private OffsetUpdateListener mOnOffsetChangedListener;
    private int mCurrentOffset;
    private int mCurrentOffsetOld = 0;
    private View mToolbar;

    WindowInsetsCompat mLastInsets;

    public CollapsingDiagonalLayout(Context context) {
        this(context, null);
    }

    public CollapsingDiagonalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingDiagonalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mToolbarId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "toolbarId", NONE);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CollapsingDiagonalLayout, defStyleAttr, R.style.Widget_Design_CollapsingToolbar);

        mOverlap = a.getDimensionPixelSize(R.styleable.CollapsingDiagonalLayout_di_overlap, NONE);
        mPosition = a.getInteger(R.styleable.CollapsingDiagonalLayout_di_position, NONE);

        a.recycle();

        setExpandedTitleMarginBottom(getExpandedTitleMarginBottom());
        setTag(TAG);

        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                mLastInsets = insets;
                return insets.consumeStableInsets();
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mToolbar == null) {
            mToolbar = findViewById(mToolbarId);
            if (mToolbar != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mToolbar.setZ(99);
                }
            }
        }

        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {

            ((AppBarLayout) parent).setBackgroundColor(Color.TRANSPARENT);
            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        final ViewParent parent = getParent();
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener);
        }

        super.onDetachedFromWindow();
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

    @Override
    public void draw(@NonNull Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);

        paint.setXfermode(pdMode);
        path.reset();

        if (mPosition == BOTTOM_LEFT) {
            path.moveTo(0, getHeight());
            path.lineTo(getWidth(), getHeight() - (mOverlap - mTranslateDiagonal));
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
        } else if (mPosition == BOTTOM_RIGHT){
            path.moveTo(0, getHeight() - (mOverlap - mTranslateDiagonal));
            path.lineTo(getWidth(), getHeight());
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
        }

        path.close();
        canvas.drawPath(path, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);

    }

    @Override
    public void setExpandedTitleMargin(int start, int top, int end, int bottom) {
        bottom = mOverlap + bottom;
        super.setExpandedTitleMargin(start, top, end, bottom);
    }

    @Override
    public void setExpandedTitleMarginBottom(int margin) {
        margin = mOverlap + margin;
        super.setExpandedTitleMarginBottom(margin);
    }

    @Override
    public int getScrimVisibleHeightTrigger() {
        if (mToolbar != null ) {
            LayoutParams lp = (LayoutParams) mToolbar.getLayoutParams();

            final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
            return insetTop + mOverlap + mToolbar.getHeight() + lp.topMargin + lp.bottomMargin;
        }

        return super.getScrimVisibleHeightTrigger();
    }

    public void updateDiagonalOverlap(int delta) {

        if (mCurrentOffset + getHeight() < getScrimVisibleHeightTrigger())
            mTranslateDiagonal = (int) Math.max(0.0f, Math.min(getWidth(),  mTranslateDiagonal - delta));
        else
            mTranslateDiagonal = 0;

        invalidate();
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            mCurrentOffset = verticalOffset;

            updateDiagonalOverlap(mCurrentOffset - mCurrentOffsetOld);

            mCurrentOffsetOld = mCurrentOffset;


        }
    }
}
