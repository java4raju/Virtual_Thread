package com.java4raju.vt;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlateformThreadPerformance {
	
	public static void main(String[] args) throws InterruptedException {
		
		int totalThread = 100000;
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("Start main thread ");
		//List<Thread> threads = IntStream.range(0, 10000000).mapToObj(i -> new Thread(() -> asyncTask(1))).collect(Collectors.toList());
		
		List<Thread> threads = IntStream.range(0, totalThread).mapToObj(i -> Thread.ofPlatform().unstarted(() -> asyncTask(10, i))).collect(Collectors.toList());
		
		
		threads.forEach(t -> t.start());
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time Taken to create thread: "+(endTime-startTime));
		System.out.println("End main thread ");
	}

	private static void asyncTask(int pauseInSeconds, int n) {
		//System.out.println("--Executig Task using thread --> " + Thread.currentThread());
		
		try {
			doubeTheNumber(n);
			Thread.sleep(pauseInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void doubeTheNumber(int n) {
		int s = 2*n;
		
		//System.out.println("Double of "+n+" is "+s);
		
	}
}
