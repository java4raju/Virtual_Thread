package com.java4raju.vt.sc;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import com.java4raju.vt.sc.LongRunningTask.TaskResponse;

public class Example_2_ShutdownOnFalure {
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Main Starts>>>>>>>");
		shutdownOnFailure();
		System.out.println("<<<<<<<<Main Ends");
		
	}
	
	public static void shutdownOnFailure() throws InterruptedException {
		
		try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
			
			Subtask<TaskResponse> s1 = scope.fork(new LongRunningTask("Weather1", 10, "35*", false));
			Subtask<TaskResponse> s2 = scope.fork(new LongRunningTask("Weather2", 3, "33*", true));
			Subtask<TaskResponse> s3 = scope.fork(new LongRunningTask("Weather3", 6, "33*", false));
			Subtask<TaskResponse> s4 = scope.fork(new LongRunningTask("Weather4", 7, "33*", false));
			
			
			scope.join();
			
			if (s1.state().equals(State.SUCCESS)) {
				System.out.println("Weather1:=========> "+s1.get());
			} else if (s1.state().equals(State.FAILED)) {
				System.out.println("Weather1:=========> "+s1.exception());
			}

			if (s2.state().equals(State.SUCCESS)) {
				System.out.println("Weather2:=========>"+s2.get());
			} else if (s2.state().equals(State.FAILED)) {
				System.out.println("Weather2:=========>"+s2.exception());
			}
			
		}
		
	}


}
