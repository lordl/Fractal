package com.shamlo.fractal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class FractalActivity extends Activity {
	public static final int MENU_START = 1;
	public static final int MENU_STOP  = 2;
	public static final int MENU_PAUSE = 3;
	
    private FractalView view;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        view = new FractalView(getApplicationContext());
        setContentView(view);
        
        
        Log.i("FractalActivity", "onCreate() done.");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, MENU_START, 1, "Start");
    	menu.add(Menu.NONE, MENU_PAUSE, 2, "Pause");
    	menu.add(Menu.NONE, MENU_STOP, 3, "Stop");
    	
    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	boolean running = view.isRunning();
    	
    	MenuItem item = menu.findItem(MENU_PAUSE);
    	item.setEnabled(running);
    	
    	item = menu.findItem(MENU_START);
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
    	Log.i("FractalActivity", "onWindowFocusChanged() " + hasFocus);
    	
    	if (hasFocus) {
    		view.calculate();
    	}
    	
    	super.onWindowFocusChanged(hasFocus);
    }
    
    @Override
    protected void onResume() {
    	Log.i("FractalActivity","onResume()");
    	super.onResume();
    }
}