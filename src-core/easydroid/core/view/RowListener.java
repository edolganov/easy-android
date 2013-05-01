package easydroid.core.view;

import android.view.View;

public abstract class RowListener<T> {
	
	public void onRowFirstCreated(int position, T item, View rowView) {};
	
	public void onRowShow(int position, T item, View rowView) {};
}