package com.java4raju.vt;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VirtualThreadPerformance {
	

	static Set<String> pThreads = ConcurrentHashMap.newKeySet();
	
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Start main thread ");
		
		int totalThread = 100000;
		
		long startTime = System.currentTimeMillis();
		
		List<Thread> threads = IntStream.range(0, totalThread).mapToObj(i -> Thread.ofVirtual().unstarted(() -> asyncTask(2, i))).collect(Collectors.toList());
		
		
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
		
		System.out.println("Total Plateform thread: "+pThreads.size());
		
		System.out.println("End main thread ");
	}

	private static void asyncTask(int pauseInSeconds, int n) {
		//System.out.println("--Executig Task using thread --> " + Thread.currentThread().toString().substring(20));
		pThreads.add(Thread.currentThread().toString().substring(20));
		try {
			doubeTheNumber(n);
			Thread.sleep(pauseInSeconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doubeTheNumber(int n) {
		int s = 2*n;
		
		//System.out.println("Double of "+n+" is "+s);
		
	}


}
