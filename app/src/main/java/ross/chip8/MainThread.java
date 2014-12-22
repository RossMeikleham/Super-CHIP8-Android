package ross.chip8;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by ross on 19/12/14.
 */
public class MainThread extends Thread {

    // flag to hold game state
    private boolean running;
    private Cpu cpu;
    private Chip8 c8;
    private final Object lock = new Object();

    private MainPanel gamePanel;

    public MainThread(MainPanel gamePanel, Cpu c, Chip8 c8) {
        super();
        this.gamePanel = gamePanel;
        this.cpu = c;
        this.c8 = c8;
    }

    public void setRunning(boolean running) {
            synchronized (lock) {
                this.running = running;
                System.out.println("running");
            }
    }



    @Override
    public void run() {
        cpu.init();
        System.out.println("setup");
        boolean running;
        synchronized(lock) {
           running = this.running;
        }
        while (running) {
            cpu.step();
            //System.out.println(((Boolean)(c8.get_key_pressed(0))));
            if (c8.graphicsUpdate) {
                gamePanel.postInvalidate();
            }

            synchronized(lock) {
                running = this.running;
            }
        }
    }
}