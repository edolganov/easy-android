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
package easydroid.gf.service;

import java.util.Collection;


import easydroid.core.annotation.Inject;
import easydroid.gf.Handler;

/**
 * Interface for editing handler's context.
 * Main implementaion is {@link easydroid.gf.core.Engine}.
 *
 * @author Evgeny Dolganov
 * @see Handler
 * @see Inject
 * @see easydroid.gf.core.Engine
 */
public interface ContextService {
	
	/**
	 * Add some object to context.
	 * So this object can be injected into filters, interceptors, handlers.
	 * @see Inject
	 */
	void addToContext(Object ob);
	
	/**
	 * Analog of multi call of {@link #addToContext(Object)}.
	 * For example for JavaBean logic.
	 */
	void setContextObjects(Collection<Object> objects);

}
