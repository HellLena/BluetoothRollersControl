package com.example.BluetoothRollersControl.HSBColorPicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import static android.graphics.Color.HSVToColor;

public class LoopSlotView extends AbstractSlotView {

	private String HSBparam;
	
	public LoopSlotView(Context context) {
		super(context);
	}

	/**
	 * 
	 * @return
	 */
	public int getLabelIndex() {
		int index = scroller.getCurrentIndex();
		int labelCount = labels.length;
		
		index %= labelCount;
		if(index < 0)
			index += labelCount;
		return index;
	}

	public void setLabelIndex(int labelIndex) {
		int labelCount = labels.length;
		super.setLabelIndex(labelIndex % labelCount);
	}

	public void scrollToLabelIndex(int labelIndex) {
		int labelCount = labels.length;
		super.scrollToLabelIndex(labelIndex % labelCount);
	}

	public void setHSBparam(String HSBparam){
		this.HSBparam = HSBparam;
	}

	public String getHSBparam(){
		return this.HSBparam;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		scroller.setRange(labelHeight * (labels.length - 1), labelHeight, true);
		
		initialMove();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		scroller.scroll(distanceY);
		//Log.d("onScroll", scroller.toString());
		invalidate();
		switch (this.HSBparam){
			case "hue": CustomTimeScrollPickerView.getSaturationSlot().invalidate();
						CustomTimeScrollPickerView.getBrightnessSlot().invalidate();
						break;
			case "saturation": CustomTimeScrollPickerView.getHueSlot().invalidate();
				               CustomTimeScrollPickerView.getBrightnessSlot().invalidate();
				               break;
			case "brightness": CustomTimeScrollPickerView.getSaturationSlot().invalidate();
				               CustomTimeScrollPickerView.getHueSlot().invalidate();
				               break;
			default: break;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int currentIndex = scroller.getCurrentIndex();
		int currentOffset = scroller.getCurrentLabelOffset();
		int labelCount = labels.length;
		Paint rectPaint = new Paint();
		Paint linePaint = new Paint();
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setColor(Color.parseColor("#8F8F8F"));
		linePaint.setAlpha(150);
		linePaint.setStrokeWidth(1);
		// 前後3件表示
		for(int i = SHOW_LABEL_PRE_IDX; i <= SHOW_LABEL_LAST_IDX; ++i) {
			int idx = currentIndex + i;
			idx %= labelCount;
			if(idx < 0)
				idx += labelCount;
			switch (this.HSBparam){
				case "hue": if(i == SHOW_LABEL_PRE_IDX + 3) CustomTimeScrollPickerView.setCurrent_hue(Integer.parseInt(labels[idx]));
							rectPaint.setColor(HSVToColor(new float[]{Float.parseFloat(labels[idx]),
									                                  CustomTimeScrollPickerView.getCurrent_saturation() / (float)100,
									                                  CustomTimeScrollPickerView.getCurrent_brightness() / (float)100}));
							break;
				case "saturation": if(i == SHOW_LABEL_PRE_IDX + 3) CustomTimeScrollPickerView.setCurrent_saturation(Integer.parseInt(labels[idx]));
					               rectPaint.setColor(HSVToColor(new float[]{CustomTimeScrollPickerView.getCurrent_hue(),
										                                     Float.parseFloat(labels[idx])/(float)100,
										                                     CustomTimeScrollPickerView.getCurrent_brightness()/(float)100}));
                                   break;
				case "brightness": if(i == SHOW_LABEL_PRE_IDX + 3) CustomTimeScrollPickerView.setCurrent_brightness(Integer.parseInt(labels[idx]));
					               rectPaint.setColor(HSVToColor(new float[]{CustomTimeScrollPickerView.getCurrent_hue(),
										                                     CustomTimeScrollPickerView.getCurrent_saturation()/(float)100,
										                                     Float.parseFloat(labels[idx])/(float)100}));
					               break;
				default: break;
			}
			canvas.drawRect(0, centerOffset + i * labelHeight - 20, 200, centerOffset + i * labelHeight + 20, rectPaint);
			if(i == SHOW_LABEL_PRE_IDX + 3) {
				canvas.drawLine(0, centerOffset + i * labelHeight - 24, 200, centerOffset + i * labelHeight - 24, linePaint);
			}
			canvas.drawText(labels[idx], labelRight, fontOffset + centerOffset + i * labelHeight - currentOffset, textPaint);

		}
	}
	
}
