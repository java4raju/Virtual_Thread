package com.java4raju.vt.sc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import com.java4raju.vt.sc.LongRunningTask.TaskResponse;


/**
 * Custom Task Scope
 */
public class CustomTaskScope extends StructuredTaskScope<TaskResponse>{
	
  List<Subtask<? extends TaskResponse>>  successSubtasks = new CopyOnWriteArrayList<>();
  
  /**
   * Custom Logic for the completion of work
   */
  	@Override
	protected void handleComplete(Subtask<? extends TaskResponse> subtask) {
		if(subtask.state().equals(State.SUCCESS)) {
			add(subtask);
		}
	}

	private void add(Subtask<? extends TaskResponse> subtask) {
	    int successCount = 0;
	    
	    synchronized (subtask) {
	    	successSubtasks.add(subtask);
	    	successCount = successSubtasks.size();
		}
	    
	    if(successCount==2) {
	    	this.shutdown();
	    }
		
	}
		
	@Override
	public CustomTaskScope join() throws InterruptedException {
	super.join();	
		return this;
		
	}
	
	//Prepare Response
	public TaskResponse response() {
		super.ensureOwnerAndJoined();
		if(successSubtasks.size()!=2) {
			throw new RuntimeException("Atleast 2 task must complete");
		}
		
		TaskResponse tr1 = successSubtasks.get(0).get();
		TaskResponse tr2 = successSubtasks.get(1).get();
		
		int average = (Integer.valueOf(tr1.response())+Integer.valueOf(tr2.response()))/2;
		
		return new TaskResponse("Avarage weather: ", average+"", tr1.timeTaken()+tr2.timeTaken());
				
	}

}
