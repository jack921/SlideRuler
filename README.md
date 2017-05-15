# SlideRuler

高仿京东金融的数值滚动尺

调用方法:

```
<com.jack.slideruler.SlideRuler
        android:id="@+id/slideruler"
        android:layout_width="match_parent"
        android:layout_height="70dp"
        android:layout_centerInParent="true"
        android:layout_marginTop="20dp"
        app:textSize="16dp"
        app:min_value="0"
        app:max_value="200000"
        app:current_value="1500"
        app:min_unitValue="1000"
        app:min_currentValue="1000"/>
```

对应的各个参数的意思:

参数 | 类型 | 含义
---|--- | ---
min_value | integer | 最小值
max_value | integer | 最大值
current_value | integer | 当前值
min_unitValue | integer | 最小单位值
min_currentValue | integer | 最小当前值
textSize | dimension | 数值字体大小
textColor | integer | 数值字体颜色
dividerColor | integer | 底部和刻度的线颜色
indicatrixColor | integer | 指示器的颜色
show_itemSize | integer | 一屏显示Item
margin_cursor_data | dimension | 刻度和数值的间距
longCursor | dimension | 长指针大小
shortCursor | dimension | 短指针大小

效果图:

![show.gif](http://upload-images.jianshu.io/upload_images/925576-d7ef373f4104a883.gif?imageMogr2/auto-orient/strip)


