package com.jack.slideruler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyViewGroup myViewGroup;
    private SlideRuler slideruler;
    private TextView data_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_test);
//        myViewGroup = (MyViewGroup) findViewById(R.id.myviewGroup);
        slideruler=(SlideRuler) findViewById(R.id.slideruler);
        data_tv=(TextView)findViewById(R.id.data);
        slideruler.setSlideRulerDataInterface(new SlideRulerDataInterface() {
            @Override
            public void getText(String data) {
                data_tv.setText(data);
            }
        });
    }

    public void scroll(View view) {
        myViewGroup.beginScroll();
    }

}
