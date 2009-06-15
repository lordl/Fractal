package com.shamlo.fractal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

public class FractalCalculator implements Runnable {

	private Thread thread;
	private boolean active = false;
	
	private View view;
	private SurfaceHolder holder;
	
	private int width;
	private int height;
	private boolean landscape = false;
	private boolean smooth = false;
	private int[][] result;
	private int completed;
	
	private double minRe = -2.0;
	private double maxRe = 1.0;
	private double minIm = -1.0;
	private double maxIm = 1.0;

	public FractalCalculator(View view) {
		
		completed = 0;
		this.view = view;
	}

	public void start(SurfaceHolder holder, boolean landscape) {
		
		this.holder = holder;
		active = true;

		if (completed == 0) {
			this.landscape = landscape;
			this.width = view.getWidth();
			this.height = view.getHeight();

			result = new int[height][width];

			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					result[i][j] = 0;
				}
			}
		}
		
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		active = false;
		completed = 0;
	}
	
	public void pause() {
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}

	public void run() {
		calculate(completed, minRe, maxRe, minIm, maxIm);
		active = false;
		
		if (completed == height - 1) {
			completed = 0;
		}
	}
	
	public void zoomToArea(int x, int y, int width, int height) {
		double Re_factor;
		double Im_factor;
		double tmpMin;

		if (landscape) {
			height = (int)((this.height/(float)this.width)*width);
			
			Re_factor = (maxRe-minRe)/(this.height-1);
			Im_factor = (maxIm-minIm)/(this.width-1);
			
			tmpMin = minIm;
			minIm = tmpMin + x*Im_factor;
			maxIm = tmpMin + (x+width)*Im_factor;
			
			tmpMin = minRe;
			minRe = tmpMin + y*Re_factor;
			maxRe = tmpMin + (y+height)*Re_factor;
		}
		else {
			Re_factor = (maxRe-minRe)/(this.width-1);
			Im_factor = (maxIm-minIm)/(this.height-1);
			
			tmpMin = minRe;
			minRe  = minRe + x*Re_factor;
			maxRe  = minRe + (x+width)*Re_factor;
			
			tmpMin = minIm;
			minIm  = tmpMin + y*Im_factor;
			maxIm  = tmpMin + (y+height)*Im_factor;
		}
		
		Log.i("FractalActivty", "New ("+x+", "+y+", "+ width+", "+height+"): " + minIm + ", " + maxIm + "; " + minRe + ", " + maxRe);
	}
	
	public void resetArea() {
		minRe = -2.0;
		maxRe = 1.0;
		minIm = -1.0;
		maxIm = 1.0;
	}

	/*
	 * Z_0     = c
	 * Z_(n+1) = (Z_n)^2 + c  
	 */
	private void calculate(int y, double minRe, double maxRe, double minIm, double maxIm) {

		double Re_factor;
		double Im_factor;

		if (landscape) {
			Re_factor = (maxRe-minRe)/(height-1);
			Im_factor = (maxIm-minIm)/(width-1);
		}
		else {
			Re_factor = (maxRe-minRe)/(width-1);
			Im_factor = (maxIm-minIm)/(height-1);
		}

		double c_re = 0, c_im = 0;
		int max_iteration = 40;
		for (int i=y; i<height; i++) {
			if (landscape) {
				c_re = minRe + i*Re_factor;
			}
			else {
				c_im = maxIm - i*Im_factor;
			} 
			
			for (int j=0; j<width; j++) {
				int iteration = 0;

				if (landscape) {
					c_im = minIm + j*Im_factor;
				}
				else {
					c_re = minRe + j*Re_factor;
				}
				
				double Z_re = c_re;
				double Z_im = c_im;

				while (iteration < max_iteration) {
					double Z_re2 = Z_re * Z_re;
					double Z_im2 = Z_im * Z_im;

					if(Z_re2 + Z_im2 > 4)
					{
						break;
					}
					Z_im = 2 * Z_re  * Z_im  + c_im;
					Z_re =     Z_re2 - Z_im2 + c_re;
					
					iteration++;
				}

				if (iteration == max_iteration) {
					result[i][j] = Color.BLACK;
				}
				else if (iteration < max_iteration/2) {
					int c = 0;
					if (smooth) {
						double v = iteration + (Math.log(Math.log(2)) - Math.log(Math.log(Math.sqrt(Z_re*Z_re+Z_im*Z_im))))/Math.log(2.0);
						c = (int)(v*(255/((max_iteration/2.0))) + 0.5);
					}
					else {
						c = (int)(iteration*(255/(max_iteration/2.0)) + 0.5);
					}
					result[i][j] = Color.rgb(0, c, 0);
				}
				else {
					int c = 0;
					if (smooth) {
						double v = iteration + (Math.log(Math.log(2)) - Math.log(Math.log(Math.sqrt(Z_re*Z_re+Z_im*Z_im))))/Math.log(2.0);
						c = (int)(v*(255/(max_iteration)) + 0.5);
					}
					else {
						c = (int)(iteration*(255.0/max_iteration) + 0.5);
					} 
					result[i][j] = Color.rgb(c, 255, c);
				}
			}

			if (!active) {
				break;
			}
			else {
				emitRow(i);
			}
		}
		
	}
	
	private void emitRow(int row) {

		completed = row;
		Paint paint = new Paint();
		Rect rect = new Rect(0, row, width, row + 1);
		Canvas canvas = holder.lockCanvas(rect);

		if (canvas != null) {
			for (int x=0; x<result[row].length; x++) {
				paint.setColor(result[row][x]);
				canvas.drawPoint(x, row, paint);
			}

			holder.unlockCanvasAndPost(canvas);
		}
	}
	
}
