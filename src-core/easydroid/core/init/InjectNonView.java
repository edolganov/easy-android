package easydroid.core.init;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import easydroid.core.Singleton;
import easydroid.core.annotation.Inject;
import easydroid.core.annotation.PostConstruct;
import easydroid.core.context.GlobalContext;

public class InjectNonView {
	
	private static ThreadLocal<Set<Class<?>>> waitingToInit = new ThreadLocal<Set<Class<?>>>();
	
	GlobalContext context = GlobalContext.getInstance();
	Object target;
	
	public InjectNonView(Object target) {
		this.target = target;
	}
	
	public void invoke(){
		try {
			injectFields();
			callPostConsruct();
		}catch (Exception e) {
			throw new IllegalStateException("can't inject data to object", e);
		}
	}

	private void injectFields() throws Exception {
		
		Class<?> curClass = target.getClass();
		while( ! Object.class.equals(curClass)){
			injectFieldsFor(curClass);
			curClass = curClass.getSuperclass();
		}
		
	}
	
	private void injectFieldsFor(Class<?> type) throws Exception {
		
		Field[] fields = type.getDeclaredFields();
		if(fields == null){
			return;
		}
		
		for (Field field : fields) {
			Inject injectAnn = field.getAnnotation(Inject.class);
			if(injectAnn != null){
				injectField(field);
			}
		}
		
	}
	
	private void callPostConsruct() throws Exception {
		
		Class<?> curClass = target.getClass();
		while( ! Object.class.equals(curClass)){
			callPostConsructFor(curClass);
			curClass = curClass.getSuperclass();
		}
	}
	
	
	private void callPostConsructFor(Class<?> type) throws Exception {
		Method[] methods = type.getDeclaredMethods();
		if(methods == null){
			return;
		}
		
		for (Method method : methods) {
			PostConstruct ann = method.getAnnotation(PostConstruct.class);
			if(ann != null){
				method.invoke(target);
			}
		}
	}
	
	
	
	private void injectField(Field field) throws Exception {
		
		field.setAccessible(true);
		
		Class<?> fieldType = field.getType();
		if(Singleton.class.isAssignableFrom(fieldType)){
			injectSingleton(fieldType, field);
		}
		else if(Context.class.isAssignableFrom(fieldType)){
			Context applicationContext = context.getApplicationContext();
			field.set(target, applicationContext);
		}
		else if(SharedPreferences.class.isAssignableFrom(fieldType)){
			Context applicationContext = context.getApplicationContext();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			field.set(target, prefs);
		}
	}

	private void injectSingleton(Class<?> singletonType, Field field) throws Exception {
		
		
		Object obj = context.getSingleton(singletonType);
		if(obj == null){
			obj = createSingleton(singletonType);
			context.putSingleton(singletonType, obj);
		}
		field.set(target, obj);
		
	}

	private Object createSingleton(Class<?> type) throws Exception {
		
		boolean isInitTreeRoot = false;
		Set<Class<?>> waitingTypes = waitingToInit.get();
		if(waitingTypes == null){
			waitingTypes = new HashSet<Class<?>>();
			waitingToInit.set(waitingTypes);
			isInitTreeRoot = true;
		}
		
		try {
			
			if(waitingTypes.contains(type)){
				throw new IllegalStateException("Cyclic dependency for "+type);
			}
			waitingTypes.add(type);
			
			Object obj = type.newInstance();
			new InjectNonView(obj).invoke();
			return obj;
			
		}finally {
			
			//clear thread's data after all work
			if(isInitTreeRoot){
				waitingToInit.remove();
			}
		}
	}

}
