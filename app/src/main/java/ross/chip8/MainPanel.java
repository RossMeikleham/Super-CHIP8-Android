package ross.chip8;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainPanel extends SurfaceView implements
        SurfaceHolder.Callback {

    Paint paint;
    Integer gfx[][];
    MainThread thread;
    Context context;
    Chip8 c8;


    int width;
    int height;

    public MainPanel(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public MainPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        setWillNotDraw(false);

        // Setup Emulation
        this.c8 = new Chip8();
        this.gfx = c8.gfx;
        C8opcodes c8Op = new C8opcodes(c8);
        Cpu c = new Cpu(context, "INVADERS" , c8, c8Op);

        paint = new Paint();
        thread = new MainThread(this, c, c8);
        Log.w("PAINT", paint.toString());
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        thread.setRunning(false);
        try {
            thread.join();
        } catch (InterruptedException e) {}

     }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        int width_inc = width/64;
        int height_inc = height/32;

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

    }

    @Override
    public void onMeasure(int widthMeasureSpec , int heightMeasureSpec){
        System.out.println("Measure");
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int desiredWidth = size.x;
        int desiredHeight = size.y;

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
        this.width = width;
        this.height = height;
    }
}