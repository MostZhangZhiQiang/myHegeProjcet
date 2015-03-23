package com.hege.pts.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class CircleBitmapDisplayer implements BitmapDisplayer {
	protected final int margin;


	public CircleBitmapDisplayer(int marginPixels) {
		this.margin = marginPixels;
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException(
					"ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new CircleDrawable(bitmap,
				margin));
	}

	protected static class CircleDrawable extends Drawable {

		protected final int margin;

		protected final RectF mRect = new RectF();
		protected final BitmapShader bitmapShader;
		protected final Paint paint;
		protected float bitmap_width=0.00f,bitmap_height=0.00f;

		CircleDrawable(Bitmap bitmap, int margin) {
			this.margin = margin;
			if(bitmap!=null){
				bitmap_width=bitmap.getWidth();
				bitmap_height=bitmap.getHeight();
			}
			bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(margin, margin, bounds.width() - margin, bounds.height()
					- margin);
			computeBitmapShaderSize();
		}

		private void computeBitmapShaderSize() {
			if(mRect==null)
				return;
			Matrix matrix=new Matrix();
			float scaleX=mRect.width()/bitmap_width;
			float scaleY=mRect.height()/bitmap_height;
			Log.e(CircleBitmapDisplayer.class.getSimpleName(), "---scaleX----"+scaleX+"----scaleY----"+scaleY);
			float scale=Math.max(scaleX, scaleY);
			matrix.postScale(scale, scale);
			bitmapShader.setLocalMatrix(matrix);
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawCircle(mRect.centerX(), mRect.centerY(),Math.min(mRect.centerX()-margin, mRect.centerY()-margin), paint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			paint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			paint.setColorFilter(cf);
		}
	}
}
