package com.java4raju.vt.sc;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeoutException;

import com.java4raju.vt.sc.LongRunningTask.TaskResponse;

// when the scope exits it make sure that all the threads terminated success/fail or intrupted
public class Exampe_1_StructuredTaskScope {

	public static void main(String[] args) throws InterruptedException, TimeoutException {
		System.out.println("Main Starts>>>>");
		
		//interruptMainThread(); //enable, it sends signal to main to intrupt, 
								//main sends signal to child to be intrupted
		
		completeAllTask();
		 
		 System.out.println("<<<<Main Ends");
	}

	private static void interruptMainThread() {		
		Thread mainThread = Thread.currentThread();		
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(Duration.ofSeconds(1));
				mainThread.interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	// Complete all task even if any fails
	private static void completeAllTask() throws InterruptedException, TimeoutException {

		try (var scope = new StructuredTaskScope<>()) {

			// Start running tasks in parallel
			Subtask<TaskResponse> s1 = scope.fork(new LongRunningTask("AirIndia", 4, "Rs24000", true));
			Subtask<TaskResponse> s2 = scope.fork(new LongRunningTask("Vistaar", 8, "Rs23000", false));

			/*if(true) {
				throw new RuntimeException("Exception just after substask starts");
			}*/
			
			// Wait for ==> all task to complete success or failure
			//as part of default implementation
			scope.join();
			//scope.joinUntil(Instant.now().plus(Duration.ofSeconds(2)));

			// Handle child task results

			if (s1.state().equals(State.SUCCESS)) {
				System.out.println("AirIndia:=========> "+s1.get());
			} else if (s1.state().equals(State.FAILED)) {
				System.out.println("AirIndia:=========> "+s1.exception());
			}

			if (s2.state().equals(State.SUCCESS)) {
				System.out.println("Vistaara:=========>"+s2.get());
			} else if (s2.state().equals(State.FAILED)) {
				System.out.println("Vistaara:=========>"+s2.exception());
			}
		}

	}

}
