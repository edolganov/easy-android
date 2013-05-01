package easydroid.view.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerExt extends ViewPager {
	
	public static interface OnCanScrollListener {
		
		public boolean canScroll(ViewPagerExt pagerView, View v, boolean checkV, int dx, int x, int y);
		
	}
	
	private OnCanScrollListener canScrollListener;

	public ViewPagerExt(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewPagerExt(Context context) {
		super(context);
	}
	
	
	public void setOnCanScrollListener(OnCanScrollListener canScrollListener) {
		this.canScrollListener = canScrollListener;
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if(canScrollListener != null){
			return canScrollListener.canScroll(this, v, checkV, dx, x, y);
		} else {
			return defaultCanScroll(v, checkV, dx, x, y);
		}
	}
	
	public boolean defaultCanScroll(View v, boolean checkV, int dx, int x, int y){
		return super.canScroll(v, checkV, dx, x, y);
	}
	
	

}
