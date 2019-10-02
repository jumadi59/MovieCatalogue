package com.jumadi.moviecatalogue.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jumadi.moviecatalogue.R;

public class FlexLayout extends FrameLayout {

    private static final int NONE = -1;

    private int partMainId;
    private int partSlaveId;

    private TextView viewPartMain;
    private View viewPartSlave;
    private int viewPartSlaveHeight;
    private float viewPartMainLastLineWidth;
    private LayoutParams viewPartSlaveLayoutParams;

    public FlexLayout(Context context) {
        super(context);
    }

    public FlexLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlexLayout);
        partMainId = a.getResourceId(R.styleable.FlexLayout_partMain, NONE);
        partSlaveId = a.getResourceId(R.styleable.FlexLayout_partSlave, NONE);
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();

        viewPartMain = findViewById(partMainId);
        viewPartSlave = findViewById(partSlaveId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize =MeasureSpec.getSize(widthMeasureSpec);
        int heightSize =MeasureSpec.getSize(heightMeasureSpec);

        if(viewPartMain ==null|| viewPartSlave ==null|| widthSize <=0){
            return;
        }

        int availableWidth = widthSize - getPaddingLeft()- getPaddingRight();
        @SuppressWarnings("unused")
        int availableHeight = heightSize - getPaddingTop()- getPaddingBottom();
        LayoutParams viewPartMainLayoutParams = (LayoutParams) viewPartMain.getLayoutParams();

        int viewPartMainWidth = viewPartMain.getMeasuredWidth() + viewPartMainLayoutParams.leftMargin + viewPartMainLayoutParams.rightMargin;
        int viewPartMainHeight = viewPartMain.getMeasuredHeight() + viewPartMainLayoutParams.topMargin + viewPartMainLayoutParams.bottomMargin;

        viewPartSlaveLayoutParams = (LayoutParams) viewPartSlave.getLayoutParams();
        int viewPartSlaveWidth = viewPartSlave.getMeasuredWidth() + viewPartSlaveLayoutParams.leftMargin + viewPartSlaveLayoutParams.rightMargin;
        viewPartSlaveHeight = viewPartSlave.getMeasuredHeight()+ viewPartSlaveLayoutParams.topMargin + viewPartSlaveLayoutParams.bottomMargin;
        int viewPartMainLineCount = viewPartMain.getLineCount();

        lineCountViewPartMain();

        widthSize = getPaddingLeft()+ getPaddingRight();
        heightSize = getPaddingTop()+ getPaddingBottom();

        if(viewPartMainLineCount >1&&!(viewPartMainLastLineWidth + viewPartSlaveWidth >= viewPartMain.getMeasuredWidth())){
            widthSize += viewPartMainWidth;
            heightSize += viewPartMainHeight;
        }
        else if(viewPartMainLineCount >1&&(viewPartMainLastLineWidth + viewPartSlaveWidth >= availableWidth)){
            widthSize += viewPartMainWidth;
            heightSize += viewPartMainHeight + viewPartSlaveHeight;
        }else if(viewPartMainLineCount ==1&&(viewPartMainWidth + viewPartSlaveWidth >= availableWidth)){
            widthSize += viewPartMain.getMeasuredWidth();
            heightSize += viewPartMainHeight + viewPartSlaveHeight;
        }else{
            widthSize += viewPartMainWidth + viewPartSlaveWidth;
            heightSize += viewPartMainHeight;
        }

        this.setMeasuredDimension(widthSize, heightSize);

        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY));


    }
    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if(viewPartMain ==null|| viewPartSlave ==null)
            return;

        viewPartMain.layout(
                getPaddingLeft(),
                getPaddingTop(),
                viewPartMain.getWidth()+ getPaddingLeft(),
                viewPartMain.getHeight()+ getPaddingTop());

        lineCountViewPartMain();
        viewPartSlave.layout(
                left + getPaddingLeft() + (int) viewPartMainLastLineWidth + viewPartSlaveLayoutParams.leftMargin,
                bottom - top - getPaddingBottom() - viewPartSlaveHeight - viewPartSlaveLayoutParams.bottomMargin,
                left + getPaddingLeft() + (int) viewPartMainLastLineWidth + viewPartSlave.getWidth() + viewPartSlaveLayoutParams.leftMargin,
                bottom - top - getPaddingBottom() - viewPartSlaveLayoutParams.bottomMargin);
    }

    private void lineCountViewPartMain() {
        int viewPartMainLineCount = viewPartMain.getLineCount();
        viewPartMainLastLineWidth = viewPartMainLineCount >0? viewPartMain.getLayout().getLineWidth(viewPartMainLineCount -1):0;
    }
}
