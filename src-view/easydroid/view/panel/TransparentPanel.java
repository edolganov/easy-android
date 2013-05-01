package easydroid.view.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TransparentPanel extends LinearLayout {
	
	Paint backgroud;
	Paint border;
	boolean paintBorder;

	public TransparentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TransparentPanel(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		backgroud = new Paint();
		backgroud.setARGB(225, 75, 75, 75); //gray
		backgroud.setAntiAlias(true);

		border = new Paint();
		border.setARGB(255, 255, 255, 255);
		border.setAntiAlias(true);
		border.setStyle(Style.STROKE);
		border.setStrokeWidth(2);
	}

	public void setBackgroud(Paint backgroud) {
		this.backgroud = backgroud;
	}

	public void setBorder(Paint border) {
		this.border = border;
	}
	

	public boolean isPaintBorder() {
		return paintBorder;
	}

	public void setPaintBorder(boolean paintBorder) {
		this.paintBorder = paintBorder;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		
    	RectF drawRect = new RectF();
    	drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
    	
    	canvas.drawRoundRect(drawRect, 5, 5, backgroud);
    	
    	if(paintBorder){
    		canvas.drawRoundRect(drawRect, 5, 5, border);
    	}
		
		super.dispatchDraw(canvas);
	}
	
	

}
