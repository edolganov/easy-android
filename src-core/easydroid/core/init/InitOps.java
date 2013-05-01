package easydroid.core.init;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;
import easydroid.core.annotation.InjectView;
import easydroid.core.view.ViewWrapper;

public class InitOps {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void injectView(Object ui, Field field, InjectView viewAnn, Object viewOrActivity) throws Exception {
		
		field.setAccessible(true);
		
		int id = viewAnn.value();
		
		View viewObj = null;
		if(viewOrActivity instanceof Activity){
			viewObj = ((Activity)viewOrActivity).findViewById(id);
		}else if(viewOrActivity instanceof View){
			viewObj = ((View)viewOrActivity).findViewById(id);
		}
		
		if(viewObj == null){
			throw new IllegalStateException("can't find view for "+field+" by id "+id);
		}
		
		
		Object injectObject = viewObj;
		
		//check if wrapper
		Class<?> fieldType = field.getType();
		if(ViewWrapper.class.isAssignableFrom(fieldType)){
			ViewWrapper wrapper = (ViewWrapper)fieldType.newInstance();
			wrapper.setView(viewObj);
			injectObject = wrapper;
		}
		
		try {
			field.set(ui, injectObject);
		}catch (Exception e) {
			throw new IllegalStateException("can't set "+injectObject+" to "+field+": "+e);
		}
		
	}

}
