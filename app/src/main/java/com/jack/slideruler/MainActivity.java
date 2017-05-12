package com.jack.slideruler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyViewGroup myViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_test);
//        myViewGroup = (MyViewGroup) findViewById(R.id.myviewGroup);

    }

    public void scroll(View view) {
        myViewGroup.beginScroll();
    }

}
