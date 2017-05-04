package com.jack.slideruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private int minValue=0;
    //最大值
    private int maxValue=199000;
    //当前值
    private int currentValue=10000;
    //最小单位值
    private int minUnitValue=1000;
    //最小当前值
    private int minCurrentValue=1000;
    //字体大小
    private int textSize;
    //字体颜色
    private int textColor;
    //线颜色
    private int dividerColor=Color.parseColor("#000000");
    //指示线颜色
    private int indicatrixColor=Color.parseColor("#000000");
    //画线的画笔
    private Paint linePaint;

    private int marginWidth=0;
    private int wrapcontentWidth;
    private int wrapcontentHight;

    private Display display =null;

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public void setMinUnitValue(int minUnitValue) {
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

    public void setMinCurrentValue(int minCurrentValue) {
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
        wrapcontentWidth=display.getWidth();

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.slideruler,defStyleAttr,0);
        int numCount=typedArray.getIndexCount();
        for(int i=0;i<numCount;i++){
            int attr=typedArray.getIndex(i);
            switch(attr){
                case R.styleable.slideruler_minValue:
                    minValue=typedArray.getInteger(i,0);
                    break;
                case R.styleable.slideruler_maxValue:
                    maxValue=typedArray.getInteger(i,199000);
                    break;
                case R.styleable.slideruler_currentValue:
                    currentValue=typedArray.getInteger(i,10000);
                    break;
                case R.styleable.slideruler_minUnitValue:
                    minCurrentValue=typedArray.getInteger(i,1000);
                    break;
                case R.styleable.slideruler_minCurrentValue:
                    minCurrentValue=typedArray.getInteger(i,1000);
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

        linePaint=new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);

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
            width=wrapcontentWidth;
        }
        if(heightModel==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            height=(int)(getPaddingBottom()+getPaddingTop()+(wrapcontentHight/4));
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        marginWidth=getWidth()/30;
        drawBaseLine(canvas);
        drawBaseView(canvas);
    }

    //画最基础的两条线
    public void drawBaseLine(Canvas canvas){
        linePaint.setColor(indicatrixColor);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),linePaint);
        linePaint.setColor(dividerColor);
        canvas.drawLine(0,getHeight(),getWidth(),getHeight(),linePaint);
    }

    //画初始的界面
    public void drawBaseView(Canvas canvas){
        int left= currentValue/minUnitValue;
        for(int i=1;i<left+1;i++){
            int xValue=getWidth()/2-(marginWidth*i);
            if(i/10==0){
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-25,linePaint);
            }else{
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-40,linePaint);
            }
        }


    }



}
