package com.example.BluetoothRollersControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

/**
 * Created by Елена on 23.03.2016.
 */
public class TrippleStateButton extends Button {

    private int state = 0;
    public int backgroundColor = 0xFF4B4B4B;
    private boolean blinkingMode = true;
    private Animation animation;

    // Constructors
    public TrippleStateButton(Context context)
    {
        super(context);
        setAnimation();
    }

    public TrippleStateButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setAnimation();
    }

    public TrippleStateButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setAnimation();
    }

    @Override
    public boolean performClick()
    {
        // Move to the next state
        nextState();

        return super.performClick();
    }

    private void nextState(){
        state++;
        if(state > 2) state = 0;
        if(state == 2 && !blinkingMode) state = 0;
        switch(state){
            case 0: this.clearAnimation();
                    this.setBackgroundColor(0xFF4B4B4B);
                    break;
            case 1: this.setBackgroundColor(backgroundColor);
                    break;
            case 2: this.startAnimation(animation);
                    break;
            default: this.setBackgroundColor(0xFF4B4B4B);
                     break;
        }
    }

    private void setAnimation(){
        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(250); // duration 500 - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
    }

    public void disableAnimation(){
        blinkingMode = false;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }
}
