package com.example.BluetoothRollersControl.HSBColorPicker;

import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stargazer on 25.03.2016.
 */
public class CustomTimeScrollPickerView extends TimeScrollPickerView {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH/mm/ss");
    protected int secondIndex;

    private static int current_hue = 0;
    private static int current_saturation = 100;
    private static int current_brightness = 100;

    private static LoopSlotView hueSlot, saturationSlot, brightnessSlot;

    public CustomTimeScrollPickerView(Context context) {
        super(context);
    }

    public CustomTimeScrollPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        String[] hue_colors = new String[361];
        for(int i = 0; i < 361; i++){
            hue_colors[i] = Integer.toString(i);
        }

        hueSlot = addSlot(hue_colors, 5, ScrollType.Loop, "hue");
        String[] sb_colors = new String[101];
        sb_colors[0] = "100";
        for(int i = 0; i < 100; i++){
            sb_colors[i+1] = Integer.toString(i);
        }

        saturationSlot = addSlot(sb_colors, 5, ScrollType.Loop, "saturation");

        brightnessSlot = addSlot(sb_colors, 5, ScrollType.Loop, "brightness");

        hourIndex = 0;
        minuteIndex = 2;
        secondIndex = 4;
    }

    public void setCurrentTime(boolean isScroll) {
        String d = sdf.format(new Date());
        String[] ds = d.split("/");

        setHour(Integer.parseInt(ds[0]), isScroll);
        setMinute(Integer.parseInt(ds[1]), isScroll);
        setSecond(Integer.parseInt(ds[2]), isScroll);
    }

    public void setSecond(int second, boolean isScroll) {
        if(isScroll) {
            setSlotIndexByScroll(secondIndex, second);
        } else {
            setSlotIndex(secondIndex, second);
        }
    }

    public int getSecond() {
        return getSlotIndex(secondIndex);
    }

    public static void setCurrent_hue(int hue){
        current_hue = hue;
    }
    public static void setCurrent_saturation(int saturation){
        current_saturation = saturation;
    }
    public static void setCurrent_brightness(int brightness){
        current_brightness = brightness;
    }
    public static int getCurrent_hue(){
        return current_hue;
    }
    public static int getCurrent_saturation(){
        return current_saturation;
    }

    public static int getCurrent_brightness(){
        return current_brightness;
    }

    public static LoopSlotView getHueSlot(){
        return hueSlot;
    }

    public static LoopSlotView getSaturationSlot(){
        return saturationSlot;
    }

    public static LoopSlotView getBrightnessSlot(){
        return brightnessSlot;
    }

}