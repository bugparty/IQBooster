package com.ifancc.braingame;

import com.ifancc.braingame.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 自定义GridView 为实现gameList3D效果
 * @author liyang
 *
 */
public class GamesAdapter extends BaseAdapter{
	
	int mGalleryItemBackground;
	private Context mContext;
	//加载资源图片
	private Integer[] mImageIds = { 
			R.drawable.speed_match_icon,
			R.drawable.memory_match_icon,
			R.drawable.top_chimp_icon, 
			R.drawable.brain_shift_icon, 
			R.drawable.color_match_icon};

	public GamesAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView i = createReflectedImages(mContext,mImageIds[position]);
		
		i.setLayoutParams(new CoverFlow.LayoutParams(200, 200));
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		
		// 设置的抗锯齿
		BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
		drawable.setAntiAlias(true);
		return i;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	
	/**
	 * 设置镜像图像
	 * @param mContext
	 * @param imageId
	 * <a href="http://my.oschina.net/u/556800" target="_blank" rel="nofollow">@return</a> 
	 */
	public ImageView createReflectedImages(Context mContext,int imageId) {

		Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
		
		final int reflectionGap = 4;
		
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage
				.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitmapWithReflection);

		return imageView;
	}
	
	
}
