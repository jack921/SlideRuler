package com.jack.slidereler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;


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
    //一屏显示Item
    private int showItemSize;
    //刻度和数值的间距
    private int marginCursorData;
    //长刻度的大小
    private int longCursor;
    //短刻度的大小
    private int shortCursor;
    //计算每个刻度的间距
    private int marginWidth=0;
    //数据回调接口
    private SlideRulerDataInterface slideRulerDataInterface;
    //正在滑动状态
    private int isScrollingState=1;
    //快速一滑
    private int fastScrollState=2;
    //结束滑动
    private int finishScrollState=3;

    private GestureDetector mDetector;
    private Display display =null;
    private Scroller scroller;

    public SlideRuler(Context context) {
        this(context,null);
    }

    public SlideRuler(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public void setSlideRulerDataInterface(SlideRulerDataInterface slideRulerDataInterface) {
        this.slideRulerDataInterface = slideRulerDataInterface;
    }

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

    public void setMinCurrentValue(int minCurrentValue) {
        this.minCurrentValue = minCurrentValue;
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

    public void setLongCursor(int longCursor) {
        this.longCursor = longCursor;
    }

    public void setShortCursor(int shortCursor) {
        this.shortCursor = shortCursor;
    }

    public void setMarginCursorData(int marginCursorData) {
        this.marginCursorData = marginCursorData;
    }

    public void setShowItemSize(int showItemSize) {
        this.showItemSize = showItemSize;
    }

    public SlideRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        display=((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //屏幕宽高
        wrapcontentWidth=display.getWidth();
        wrapcontentHeight=display.getHeight();
        //初始化自定义的参数
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.slideruler,defStyleAttr,0);
        textSize = typedArray.getDimensionPixelSize(R.styleable.slideruler_textSize,(int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics()));
        textColor=typedArray.getColor(R.styleable.slideruler_textColor,Color.DKGRAY);
        dividerColor=typedArray.getColor(R.styleable.slideruler_dividerColor,Color.BLACK);
        indicatrixColor=typedArray.getColor(R.styleable.slideruler_indicatrixColor,Color.BLACK);
        minValue=typedArray.getInteger(R.styleable.slideruler_min_value,0);
        maxValue=typedArray.getInteger(R.styleable.slideruler_max_value,199000);
        currentValue=typedArray.getInteger(R.styleable.slideruler_current_value,10000);
        minUnitValue=typedArray.getInteger(R.styleable.slideruler_min_unitValue,1000);
        minCurrentValue=typedArray.getInteger(R.styleable.slideruler_min_currentValue,1000);
        showItemSize=typedArray.getInteger(R.styleable.slideruler_show_itemSize,30);
        marginCursorData=typedArray.getDimensionPixelSize(R.styleable.slideruler_margin_cursor_data,(int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,10,getResources().getDisplayMetrics()));
        longCursor=typedArray.getDimensionPixelSize(R.styleable.slideruler_longCursor,(int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,25,getResources().getDisplayMetrics()));
        shortCursor=typedArray.getDimensionPixelSize(R.styleable.slideruler_shortCursor,(int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics()));

        scroller=new Scroller(context);
        mDetector=new GestureDetector(context,myGestureListener);

        //初始化Paint
        linePaint=new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setTextSize(textSize);
        //检查当前值是不是正确值
        checkCurrentValue();
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
        //计算每个刻度的间距
        marginWidth=getWidth()/showItemSize;
        //开始时的距离
        rollingWidth=(int)(marginWidth*cursorNum());
        //整个控件的宽度
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
        //整数刻度的个数
        int integerWidth= (int)Math.rint((currentValue-minValue)/minUnitValue);
        //剩余不整一个刻度的数值
        int residueWidth=(currentValue-minValue)%minUnitValue;
        //开始画图的X轴位置
        int startCursor=(getWidth()/2)-(marginWidth*integerWidth)-(int)(marginWidth*(float)residueWidth/minUnitValue);
        for(int i=0;i<allCursorNum()+1;i++){
            float xValue=startCursor+(marginWidth*i);
            if(i%10==0){
                //画长刻度
                linePaint.setColor(textColor);
                canvas.drawText((minCurrentValue*i)+"",xValue,getHeight()-longCursor-marginCursorData,linePaint);
                linePaint.setColor(dividerColor);
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-longCursor,linePaint);
            }else{
                //画短刻度
                canvas.drawLine(xValue,getHeight(),xValue,getHeight()-shortCursor,linePaint);
            }
        }
    }

    //动态更新滑动View
    public void updateView(int srcollWidth,int action){
        if(action==isScrollingState){
            //正在滑动状态(onScroll())
            rollingWidth=srcollWidth;
            float itemNum=(float)srcollWidth/marginWidth;
            currentValue=(int)(minUnitValue*itemNum);
        }else if(action==fastScrollState){
            //快速一滑(onFling())
            rollingWidth=srcollWidth;
            int itemNum=(int)Math.rint((float)rollingWidth/marginWidth);
            currentValue=(minUnitValue*itemNum);
        }else if(action==finishScrollState){
            //结束滑动(ACTION_UP)
            int itemNum=(int)Math.rint((float)rollingWidth/marginWidth);
            currentValue=minUnitValue*itemNum;
        }
        //判断是否在最小选择值
        if(currentValue<=minCurrentValue){
            rollingWidth=(minCurrentValue/minUnitValue)*marginWidth;
            currentValue=minCurrentValue;
        }
        //判断是否在最大值
        if(currentValue>=maxValue){
            rollingWidth=marginWidth*allCursorNum();
            currentValue=maxValue;
        }
        //回调数值
        if(slideRulerDataInterface!=null){
            slideRulerDataInterface.getText(currentValue+"");
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            //快滑刷新UI
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
            //滑动刷新UI
            updateView(rollingWidth+(int)distanceX,isScrollingState);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //快速滑动的动画
            scroller.fling(rollingWidth,0,(int)(-velocityX/1.5),0,0,(maxValue/minUnitValue)*marginWidth,0,0);
            return true;
        }
    };

    //检查当前值是不是正确值
    public void checkCurrentValue(){
        if(currentValue<minValue){
            if(minValue<minCurrentValue){
                currentValue=minCurrentValue;
            }else{
                currentValue=minValue;
            }
        }
        if(currentValue>maxValue){
            currentValue=maxValue;
        }
        if(currentValue%minUnitValue!=0){
            int num=currentValue/minCurrentValue;
            currentValue=minUnitValue*(num+1);
        }
    }


}
