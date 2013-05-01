package easydroid.view.list;

import easydroid.core.view.RowListener;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SameBaseArrayAdapter<T> extends ArrayAdapter<T>{
	
	Activity activity;
	int resource;
	RowListener<T> listener;

	public SameBaseArrayAdapter(Activity activity, int resource) {
		super(activity, resource, 0);
		this.activity = activity;
		this.resource = resource;
	}


	public void setListener(RowListener<T> listener) {
		this.listener = listener;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        T item = getItem(position);
        
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(resource, null, true);
            
            if(listener != null){
            	listener.onRowFirstCreated(position, item, rowView);
            }
		}
		
        if(listener != null){
        	listener.onRowShow(position, item, rowView);
        }
		
		
		return rowView;
	}
	
	

}
