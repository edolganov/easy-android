package easydroid.core.init;

import android.app.Activity;
import easydroid.core.annotation.ContentView;

public class SetContentView {
	
	Activity target;
	
	public SetContentView(Activity target) {
		this.target = target;
	}
	
	public void invoke(){
		
		int id = 0;
		
		try {
			
			ContentView contentViewAnn = target.getClass().getAnnotation(ContentView.class);
			if(contentViewAnn != null){
				id = contentViewAnn.value();
				target.setContentView(id);
			}
			
		}catch (Exception e) {
			throw new IllegalStateException("can't set content view to activity by id: "+id, e);
		}
	}

}
