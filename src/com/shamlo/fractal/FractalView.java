package com.shamlo.fractal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
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

	private int x = 0;
	private int y = 0;
	private int w = 0;
	private int h = 0;
	private boolean zoomMode = false;
	public void setZoomSelectionMode() {
		zoomMode = true;
	}


	public void resetZoom() {
		calculator.resetArea();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = false;
		if (zoomMode) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				x = (int)event.getX();
				y = (int)event.getY();
				
				ret = true;
			}
			else if (event.getAction() == MotionEvent.ACTION_UP) {
				w = ((int)event.getX()) - x;
				h = ((int)event.getY()) - y;
				
				if (w > x && h > y) {
					zoomMode = false;
					calculator.zoomToArea(x, y, w, h);
					
					Canvas c = holder.lockCanvas();
					if (c != null) {
						c.drawColor(Color.WHITE);
						
						holder.unlockCanvasAndPost(c);
					}
				}
				
				ret = true;
			}
			else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int nx = (int)event.getX();
				int ny = (int)event.getY();
				
				Canvas c = holder.lockCanvas();
				
				if (c != null) {
					
					Paint stroke = new Paint();
					stroke.setColor(Color.BLUE);
					stroke.setStyle(Style.STROKE);
					
					Paint fill = new Paint();
					fill.setColor(Color.CYAN);
					fill.setStyle(Style.FILL);
					fill.setAlpha(100);
					
					Rect r = new Rect(x, y, nx, ny);
					c.drawRect(r, fill);
					c.drawRect(r, stroke);
					
					holder.unlockCanvasAndPost(c);
				}
				
				ret = true;
			}
		}
		return ret;
	}

}
