package com.jack.slideruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

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

    public SlideRuler(Context context) {
        this(context,null);
    }

    public SlideRuler(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public SlideRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    
}
