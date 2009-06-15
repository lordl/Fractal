package com.shamlo.fractal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public class FractalActivity extends Activity {
	public static final int MENU_START = 1;
	public static final int MENU_STOP  = 2;
	public static final int MENU_PAUSE = 3;
	public static final int MENU_SETUP = 4;
	public static final int MENU_SETUP_ZOOM       = 5;
	public static final int MENU_SETUP_ZOOM_RESET = 6;
	public static final int MENU_SETUP_COLOR      = 7;
	
	public static final int MENU_SETUP_ID = 1;
		
    private FractalView view;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        view = new FractalView(getApplicationContext());
        setContentView(view);
        
        
        Log.i("Fractal", "onCreate() done.");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, MENU_START, 1, "Start");
    	menu.add(Menu.NONE, MENU_PAUSE, 2, "Pause");
    	menu.add(Menu.NONE, MENU_STOP,  3, "Stop");
    	
    	SubMenu setup = menu.addSubMenu(Menu.NONE, MENU_SETUP, 4, "Setup");
    	
    	setup.add(MENU_SETUP_ID, MENU_SETUP_ZOOM, 1, "Zoom");
    	setup.add(MENU_SETUP_ID, MENU_SETUP_ZOOM_RESET, 2, "Reset Zoom");
    	setup.add(MENU_SETUP_ID, MENU_SETUP_COLOR, 3, "Color");
    	
    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	boolean running = view.isRunning();
    	
    	MenuItem item = menu.findItem(MENU_PAUSE);
    	item.setEnabled(running);
    	
    	item = menu.findItem(MENU_START);
    	item.setEnabled(!running);
    	
    	item = menu.findItem(MENU_SETUP);
    	item.setEnabled(!running);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case MENU_START:
    		view.start();
    		return true;
    	case MENU_PAUSE:
    		view.pause();
    		return true;
    	case MENU_STOP:
    		view.stop();
    		return true;
    	case MENU_SETUP_ZOOM:
    		view.setZoomSelectionMode();
    		return true;
    	case MENU_SETUP_ZOOM_RESET:
    		view.resetZoom();
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    protected void onPause() {
    	Log.i("FractalActivy", "onPause()");
    	super.onPause();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	Log.i("Fractal", "onWindowFocusChanged() " + hasFocus);

    	super.onWindowFocusChanged(hasFocus);
    }
    
    @Override
    protected void onResume() {
    	Log.i("Fractal","onResume()");
    	super.onResume();
    }
}