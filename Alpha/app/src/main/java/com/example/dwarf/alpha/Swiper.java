package com.example.dwarf.alpha;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.app.Activity;

public class Swiper implements View.OnTouchListener {

    private int padding=0;
    private int initX=0;
    private int currX=0;
    private View tmp_v=null;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean flag=false;
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initX=(int)event.getX();
                    currX=(int)event.getX();
                    int Y=(int)event.getRawY();
                    int []coords=new int[2];
                    ListView l=(ListView)v;
                    int num_of_childs=l.getChildCount(),i;
                    for(i=0;i<num_of_childs;i++){
                        tmp_v=l.getChildAt(i);
                        tmp_v.getLocationOnScreen(coords);
                        if(Y>=coords[1]&&Y<=(coords[1]+tmp_v.getHeight())) {
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    currX=(int)event.getX();
                    padding=currX-initX;
                    tmp_v.setPadding(padding,0,0,0);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    initX=0;
                    currX=0;
                    padding=0;
                    tmp_v.setPadding(padding,0,0,0);
                    tmp_v=null;
                    flag=true;
            }
            return true;
        }
    }
