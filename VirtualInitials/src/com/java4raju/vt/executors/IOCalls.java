package com.java4raju.vt.executors;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class IOCalls implements Callable<String> {

	@Override
	public String call() {
		
		String result = null;
		try {
			result = parallelCallFunctionalCF();
			//result = sequencialCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	
  private String parallelCallFunctionalCF() throws InterruptedException, ExecutionException {
		
	  try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
			
			String output = CompletableFuture
								.supplyAsync(this::makeDBCall, service)
								.thenCombine(CompletableFuture.supplyAsync(this::makeRestCall, service)
										, (r1, r2) -> {
											return "[" + r1 + "," + r2 + "]";
										}).join();
								
			/*.thenApply(result -> {
									
									// both dbCall and restCall have completed 
									String r = externalCall();
									return "[" + result + "," + r + "]";
									
								}).join();
								*/
			
			//System.out.println(output);
			return output;
		}
	}

  
	private String parallelCallFunctional() throws InterruptedException, ExecutionException {
		
		try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
		 List<Future<String>> results =	executor.invokeAll(Arrays.asList(() -> makeDBCall(),() ->  makeRestCall()));
		return  results.stream().map(r -> {
			try {
				return r.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.joining(", "));
		}
	}

	
	private String parallelCall() throws InterruptedException, ExecutionException {
		
		try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
			
			Future<String> dbcall = executor.submit(() -> makeDBCall());
			Future<String> restcall = executor.submit(() -> makeRestCall());
			
			return dbcall.get()+ restcall.get();
			
		}
	}

	private String sequencialCall() {
		
		String restCall = makeRestCall();
		String dbCall = makeDBCall();
		
		var result = dbCall+restCall;
		return result;
	}

	private String makeRestCall() {
		String response=null;
		try {
			response= new RestServiceCall("RestCall::").request(5);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private String makeDBCall() {
		String response=null;
		try {
			response= new RestServiceCall("DBCall::").request(2);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private String externalCall() {
		String response=null;
		try {
			response= new RestServiceCall("DBCall::").request(1);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
