package easydroid.core.init;

import java.lang.reflect.Field;

import easydroid.core.annotation.InjectView;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;

public class CreateUI<T> {
	
	public static final Logger log = LogFactory.getLog(CreateUI.class);
	
	Object activityOrView;
	Class<T> type;

	public CreateUI(Object activityOrView, Class<T> type) {
		super();
		this.activityOrView = activityOrView;
		this.type = type;
	}
	
	public T invoke(){
		try {
			return createUI();
		}catch (Exception e) {
			throw new IllegalStateException("can't create "+type+" from "+activityOrView, e);
		}
	}

	private T createUI() throws Exception {
		
		T ui = (T) type.newInstance();
		
		Field[] uiFields = type.getDeclaredFields();
		if(uiFields != null){
			for (Field uiField : uiFields) {
				InjectView viewAnn = uiField.getAnnotation(InjectView.class);
				if(viewAnn != null){
					InitOps.injectView(ui, uiField, viewAnn, activityOrView);
				}
			}
		}
		
		return ui;
	}

}
