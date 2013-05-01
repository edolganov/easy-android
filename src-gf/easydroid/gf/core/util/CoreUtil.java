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
package easydroid.gf.core.util;

import easydroid.core.exception.BaseException;
import easydroid.core.exception.ExceptionWrapper;
import easydroid.core.exception.InvalidStateException;


public class CoreUtil {
	
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<?> type) throws RuntimeException {
		try {
			return (T)type.newInstance();
		} catch (Exception e) {
			throw new InvalidStateException("cannot create instance of "+type, e);
		}
	}
	
	public static RuntimeException convertException(Exception e, String info){
		if(e instanceof BaseException){
			return (BaseException)e;
		}
		else if(e instanceof RuntimeException){
			return (RuntimeException)e;
		}
		else {
			return new ExceptionWrapper(info, e);
		}
	}

}
