/*
 * Copyright 2012 Evgeny Dolganov (evgenij.dolganov@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package easydroid.gf.core.deploy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import easydroid.gf.annotation.Mapping;
import easydroid.gf.exception.deploy.DeployException;
import easydroid.gf.exception.deploy.NoMappingAnnotationException;
import easydroid.gf.exception.deploy.NotOneHandlerException;
import easydroid.util.Util;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;

public class TypesRepositoryImpl implements TypesRepository {
	
	private static Logger log = LogFactory.getLog(TypesRepositoryImpl.class);
	
	//[target - [current handlers]]
	private HashMap<Class<?>, Set<Class<?>>> initialMapping = new HashMap<Class<?>, Set<Class<?>>>();
	
	//[target - [all handlers (from cur, superclass, interfaces)]]
	private HashMap<Class<?>, Set<Class<?>>> targetCache = new HashMap<Class<?>, Set<Class<?>>>();
	
	private boolean isOneHandler = false;
	
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	private Lock readLock = rw.readLock();
	private Lock writeLock = rw.writeLock();
	
	@Override
	public Set<Class<?>> put(Class<?> handler) throws DeployException {
		writeLock.lock();
		try {
			
			HashSet<Class<?>> out = new HashSet<Class<?>>();
			
			Mapping annotation = handler.getAnnotation(Mapping.class);
			if(Util.isEmpty(annotation)){
				throw new NoMappingAnnotationException(handler);
			}
			
			HashMap<Class<?>, Set<Class<?>>> newMap = new HashMap<Class<?>, Set<Class<?>>>(initialMapping);
			Class<?>[] targets = annotation.value();
			for(Class<?> target : targets){
				boolean added = putToMapping(newMap, target, handler);
				if(added){
					out.add(target);
				}
			}
			
			if(isOneHandler){
				checkForManyHandlers(newMap);
			}
			
			//if ok: replace map, clear cache
			initialMapping = newMap;
			targetCache = new HashMap<Class<?>, Set<Class<?>>>();
			
			return out;
			
		}finally {
			writeLock.unlock();
		}
	}
	
	
	@Override
	public Set<Class<?>> getTypes(Class<?> target) {
		
		//check-create
		readLock.lock();
		if ( ! hasInTargetCache(target)) {
			readLock.unlock();
			writeLock.lock();
		    try {
		    	if ( ! hasInTargetCache(target)) {
		    		putToTargetCache(target);
		    	}
		    	readLock.lock();
		    } finally {
		    	writeLock.unlock();
		    }
		}

	    //read
		try {
			Set<Class<?>> out = getFromTargetCache(target);
			return out;
		} finally {
			 readLock.unlock();
		}
	}
	
	@Override
	public void setOneHandlerOnly(boolean val) {
		writeLock.lock();
		try {
			
			if(val && !isOneHandler){
				checkForManyHandlers(initialMapping);
			}
			
			this.isOneHandler = val;
			
		}finally {
			writeLock.unlock();
		}
	}


	@Override
	public boolean isOneHandlerOnly() {
		readLock.lock();
		try {
			
			return isOneHandler;
			
		} finally {
			readLock.unlock();
		}
		
	}

	
	@Deprecated
	Map<Class<?>, Set<Class<?>>> getInitalMapping(){
		readLock.lock();
		try {
			return new HashMap<Class<?>, Set<Class<?>>>(initialMapping);
		} finally {
			readLock.unlock();
		}
	}
	
	@Deprecated
	Map<Class<?>, Set<Class<?>>> getTargetCache(){
		readLock.lock();
		try {
			return new HashMap<Class<?>, Set<Class<?>>>(targetCache);
		} finally {
			readLock.unlock();
		}
	}
	



	private boolean hasInTargetCache(Class<?> target) {
		return targetCache.containsKey(target);
	}
	
	private void putToTargetCache(Class<?> target) {
		
		Set<Class<?>> allHandlers = getAllHandlers(initialMapping, target);
		targetCache.put(target, allHandlers);
	}


	private Set<Class<?>> getAllHandlers(HashMap<Class<?>, Set<Class<?>>> map, Class<?> target) {
		
		HashSet<Class<?>> allHandlers = new HashSet<Class<?>>();
		
		LinkedList<Class<?>> queue = new LinkedList<Class<?>>();
		queue.addLast(Object.class);
		queue.addLast(target);
		
		while( ! queue.isEmpty()){
			Class<?> curTarget = queue.removeFirst();
			Set<Class<?>> curHandlersTypes = map.get(curTarget);
			if( ! Util.isEmpty(curHandlersTypes)){
				allHandlers.addAll(curHandlersTypes);
			}
			
			Class<?> superclass = curTarget.getSuperclass();
			if( ! Util.isEmpty(superclass)){
				queue.addLast(superclass);
			}
			
			Class<?>[] interfaces = curTarget.getInterfaces();
			if( ! Util.isEmpty(interfaces)){
				for(Class<?> interfaceType : interfaces){
					queue.addLast(interfaceType);
				}
			}
		}
		return allHandlers;
	}
	
	private Set<Class<?>> getFromTargetCache(Class<?> target) {
		Set<Class<?>> original = targetCache.get(target);
		if(original == null){
			original = new HashSet<Class<?>>();
		}
		HashSet<Class<?>> copy = new HashSet<Class<?>>(original);
		return copy;
	}


	private boolean putToMapping(HashMap<Class<?>, Set<Class<?>>> map, Class<?> target, Class<?> handler) {
		Set<Class<?>> set = map.get(target);
		if(set == null){
			set = new HashSet<Class<?>>();
			map.put(target, set);
		}
		if(set.contains(handler)){
			log.warn("mapping already contains "+handler+" for "+target);
			return false;
		} else {
			set.add(handler);
			return true;
		}
	}


	private void checkForManyHandlers(HashMap<Class<?>, Set<Class<?>>> map) {
		
		Set<Class<?>> targets = map.keySet();
		
		for(Class<?> target : targets){
			Set<Class<?>> all = getAllHandlers(map, target);
			if(all.size() > 1){
				throw new NotOneHandlerException(target, all);
			}
		}
	}
	



}
