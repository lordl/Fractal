package com.shamlo.fractal;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class FractalView extends SurfaceView implements Callback {

	private FractalCalculator calculator;
	private SurfaceHolder holder;
	
	public FractalView(Context context) {
		super(context);
		
		SurfaceHolder holder = getHolder();
		
		holder.addCallback(this);
		this.calculator = new FractalCalculator(this);
		
	}


	public void calculate() {
//		calculator.start(getHolder(), false);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("FractalActivity", "Surface created");
		this.holder = holder;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	public void start() {
		if (holder != null) {
			calculator.start(holder, true);
		}
	}
	
	public void stop() {
		if (holder != null) {
			calculator.stop();
		}
	}
	
	public void pause() {
		if (holder != null) {
			calculator.pause();
		}
	}


	public boolean isRunning() {
		return calculator.isActive();
	}

}
