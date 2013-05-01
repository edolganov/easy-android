package easydroid.core.init;

import java.lang.reflect.Field;

import android.app.Activity;
import easydroid.core.annotation.Inject;
import easydroid.core.annotation.InjectView;
import easydroid.core.view.BaseUI;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;

public class InjectViewAndUI {
	
	public static final Logger log = LogFactory.getLog(InjectViewAndUI.class);
	
	Activity target;
	
	public InjectViewAndUI(Activity target) {
		this.target = target;
	}
	
	public void invoke(){
		try {
			injectFields();
		}catch (Exception e) {
			throw new IllegalStateException("can't inject view to activity", e);
		}
	}



	private void injectFields() throws Exception {
		
		Field[] fields = target.getClass().getDeclaredFields();
		if(fields == null){
			return;
		}
		
		for (Field field : fields) {
			Inject injectAnn = field.getAnnotation(Inject.class);
			if(injectAnn != null){
				injectField(field);
			} else {
				InjectView injectViewAnn = field.getAnnotation(InjectView.class);
				if(injectViewAnn != null){
					InitOps.injectView(target, field, injectViewAnn, target);
				}
			}
		}
		
	}
	
	
	
	private void injectField(Field field) throws Exception {
		
		field.setAccessible(true);
		
		Class<?> fieldType = field.getType();
		if(BaseUI.class.isAssignableFrom(fieldType)){
			injectUI(fieldType, field);
		}
	}



	private void injectUI(Class<?> fieldType, Field field) throws Exception {
		
		Object ui = fieldType.newInstance();
		
		Field[] uiFields = fieldType.getDeclaredFields();
		if(uiFields != null){
			for (Field uiField : uiFields) {
				InjectView viewAnn = uiField.getAnnotation(InjectView.class);
				if(viewAnn != null){
					InitOps.injectView(ui, uiField, viewAnn, target);
				}
			}
		}
		
		field.set(target, ui);
		
	}


}
