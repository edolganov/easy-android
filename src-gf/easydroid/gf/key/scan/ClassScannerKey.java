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
package easydroid.gf.key.scan;

import easydroid.gf.config.ConfigKey;
import easydroid.gf.extra.scan.ClassScanner;

/**
 * Config key for {@link ClassScanner} implementation.
 *
 * @author Evgeny Dolganov
 * @see ClassScanner
 */
public class ClassScannerKey extends ConfigKey<Class<?>>{

	@Override
	public boolean hasDefaultValue() {
		return true;
	}
	
	@Override
	public Class<?> getDefaultValue() throws Exception {
		
		Class<?> scannerClass = null;
		
		//DefaultClassScanner
		if(scannerClass == null){
			try {
				scannerClass = Class.forName("framework.gf.android.AndroidClassScanner");
			}catch (Exception e) {
				throw new IllegalStateException("can't find any default implementation of "+ClassScanner.class, e);
			}
		}
		
		return scannerClass;
		
	}

}
