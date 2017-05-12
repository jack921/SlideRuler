package com.jack.slideruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Jack on 2017/5/3.
 */

public class SlideRuler extends View{

    //最小值
    private int minValue;
    //最大值
    private int maxValue;
    //当前值
    private int currentValue;
    //最小单位值
    private int minUnitValue;
    //最小当前值
    private int minCurrentValue;
    //字体大小
    private int textSize;
    //字体颜色
    private int textColor;
    //线颜色
    private int dividerColor;
    //指示线颜色
    private int indicatrixColor;
    //画线的画笔
    private Paint linePaint;

    private Scroller scroller;

    private GestureDetector mDetector;
    private Display display =null;
    private int marginWidth=0;
    private int marginHeight=0;
    private int wrapcontentWidth;
    private int wrapcontentHeight;

    private int data;

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
        textSize = typedArray.getDimensionPixelOffset(R.styleable.slideruler_textSize,24);
        textColor=typedArray.getColor(R.styleable.slideruler_textColor,Color.DKGRAY);
        dividerColor=typedArray.getColor(R.styleable.slideruler_dividerColor,Color.BLACK);
        indicatrixColor=typedArray.getColor(R.styleable.slideruler_indicatrixColor,Color.GREEN);
        minValue=typedArray.getInteger(R.styleable.slideruler_minValue,0);
        maxValue=typedArray.getInteger(R.styleable.slideruler_maxValue,199000);
        currentValue=typedArray.getInteger(R.styleable.slideruler_currentValue,10000);
        minUnitValue=typedArray.getInteger(R.styleable.slideruler_minUnitValue,1000);
        minCurrentValue=typedArray.getInteger(R.styleable.slideruler_minCurrentValue,1000);

        linePaint=new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setTextSize(textSize);

        data=marginWidth*(currentValue*minUnitValue);
        scroller=new Scroller(context);
        mDetector=new GestureDetector(context,myGestureListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthModel=MeasureSpec.getMode(widthMeasureSpec);
        int heightModel=MeasureSpec.getMode(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if(widthModel==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            width=wrapcontentWidth;
        }
        if(heightModel==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            height=(getPaddingBottom()+getPaddingTop()+(wrapcontentHeight/4));
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        marginWidth=getWidth()/30;
        marginHeight=getWidth()/40;
        drawBaseLine(canvas);
        drawBaseView(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                actionUpView();
                break;
            default:
                mDetector.onTouchEvent(event);
                break;
        }
        return true;
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
        int left= (currentValue-minValue)/minUnitValue;
        int residueData=(currentValue-minValue)%minUnitValue;
        int startCursor=(getWidth()/2)-(marginWidth*left)-(int)(marginWidth*(float)residueData/minUnitValue);
        Log.e("test",(maxValue/minUnitValue)+"");
        for(int i=0;i<(maxValue/minUnitValue)+1;i++){
            float xValue=startCursor+(marginWidth*i);
            if(i%10==0){
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-40,linePaint);
                canvas.drawText((minCurrentValue*i)+"",xValue,getHeight()-40-marginHeight,linePaint);
            }else{
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-25,linePaint);
            }
        }
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            postInvalidate();
        }
    }

    private GestureDetector.SimpleOnGestureListener myGestureListener =new  GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            updateView((int)distanceX);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };

    public void updateView(int distanceX){
        data=data+distanceX;
        float itemNum=(float)data/marginWidth;
        currentValue=(int)(minUnitValue*itemNum);
        if(currentValue<=minValue){
            data=0;
            currentValue=minValue;
        }
        if(currentValue>=maxValue){
            data=marginWidth*(maxValue/minUnitValue);
            currentValue=maxValue;
        }
        invalidate();
    }

    public void actionUpView(){
        BigDecimal mData = new BigDecimal((data/marginWidth)+"").setScale(0,BigDecimal.ROUND_HALF_UP);
        int itemNum=mData.intValue();
        currentValue=(minUnitValue*itemNum);
        if(currentValue<=minValue){
            data=0;
            currentValue=minValue;
        }
        if(currentValue>=maxValue){
            data=marginWidth*(maxValue/minUnitValue);
            currentValue=maxValue;
        }
        invalidate();
    }


}
