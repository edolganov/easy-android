package easydroid.core.view;

import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;
import android.view.View;

public abstract class ViewWrapper<T extends View> {
	
	protected Logger log = LogFactory.getLog(getClass());
	
	protected T view;

	public void setView(T view) {
		this.view = view;
	}

	public T getView() {
		return view;
	}
	
	
	
	

}
