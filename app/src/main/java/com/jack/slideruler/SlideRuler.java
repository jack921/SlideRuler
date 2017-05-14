package com.jack.slideruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
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
    //控价的宽度
    private int slideRulerWidth=0;
    //滑动的宽度
    private int rollingWidth;
    //屏幕的宽
    private int wrapcontentWidth;
    //屏幕的高
    private int wrapcontentHeight;
    //数据回调接口
    private SlideRulerDataInterface slideRulerDataInterface;
    //正在滑动状态
    private int isScrollState=1;
    //快速一滑
    private int fastScrollState=2;
    //结束滑动
    private int finishScrollState=3;

    private Scroller scroller;
    private GestureDetector mDetector;
    private Display display =null;
    private int marginWidth=0;
    private int marginHeight=0;

    public SlideRuler(Context context) {
        this(context,null);
    }

    public SlideRuler(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public void setSlideRulerDataInterface(SlideRulerDataInterface slideRulerDataInterface) {
        this.slideRulerDataInterface = slideRulerDataInterface;
    }

    public SlideRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        display=((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        wrapcontentWidth=display.getWidth();
        wrapcontentHeight=display.getHeight();

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        marginWidth=getWidth()/30;
        marginHeight=getWidth()/40;
        rollingWidth=(int)(marginWidth*cursorNum());
        slideRulerWidth=(maxValue/minUnitValue)*marginWidth;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //画最基础的两条线
        drawBaseView(canvas);
        //画初始的界面
        drawBaseLine(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                updateView(0,finishScrollState);
            default:
                mDetector.onTouchEvent(event);
                break;
        }
        return true;
    }

    //画最基础的两条线
    public void drawBaseLine(Canvas canvas){
        //画中间的线
        linePaint.setColor(indicatrixColor);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),linePaint);
        //画底部的直线
        linePaint.setColor(dividerColor);
        canvas.drawLine(0,getHeight(),slideRulerWidth,getHeight(),linePaint);
    }

    //画初始的界面
    public void drawBaseView(Canvas canvas){
        int left= (currentValue-minValue)/minUnitValue;
        int residuerollingWidth=(currentValue-minValue)%minUnitValue;
        int startCursor=(getWidth()/2)-(marginWidth*left)-(int)(marginWidth*(float)residuerollingWidth/minUnitValue);
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

    //动态更新滑动View
    public void updateView(int srcollWidth,int action){
        if(action==isScrollState){
            rollingWidth=srcollWidth;
            float itemNum=(float)srcollWidth/marginWidth;
            currentValue=(int)(minUnitValue*itemNum);
        }else if(action==fastScrollState){
            rollingWidth=srcollWidth;
            int itemNum=(int)Math.rint((float)rollingWidth/marginWidth);
            currentValue=(minUnitValue*itemNum);
        }else if(action==finishScrollState){
            int itemNum=(int)Math.rint((float)rollingWidth/marginWidth);
            currentValue=minUnitValue*itemNum;
        }
        if(slideRulerDataInterface!=null){
            slideRulerDataInterface.getText(currentValue+"");
        }
        if(currentValue<=minValue){
            rollingWidth=0;
            currentValue=minValue;
        }
        if(currentValue>=maxValue){
            rollingWidth=marginWidth*allCursorNum();
            currentValue=maxValue;
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            updateView(scroller.getCurrX(),fastScrollState);
        }
    }

    //获取的指针数
    public float cursorNum(){
        return (float)currentValue/minUnitValue;
    }

    //获取所有的指针个数
    public int allCursorNum(){
        return maxValue/minUnitValue;
    }

    private GestureDetector.SimpleOnGestureListener myGestureListener =new  GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            updateView(rollingWidth+(int)distanceX,isScrollState);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("onFling","onFling");
            scroller.fling(rollingWidth,0,(int)(-velocityX/1.5),0,0,(maxValue/minUnitValue)*marginWidth,0,0);
            return true;
        }
    };

}
