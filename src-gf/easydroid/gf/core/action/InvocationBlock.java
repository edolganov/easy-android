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
package easydroid.gf.core.action;

import java.util.List;

import easydroid.gf.Action;
import easydroid.gf.core.action.filter.FiltersBlock;
import easydroid.gf.core.action.interceptor.InterceptorsBlock;
import easydroid.gf.core.action.trace.Body;
import easydroid.gf.core.action.trace.TraceWrapper;
import easydroid.gf.core.context.ContextRepository;
import easydroid.gf.exception.invoke.InvokeDepthMaxSizeException;
import easydroid.gf.key.InvokeDepthMaxSize;
import easydroid.gf.key.TraceHandlers;


public class InvocationBlock {
	
	private boolean isTraceHandlers;
	private int depthMaxSize;
	
	ActionServiceImpl actionService;
	Action<?,?> action;
	
	
	public InvocationBlock(ActionServiceImpl actionService, Action<?,?> action){
		this.actionService = actionService;
		this.action = action;
		
		isTraceHandlers = actionService.config.isTrueConfig(new TraceHandlers());
		depthMaxSize = actionService.config.getConfig(new InvokeDepthMaxSize());
	}
	
	public void invoke() throws Exception {
		
		final InvocationContext c = createContext(action, null, true);
		
		c.traceWrapper.wrapInvocationBlock(actionService.owner, action, new Body() {
			
			@Override
			public void invocation() throws Throwable {
				FiltersBlock block = new FiltersBlock(c);
				block.invoke();
			}
		});

		
	}
	
	
	<I, O> O subInvoke(InvocationContext parent, Action<I, O> action) throws Exception {
		
		final InvocationContext c = createContext(action, parent, false);
		
		c.traceWrapper.wrapSubHandlers(new Body() {
			
			@Override
			public void invocation() throws Throwable {
				InterceptorsBlock block = new InterceptorsBlock(c);
				block.invoke();
			}
		});

		
		return (O) action.getOutput();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private InvocationContext createContext(Action<?,?> action, InvocationContext parent, boolean initFilters){
		
		//check depth size
		int depth = parent == null? 1 : parent.depth+1;
		if(depth > depthMaxSize){
			throw new InvokeDepthMaxSizeException(depthMaxSize);
		}
		
		//create new context
		InvocationContext c = new InvocationContext();
		c.owner = this;
		c.parent = parent;
		c.depth = depth;
		c.actions = actionService;
		c.action = action;
		c.traceWrapper = new TraceWrapper(isTraceHandlers);
		c.config = c.actions.config;
		c.staticContextObjects = c.actions.staticContext.getStaticContextObjects();
		c.invocationContext = parent == null? new ContextRepository() : parent.invocationContext;
		
		if(initFilters){
			c.filters = c.actions.resourse.getFilters();
		}
		c.interceptors = (List)c.actions.resourse.getInterceptors(c.action);
		c.handler = c.actions.resourse.getHandler(c.action);
		c.initializers = c.actions.resourse.getInitializers();
		
		return c;
	}

}
