package easydroid.view.pager;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import easydroid.core.view.RowListener;

public class SamePagerAdapter<T> extends PagerAdapter {
	
	private static class ViewWrapper {
		
		int id;
		View view;
		int position = -1;
		
		public ViewWrapper(int id) {
			super();
			this.id = id;
		}

		@Override
		public String toString() {
			return "ViewWrapper [id=" + id + ", view=" + view + "]";
		}
		
		
	}
	
	
	private ViewWrapper[] wrappers = new ViewWrapper[4];
	{
		wrappers[0] = new ViewWrapper(1);
		wrappers[1] = new ViewWrapper(2);
		wrappers[2] = new ViewWrapper(3);
		wrappers[3] = new ViewWrapper(4);
	}
	
	private ArrayList<T> data = new ArrayList<T>();
	private RowListener<T> listener;
	
	private Activity activity;
	private int resource;
	
	
	public SamePagerAdapter(Activity activity, int resource) {
		super();
		this.activity = activity;
		this.resource = resource;
	}
	

	public void setListener(RowListener<T> listener) {
		this.listener = listener;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		T item = data.get(position);
		
		ViewWrapper viewWrapper = getViewWrapper(position);
		if(viewWrapper.view == null){
			
			LayoutInflater inflater = activity.getLayoutInflater();
			viewWrapper.view = inflater.inflate(resource, null, true);
            
            if(listener != null){
            	listener.onRowFirstCreated(position, item, viewWrapper.view);
            }
		}
		
		viewWrapper.position = position;
		View curView = viewWrapper.view;
        
        ViewPager pager = (ViewPager) container;
		pager.addView(curView, 0);
        
        if(listener != null){
        	listener.onRowShow(position, item, curView);
        }
        
		return curView;
	}

	private ViewWrapper getViewWrapper(int position) {
		int viewIndex = position % 4;
		return wrappers[viewIndex];
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
		
		ViewWrapper viewWrapper = getViewWrapper(position);
		viewWrapper.position = -1;
	}
	
	public View getView(int position){
		ViewWrapper viewWrapper = getViewWrapper(position);
		return viewWrapper.position == position ? viewWrapper.view : null;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	public void add(T item){
		data.add(item);
		notifyDataSetChanged();
	}
	
	public T get(int index){
		return data.get(index);
	}
	
	public void addAll(Collection<T> collection){
		data.addAll(collection);
		notifyDataSetChanged();
	}
	

	public void clear() {
		
		data.clear();
		notifyDataSetChanged();
		clearWrappers();
	}


	private void clearWrappers() {
		for (ViewWrapper wrapper : wrappers) {
			wrapper.position = -1;
			wrapper.view = null;
		}
	}

}
