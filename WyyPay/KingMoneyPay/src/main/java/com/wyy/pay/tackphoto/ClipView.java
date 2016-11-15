package com.wyy.pay.tackphoto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
    public static final int SX = 5;//显示器X轴起始余量
    public static final int EX = 5;//显示器X轴结束余量
    private int clipW;
    private int clipH;
    
    public ClipView(Context context) {
        super(context);
    }
    
    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ClipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);  /*这里就是绘制矩形区域*/
        int width = this.getWidth();
        int height = this.getHeight();
        Paint paint = new Paint();  paint.setColor(0xaa000000);
        //top
        canvas.drawRect(0, 0, width, (height - clipH)/2, paint);
        //left
        canvas.drawRect(0, (height - clipH)/2, (width - clipW)/2, (height + clipH)/2, paint);
        //right
        canvas.drawRect((width + clipW)/2, (height - clipH)/2, width , (height + clipH)/2, paint);
        //bottom
        canvas.drawRect(0, (height + clipH)/2, width, height, paint);
        
        Paint paintLine = new Paint();  paintLine.setColor(0xFFFFFFFF);
        canvas.drawLine((width - clipW)/2, (height - clipH)/2, (width + clipW)/2, (height - clipH)/2, paintLine);
        canvas.drawLine((width - clipW)/2, (height - clipH)/2, (width - clipW)/2, (height + clipH)/2, paintLine);
        canvas.drawLine((width + clipW)/2 - 1, (height - clipH)/2, (width + clipW)/2, (height + clipH)/2, paintLine);
        canvas.drawLine((width - clipW)/2, (height + clipH)/2 - 1, (width + clipW)/2, (height + clipH)/2 - 1, paintLine);
    }

    public int getClipW() {
        return clipW;
    }

    public void setClipW(int clipW) {
        this.clipW = clipW;
    }

    public int getClipH() {
        return clipH;
    }

    public void setClipH(int clipH) {
        this.clipH = clipH;
    }
    
    public int getClipLeft() {
        return (getWidth() - clipW)/2;
    }
    
    public int getClipTop() {
        return (getHeight() - clipH)/2;
    }
    
    public int getClipRight() {
        return (getWidth() + clipW)/2;
    }
    
    public int getClipBottom() {
        return (getHeight() + clipH)/2;
    }
}
		
		/**
		 * int titleBarH = 0;
			int stateBarH = 0;
			
			x1 =pw/2-330;
			y1 = py/2-195-titleBarH-stateBarH;
			x2 =pw/2+330;
			y2 =y1;
			x3 = x1;
			y3=py/2+195-titleBarH-stateBarH;
			x4=x2;
			y4=y3
		 */
		
		/**(0,0)
		 * ****************************************(pw,0)
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 * (x1,y1)                       (x2,y2)  *
		 *      ****************************      *
		 *      *                          *      *                      
		 *      *                          *      *                     
		 *      *                          *      *                     
		 *      *                          *      *                     
		 *      *                          *      *                     
		 *      *                          *      *                     
		 *      *                          *      *                     
		 *      ****************************      *
		 * (x3,y3)                       (x4,y4)  *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 *                                        *
		 ******************************************(pw,py)
		 * (0,py)
		 */
		
		

