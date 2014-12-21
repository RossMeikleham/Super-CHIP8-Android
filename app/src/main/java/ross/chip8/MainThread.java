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

    private SurfaceHolder surfaceHolder;
    private MainPanel gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MainPanel gamePanel, Cpu c, Chip8 c8) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        this.cpu = c;
        this.c8 = c8;
    }

    public synchronized void setRunning(boolean running) {
            this.running = running;
            System.out.println("running");
    }

    @Override
    public void run() {
        cpu.init();
        System.out.println("setup");
        while (running) {
            cpu.step();
            if (c8.graphicsUpdate) {
                gamePanel.postInvalidate();
            }
        }
    }
}