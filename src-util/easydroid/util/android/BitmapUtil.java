package easydroid.util.android;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtil {
	
	
	public static Bitmap loadByUrl(String url) throws MalformedURLException, IOException {

		InputStream is = new URL(url).openStream();
		BufferedInputStream bis = new BufferedInputStream(is);
    	Bitmap bitmap = BitmapFactory.decodeStream(bis);
	    return bitmap;
	}
	
	public static Bitmap loadByBytes(byte[] bytes) throws IOException {
		
    	Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	    return bitmap;
	}
	
	public static Bitmap loadByStream(InputStream is) throws IOException {
		
    	Bitmap bitmap = BitmapFactory.decodeStream(is);
	    return bitmap;
	}
	
	/**
	 * @param color (example: 0xff424242)
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, int color) {
		
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	

}
