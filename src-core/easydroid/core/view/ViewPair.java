package easydroid.core.view;

import easydroid.util.model.Pair;
import android.view.View;

public class ViewPair<T> extends Pair<T, View>{
	
	
	public ViewPair() {
		super();
	}

	public ViewPair(T item, View itemView) {
		super(item, itemView);
	}

	public T getItem(){
		return first;
	}
	
	public View getView(){
		return second;
	}

}
