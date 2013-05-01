package easydroid.core.shutdown;

import java.lang.reflect.Field;

import easydroid.util.Util;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;
import easydroid.util.model.Shutdownable;


public class ShutdownOnDestroy {
	
	Logger log = LogFactory.getLog(getClass());
	Object target;

	public ShutdownOnDestroy(Object target) {
		super();
		this.target = target;
	}
	
	public void invoke(){
		try {
			
			Class<? extends Object> clazz = target.getClass();
			Field[] fields = clazz.getDeclaredFields();
			if(Util.isEmpty(fields)){
				return;
			}
			for (Field field : fields) {
				tryShutdown(field);
			}
			
		}catch (Exception e) {
			log.error("can't invoke", e);
		}
	}

	private void tryShutdown(Field field) {
		try {
			
			easydroid.core.annotation.ShutdownOnDestroy flag = field.getAnnotation(easydroid.core.annotation.ShutdownOnDestroy.class);
			if(flag == null){
				return;
			}
			
			field.setAccessible(true);
			Object ob = field.get(target);
			if(ob == null){
				return;
			}
			
			if(ob instanceof Shutdownable){
				log.info("shutdown object: "+ob);
				((Shutdownable)ob).shutdown();
			} else {
				log.warn("unknown ob type to shutdown: "+field);
			}
			
			
		}catch (Exception e) {
			log.error("can't shutdown "+field, e);
		}
	}

}
