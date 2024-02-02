package com.java4raju.continuation;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class Example_1_ContinuationVisited {
	
	private static final ContinuationScope SCOPE1 = new ContinuationScope("scope1");

	public static void main(String[] args) {
		
		System.out.println("Starting main");
            
		Continuation continuation = new Continuation(SCOPE1, ()->{
			
			System.out.println("Execution line 1");
			Continuation.yield(SCOPE1);
			
			System.out.println("Execution line 2");
			Continuation.yield(SCOPE1);
			
			System.out.println("Execution line 3");
		});
		
		continuation.run();
		
		System.out.println("Execution Line Main 1");
		
		continuation.run();
		
		System.out.println("Execution Line Main 2");
		
		//continuation.run();
		
		System.out.println("Is Execution done: "+continuation.isDone());
		
	}
	
}