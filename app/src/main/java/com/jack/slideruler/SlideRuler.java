package com.jack.slideruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Jack on 2017/5/3.
 */

public class SlideRuler extends View{

    //最小值
    private float minValue=0;
    //最大值
    private float maxValue=199000;
    //当前值
    private float currentValue=10000;
    //最小单位值
    private float minUnitValue=1000;
    //字体大小
    private int textSize;
    //字体颜色
    private int textColor;
    //线颜色
    private int dividerColor;
    //最小当前值
    private float minCurrentValue;
    //指示线颜色
    private int indicatrixColor;

    private int WrapcontentWidth;
    private int WrapcontentHight;

    private Display display =null;

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public void setMinUnitValue(float minUnitValue) {
        this.minUnitValue = minUnitValue;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public void setIndicatrixColor(int indicatrixColor) {
        this.indicatrixColor = indicatrixColor;
    }

    public void setMinCurrentValue(float minCurrentValue) {
        this.minCurrentValue = minCurrentValue;
    }

    public SlideRuler(Context context) {
        this(context,null);
    }

    public SlideRuler(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public SlideRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        display=((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        WrapcontentWidth=display.getWidth();

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.slideruler,defStyleAttr,0);
        int numCount=typedArray.getIndexCount();
        for(int i=0;i<numCount;i++){
            int attr=typedArray.getIndex(i);
            switch(attr){
                case R.styleable.slideruler_minValue:
                    minValue=typedArray.getFloat(i,0);
                    break;
                case R.styleable.slideruler_maxValue:
                    maxValue=typedArray.getFloat(i,199000);
                    break;
                case R.styleable.slideruler_currentValue:
                    currentValue=typedArray.getFloat(i,10000);
                    break;
                case R.styleable.slideruler_minUnitValue:
                    minCurrentValue=typedArray.getFloat(i,1000);
                    break;
                case R.styleable.slideruler_minCurrentValue:
                    minCurrentValue=typedArray.getFloat(i,1000);
                    break;
                case R.styleable.slideruler_textSize:
                    textSize=typedArray.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.slideruler_textColor:
                    textColor=typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.slideruler_dividerColor:
                    dividerColor=typedArray.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.slideruler_indicatrixColor:
                    indicatrixColor=typedArray.getColor(attr,Color.GREEN);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthModel=MeasureSpec.getMode(widthMeasureSpec);
        int heightModel=MeasureSpec.getMode(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width=0;
        int height=0;
        if(widthModel==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            width=WrapcontentWidth;
        }
        if(heightModel==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            height=(int)(getPaddingBottom()+getPaddingTop()+(WrapcontentHight/4));
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
