package easydroid.core.view;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("rawtypes")
public class UICache {
	
	
	WeakHashMap<Object, HashMap<Class, Object>> cache = new WeakHashMap<Object, HashMap<Class, Object>>();
	
	ReadWriteLock rw = new ReentrantReadWriteLock();
	Lock readLock = rw.readLock();
	Lock writeLock = rw.writeLock();
	

	public Object get(Object key, Class type){
		readLock.lock();
		try {
			HashMap<Class, Object> map = cache.get(key);
			return map == null? null : map.get(type);
		}finally{
			readLock.unlock();
		}
	}
	
	public void put(Object key, Class type, Object value){
		writeLock.lock();
		try {
			HashMap<Class, Object> map = cache.get(key);
			if(map == null){
				map = new HashMap<Class, Object>();
				cache.put(key, map);
			}
			map.put(type, value);
		}finally {
			writeLock.unlock();
		}
	}
	

}
