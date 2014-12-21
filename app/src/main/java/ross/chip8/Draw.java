package ross.chip8;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;


public class Draw extends View  {
	
	Integer gfx[][];
	Paint paint = new Paint();
	
	Draw(Context context, Integer gfx[][]) {
	 super(context);
	 this.setLayoutParams(new LayoutParams(512, 256));
	 paint.setStyle(Paint.Style.FILL_AND_STROKE);
     paint.setStrokeWidth(15);
     paint.setTextSize(30);
	 this.setBackgroundColor(Color.CYAN);
	 this.gfx = gfx;
	}
	
	/*void clear() {
		paint.setColor(Color.BLACK);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,510,255);
		//g.dispose();
		
	}*/

	public void onMeasure(int widthMeasureSpec , int heightMeasureSpec){
		System.out.println("Measure");
		    int desiredWidth = 100;
		    int desiredHeight = 100;

		    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		    int width;
		    int height;

		    //Measure Width
		    if (widthMode == MeasureSpec.EXACTLY) {
		        //Must be this size
		        width = widthSize;
		    } else if (widthMode == MeasureSpec.AT_MOST) {
		        //Can't be bigger than...
		        width = Math.min(desiredWidth, widthSize);
		    } else {
		        //Be whatever you want
		        width = desiredWidth;
		    }

		    //Measure Height
		    if (heightMode == MeasureSpec.EXACTLY) {
		        //Must be this size
		        height = heightSize;
		    } else if (heightMode == MeasureSpec.AT_MOST) {
		        //Can't be bigger than...
		        height = Math.min(desiredHeight, heightSize);
		    } else {
		        //Be whatever you want
		        height = desiredHeight;
		    }

		    //MUST CALL THIS
		    setMeasuredDimension(width, height);
	}

	@Override 
	public void onDraw(Canvas c) {
		   super.onDraw(c);
		   int width_inc = 512/64;
		   int height_inc = 256/32;
		   
		   for(int i = 0; i < 64; i++)
			   for(int j = 0; j < 32; j++) {
				   if(gfx[i][j]==1)
				    	paint.setColor(Color.GREEN);
				    else
				    	paint.setColor(Color.BLACK);
				   float x1 = i*width_inc;
				   float y1 = j*height_inc;
				   float x2 = (i+1)*width_inc;
				   float y2 = (j+1)*height_inc;
				   c.drawRect(x1, y1, x2, y2, paint);
				  
			   }
				   
		 System.out.println("ACTUALDRAWING");	   
	}
		   
    
	
	void update() {
		System.out.println("DRAWING");
		this.invalidate();
	}

}
