package com.java4raju.continuation;

import java.time.Duration;

// Continuation related classes moved inside this
// internal package but we are making this visible by exposing
// the package --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
import jdk.internal.vm.*;

public class Example_2_ContinuationVisited {

	private static final ContinuationScope SCOPE1 = new ContinuationScope("scope1");

	public static void main(String[] args) throws Exception {

		System.out.println("main : enter");

		Continuation c = new Continuation(SCOPE1, new IOProcessor());
		while (!c.isDone()) {
			c.run();
			System.out.println("main : scope1 loop");
			Thread.sleep(Duration.ofSeconds(1));
		}
		System.out.println("main : exit");
	}
}

class IOProcessor implements Runnable {
	
	private static final ContinuationScope SCOPE2 = new ContinuationScope("scope2");
	
	@Override
	public void run() {
		execution_1();
	}
	
	private void execution_1() {
		System.out.println("execution_1 : enter");
		Continuation c = new Continuation(SCOPE2, this::execution_2);
		while (!c.isDone()) {
			c.run();
			System.out.println("execution_1 : scope2 loop");
		}
		System.out.println("execution_1 : exit");
	} 

	private void execution_2() {
		System.out.println("execution_2 : enter");

		int possition = 0;

		possition++;
		System.out.println("execution_2 : execute possition " + possition);
		Continuation.yield(SCOPE2);

		possition++;
		System.out.println("execution_2 : execute possition " + possition);
		Continuation.yield(SCOPE2);

		possition++;
		System.out.println("execution_2 : execute possition " + possition);
		Continuation.yield(SCOPE2);

		System.out.println("execution_2 : exit");
	} 

}
