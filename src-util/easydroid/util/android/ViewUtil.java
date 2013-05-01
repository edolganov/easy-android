package easydroid.util.android;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

public class ViewUtil {
	
	public static int convertDip2Pixels(Context context, int dip) {
	    return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}
	
	public static void setVisible(View view){
		view.setVisibility(View.VISIBLE);
	}
	
	public static void setGone(View view){
		view.setVisibility(View.GONE);
	}
	
	public static void setInvisible(View view){
		view.setVisibility(View.INVISIBLE);
	}
	
	public static boolean isGone(View view){
		return view.getVisibility() == View.GONE;
	}
	
	public static boolean isVisible(View view){
		return view.getVisibility() == View.VISIBLE;
	}
	
	public static boolean isInvisible(View view){
		return view.getVisibility() == View.INVISIBLE;
	}

}
