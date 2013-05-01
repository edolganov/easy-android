package easydroid.core.context;

import java.util.HashMap;

import android.content.Context;

public class GlobalContext {
	
	private static GlobalContext instance = new GlobalContext();
	
	public static GlobalContext getInstance(){
		return instance;
	}
	
	private HashMap<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();
	
	private Context applicationContext;
	
	
	GlobalContext(){
		
	}


	public synchronized Object getSingleton(Class<?> singletonType) {
		return singletons.get(singletonType);
	}


	public synchronized void putSingleton(Class<?> type, Object obj) {
		singletons.put(type, obj);
	}


	public Context getApplicationContext() {
		return applicationContext;
	}


	public void setApplicationContext(Context applicationContext) {
		if(this.applicationContext == null){
			this.applicationContext = applicationContext;
		}
	}

	
	

}
