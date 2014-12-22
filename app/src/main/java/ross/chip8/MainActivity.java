package ross.chip8;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen view
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //                     WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //MainPanel mainPanel = new MainPanel(this);
        setContentView(R.layout.main_layout);


        MainPanel mp = (MainPanel) findViewById(R.id.main_panel);
        setupButtons(mp.c8);

	}


    // Setup button listeners for controlling the games
    public void setupButtons(final Chip8 c8) {

        for (Integer j = 0; j < 16; j++) {
            final Integer i = j;
            String buttonName = "button" + i.toString();
            int resID = getResources().getIdentifier(buttonName,
                "id", this.getPackageName());

        Button b = (Button) findViewById(resID);
            b.setOnTouchListener(new Button.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        // Button Pressed
                        case MotionEvent.ACTION_DOWN : {
                            c8.set_key_pressed(i, true);
                            break;
                        }
                        // Button Unpressed
                        case MotionEvent.ACTION_UP : {
                            c8.set_key_pressed(i, false);
                        }
                    }
                    return true;
            }});
    }
   }



}
