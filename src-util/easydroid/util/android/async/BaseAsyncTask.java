package easydroid.util.android.async;

import java.util.HashMap;

import easydroid.util.Util;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<T> extends AsyncTask<Object, Object, T>{
	
	private static Logger log = LogFactory.getLog(BaseAsyncTask.class); 
	
	//don't use directly access
	private static HashMap<String, String> ownerData = new HashMap<String, String>();
	
	private static synchronized void putOwnerData(String ownerId, String reqId){
		ownerData.put(ownerId, reqId);
	}
	
	private static synchronized boolean checkOwnerReqForOverdue(String ownerId, String oldReqId){
		
		//check
		Object curReqId = ownerData.get(ownerId);
		boolean isOverdue = ! oldReqId.equals(curReqId);
		
		//clear data if need
		if(!isOverdue){
			ownerData.remove(ownerId);
		}
		
		return isOverdue;
	}
	
	
	public static synchronized void resetRequest(Object requestOwner){
		
		if(requestOwner == null){
			return;
		}
		
		String ownerId = getOwnerId(requestOwner);
		ownerData.remove(ownerId);
	}
	
	private static String getOwnerId(Object requestOwner){
		return Util.toObjectString(requestOwner);
	}
	
	
	
	Throwable t;
	
	boolean isOverdue;
	boolean skipOverdue;
	String ownerId;
	String reqId;
	BaseAsyncTaskContext taskContext;
	
	
	
	public BaseAsyncTask() {
		super();
	}
	
	public BaseAsyncTask(Object requestOwner) {
		this(requestOwner, true);
	}

	public BaseAsyncTask(Object requestOwner, boolean skipOverdue) {
		super();
		
		Util.checkArgumentForEmpty(requestOwner, "requestOwner is null");
		this.ownerId = getOwnerId(requestOwner);
		this.reqId =  ""+System.currentTimeMillis()+"-"+System.nanoTime();
		putOwnerData(ownerId, reqId);
		
		this.skipOverdue = skipOverdue;
		
	}

	public void setTaskContext(BaseAsyncTaskContext taskContext) {
		this.taskContext = taskContext;
	}

	@Override
	protected T doInBackground(Object... params) {
		
		try {
			return (T)doInBackground();
		}catch (Throwable t) {
			this.t = t;
			return null;
		}
	}
	
	protected abstract T doInBackground() throws Throwable;
	
	
	@Override
	protected void onPostExecute(T result) {
		
		checkOverdueReq();
		if( needSkipRequest()){
			log.debug("skip invalid request for "+ownerId);
			return;
		}
		
		beforeResult();
		if(t == null){
			onSuccess(result);
		} else {
			onFail(t);
		}
	}
	
	private boolean needSkipRequest(){
		return (isOverdue && skipOverdue) 
				|| (taskContext != null? ! taskContext.isValidTaskContext() : false);
	}
	
	private void checkOverdueReq() {
		if(ownerId == null){
			return;
		}
		isOverdue = checkOwnerReqForOverdue(ownerId, reqId);
	}


	protected void beforeResult(){
		//override if need
	}
	
	protected void onSuccess(T result){
		//override if need
	}
	
	protected void onFail(Throwable t){ 
		//override if need
		LogFactory.getLog(getClass()).error("error while invoke async operation", t);
	}

}
