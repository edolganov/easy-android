package easydroid.core.init;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import easydroid.core.annotation.OnActivityResult;
import easydroid.util.Util;

public class FindOnActivityResultMethods {
	
	public static class MethodWrapper {
		
		final Object target;
		final Method method;
		final int requestCode;
		final int resultCode;
		
		public MethodWrapper(Object target, Method method, OnActivityResult ann) {
			
			this.target = target;
			this.method = method;
			this.requestCode = ann.requestCode();
			this.resultCode = ann.resultCode();
			
		}

		public boolean isTarget(int requestCode, int resultCode) {
			return this.requestCode == requestCode && this.resultCode == resultCode;
		}

		public void invoke(Intent data) {
			
			try {
				
				method.setAccessible(true);
				Class<?>[] params = method.getParameterTypes();
				
				if(Util.isEmpty(params)){
					method.invoke(target);
					return;
				}
				
				if(params.length == 1){
					if(Intent.class.isAssignableFrom(params[0])){
						method.invoke(target, data);
						return;
					}
				}
				
				throw new IllegalStateException("unknow parameter types in "+method);
				
			}catch (Exception e) {
				throw new IllegalStateException("can't call "+method, e);
			}
			
		}
		
	}
	
	Object target;

	public FindOnActivityResultMethods(Object target) {
		super();
		this.target = target;
	}
	
	public List<MethodWrapper> invoke(){
		try {
			return findAll();
		}catch (Exception e) {
			throw new IllegalStateException("can't init OnActivityResultMethods", e);
		}
	}

	private List<MethodWrapper> findAll() {
		
		Method[] methods = target.getClass().getDeclaredMethods();
		if(methods == null){
			return Collections.emptyList();
		}
		
		ArrayList<MethodWrapper> out = new ArrayList<MethodWrapper>();
		
		for (Method method : methods) {
			OnActivityResult ann = method.getAnnotation(OnActivityResult.class);
			if(ann != null){
				out.add(new MethodWrapper(target, method, ann));
			}
		}
		
		return out;
		
	}

}
