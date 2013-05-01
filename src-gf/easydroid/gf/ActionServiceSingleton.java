package easydroid.gf;

import easydroid.core.Singleton;
import easydroid.core.annotation.PostConstruct;
import easydroid.core.exception.ExceptionWrapper;
import easydroid.gf.exception.invoke.InvocationException;
import easydroid.gf.service.ActionService;

public abstract class ActionServiceSingleton extends Singleton implements ActionService {
	
	protected ActionService actionService;
	
	@PostConstruct
	public void init(){
		actionService = createActionService();
	}
	
	protected abstract ActionService createActionService();

	@Override
	public <I, O> O invoke(Action<I, O> action) throws InvocationException, ExceptionWrapper, RuntimeException {
		return (O) actionService.invoke(action);
	}

	@Override
	public <I, O> O invokeUnwrap(Action<I, O> action) throws InvocationException, Exception {
		return (O) actionService.invokeUnwrap(action);
	}

}
