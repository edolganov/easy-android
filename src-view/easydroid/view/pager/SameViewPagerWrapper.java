package easydroid.view.pager;

import java.util.Collection;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import easydroid.core.view.RowListener;
import easydroid.core.view.ViewWrapper;
import easydroid.util.android.ViewUtil;
import easydroid.view.pager.ViewPagerExt.OnCanScrollListener;

public class SameViewPagerWrapper<T> extends ViewWrapper<ViewPager>{
	
	SamePagerAdapter<T> adapter;
	
	public void initAdapter(Activity activity, int rowResource){
		adapter = new SamePagerAdapter<T>(activity, rowResource);
		view.setAdapter(adapter);
	}
	
	public void setListener(RowListener<T> listener) {
		adapter.setListener(listener);
	}

	public void clear() {
		adapter.clear();
	}

	public void add(T data) {
		adapter.add(data);
	}
	
	public void addAll(Collection<T> collection) {
		adapter.addAll(collection);
	}
	
	public void setCurrentItem(int index){
		view.setCurrentItem(index);
	}
	
	public T getItem(int index){
		return (T) adapter.get(index);
	}
	
	public int getCurrentItem(){
		return view.getCurrentItem();
	}
	
	public View getCurrentView(){
		
		int index = view.getCurrentItem();
	    return adapter.getView(index);
	}
	
	public void trySetOnCanScrollListener(OnCanScrollListener listener){
		if(view instanceof ViewPagerExt){
			ViewPagerExt ext = (ViewPagerExt) view;
			ext.setOnCanScrollListener(listener);
		} else {
			log.error("can't set OnCanScrollListener");
		}
	}

	public void setPageMarginInDp(int dpMargin) {
		int value = ViewUtil.convertDip2Pixels(view.getContext(), dpMargin);
		view.setPageMargin(value);
	}
	
	public void setOnPageChangeListener(final OnPageSelectedListener listener){
		
		view.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				listener.onPageSelected(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}
	
	

}
