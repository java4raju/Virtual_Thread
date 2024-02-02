package com.java4raju.vt.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserRequests {
	
	public static void main(String[] args) {
		
		System.out.println("==========Main Thread starts==============");
		long startTime = System.currentTimeMillis();
		
		int CONCURRENT_REQUESTS = 2000; //In parallel
		
		var factory = Thread.ofVirtual().name("http-request-", 0).factory();
		
		try (ExecutorService service = Executors.newThreadPerTaskExecutor(factory)){
			
		IntStream.range(0, CONCURRENT_REQUESTS).forEach(request -> {
			service.submit(new IOCalls());
			});
		}		
		System.out.println("==============Main Thread ends=============");
		long endTime = System.currentTimeMillis();
		
		System.out.println("Total time Taken: "+(endTime-startTime)/1000);
		
	}

}
