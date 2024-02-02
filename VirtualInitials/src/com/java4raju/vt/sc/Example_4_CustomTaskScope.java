package com.java4raju.vt.sc;

import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import com.java4raju.vt.sc.LongRunningTask.TaskResponse;

public class Example_4_CustomTaskScope {

	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Main Starts>>>>>>>");
		averageWeather();
		System.out.println("<<<<<<<<Main Ends");
		
	}
	
	public static void averageWeather() throws InterruptedException {
		
		try(var scope = new CustomTaskScope()){
			
		scope.fork(new LongRunningTask("Weather1", 8, "35", false));
		scope.fork(new LongRunningTask("Weather2", 6, "34", false));
		scope.fork(new LongRunningTask("Weather3", 7, "36", false));
		scope.fork(new LongRunningTask("Weather4", 3, "35", false));
		scope.fork(new LongRunningTask("Weather3", 2, "36", true));
		scope.fork(new LongRunningTask("Weather3", 1, "32", false));
			
			
			scope.join();
			
			System.out.println(scope.response());
			
		}
		
	}




}
