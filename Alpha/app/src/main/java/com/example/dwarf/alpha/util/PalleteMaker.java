package com.example.dwarf.alpha.util;

import android.graphics.Color;

public class PalleteMaker {
    color p;
    color q;
    int no_of_pallets;
    public PalleteMaker(int start,int end,int no_of_pallets) {
        p = new color(start);
        q = new color(end);
        this.no_of_pallets=no_of_pallets;
    }
    public int[] getPalleteArray(){
            int[] listOfColors=new int[no_of_pallets];
            int i;
            int r,g,b;
            int n=no_of_pallets;
            for(i=0;i<n;i++) {
                r=(int)(p.r+(q.r-p.r)/(float)n*i);
                g=(int)(p.g+(q.g-p.g)/(float)n*i);
                b=(int)(p.b+(q.b-p.b)/(float)n*i);
                listOfColors[i]=Color.argb(255,r,g,b);
            }
        return listOfColors;
    }
}

class color{
    int r,g,b;
    int code;
    public color(int code){
        r=Color.red(code);
        g=Color.green(code);
        b=Color.blue(code);
        this.code=code;
    }

    public color(int r,int g,int b){
        code=Color.argb(255,r,g,b);
    }

}
