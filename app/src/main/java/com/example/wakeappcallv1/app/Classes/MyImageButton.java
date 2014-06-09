package com.example.wakeappcallv1.app.Classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by lucamarconcini on 22/05/14.
 */
public class MyImageButton extends ImageButton{


        public boolean state;




    public MyImageButton(Context context) {

        super(context);
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    boolean isActive(){

        return state;

    }


    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }




    public void changeButtonState(MyImageButton bi, int drawable) {


        bi.setImageResource(drawable);


    }


}




