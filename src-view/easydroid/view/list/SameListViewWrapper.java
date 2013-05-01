package easydroid.view.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import easydroid.core.view.RowListener;
import easydroid.core.view.ViewPair;
import easydroid.core.view.ViewWrapper;

public class SameListViewWrapper<T> extends ViewWrapper<ListView> {
	
	SameBaseArrayAdapter<T> adapter;
	
	public void initAdapter(Activity activity, int rowResource){
		adapter = new SameBaseArrayAdapter<T>(activity, rowResource);
		view.setAdapter(adapter);
	}
	
	public void setRowListener(RowListener<T> listener) {
		adapter.setListener(listener);
	}

	public void clear() {
		adapter.clear();
	}

	public void add(T data) {
		adapter.add(data);
	}
	
	public int getCount(){
		return adapter.getCount();
	}
	
	public T getItem(int position){
		return (T) adapter.getItem(position);
	}
	
	public void addAll(Collection<T> collection) {
		for (T data : collection) {
			adapter.add(data);
		}
	}
	
	public void insert(T data, int index){
		adapter.insert(data, index);
	}
	
	public T remove(int index){
		T item = adapter.getItem(index);
		adapter.remove(item);
		return item;
	}
	
	public void setSelection(int index){
		view.setSelection(index);
	}
	
	public int getFirstVisiblePosition(){
		return view.getFirstVisiblePosition();
	}
	
	public List<ViewPair<T>> getVisibleItems(){
		
		if(adapter.getCount() == 0){
			return Collections.emptyList();
		}
		
		ArrayList<ViewPair<T>> out = new ArrayList<ViewPair<T>>();
		
		int visibleIndex = 0;
		int firstIndex = view.getFirstVisiblePosition();
		int lastIndex = view.getLastVisiblePosition();
		for(int i=firstIndex; i < lastIndex+1; i++){
			T item = adapter.getItem(i);
			View itemView = view.getChildAt(visibleIndex);
			out.add(new ViewPair<T>(item, itemView));
			visibleIndex++;
		}
		
		return out;
	}
	
	public void setOnScrollStopListener(final OnScrollStopListener listener){
		view.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					listener.onScrollStop();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
		});
	}
	
	public T getOnContextMenuSelectedObject(MenuItem item){
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    return getItem(info.position);
	}

	@Override
	public String toString() {
		return "ListViewWrapper [listAdapter=" + adapter + ", view=" + view
				+ "]";
	}

}
