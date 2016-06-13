package com.example.BluetoothRollersControl.HSBColorPicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Color.HSVToColor;

class RangedSlotView extends AbstractSlotView {
	
	public RangedSlotView(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLabelIndex() {
		int index = scroller.getCurrentIndex();
		int labelCount = labels.length;
		
		if(index < 0)
			return 0;
		if(index >= labelCount)
			return labelCount - 1;
		return index;
	}

	public void scrollToLabelIndex(int labelIndex) {
		handler.removeCallbacks(scrollHandler);
		if(isShown()) {
			scroller.moveToIndex(labelIndex);
			handler.post(scrollHandler);
		} else {
			setInitializeIndex(labelIndex, INIT_INDEX_TYPE_MOVE);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		scroller.setRange(labelHeight * (labels.length - 1), labelHeight, false);

		initialMove();
	}
	
    
    /**
     * 
     * @param canvas
     */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int currentIndex = scroller.getCurrentIndex();
		int currentOffset = scroller.getCurrentLabelOffset();
		int labelCount = labels.length;
		Paint rectPaint = new Paint();
		rectPaint.setAlpha(180);
		for(int i = SHOW_LABEL_PRE_IDX; i <= SHOW_LABEL_LAST_IDX; ++i) {
			int idx = currentIndex + i;
			if(idx >= 0 && idx < labelCount) {
				rectPaint.setColor(HSVToColor(new float[]{Integer.parseInt(labels[idx]), 100, 100}));
				canvas.drawRect(0, centerOffset + i * labelHeight - 20, 200, centerOffset + i * labelHeight + 20, rectPaint);
				canvas.drawText(labels[idx], labelRight, fontOffset + centerOffset + i * labelHeight - currentOffset, textPaint);
			}		
		}
	}
}
