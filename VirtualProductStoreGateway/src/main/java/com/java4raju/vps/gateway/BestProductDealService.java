package com.java4raju.vps.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


/**
 * Interact with two different services, Amazon Store and Flipkar store service
 */
@Service
public class BestProductDealService {

	  @Value("#{${product.store.baseurls}}")
	   private Map<String,String> storeUrlMap;
	  
	  RestClient restClient = RestClient.create();
	  
	  public List<Product> getProductFromAllStore(String productName) throws Exception {
		 
		  return processWithVTStructuredConcurrency(productName);
		  
		  /**
		   * Enable below for perfomaceTest with different approaches (TODO-manage ScopeValue with StructuredTaskScope)
		   */
		  //return processWithVT_CF(productName);
		  //return processWithVT_F(productName);
		  
		  //return processWithPT_CF(productName);
		  //return processWithPT_F(productName);
		  
		  //return processSequencially(productName);
		  
	  }

	 /**
	  * Description: call with Virtual Thread and Structured concurrency 
	  * @param productName
	  * @return List<Product>
	  */
	private List<Product> processWithVTStructuredConcurrency(String productName) {
		ThreadFactory factory = Thread.ofVirtual().name("p-s-t-", 0).factory();
	      
		//StructuredTaskScope: complete the task when at least one is successful or all 
		  try (var scope = new StructuredTaskScope<Product>("virtualstore", factory)) {
	            
	            List<Subtask<Product>> productTasks = new ArrayList<>();
	           
	            storeUrlMap.forEach( (name, url) -> {
	            	productTasks.add(scope.fork(() -> getProductFromStore(name,url,productName)));
	            });
	            
	            try {
					scope.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	            
	             
	            productTasks.stream()
	                .filter(t -> t.state() == State.FAILED) 
	                .map(Subtask::exception)
	                .forEach(e -> e.printStackTrace());
	            
	            return productTasks.stream()
	                .filter(t -> t.state() == State.SUCCESS)
	                .map(Subtask::get)
	                .toList();
	        }
	}

	/**
	 * Process request with Virtual Thread and Completable Future
	 * @param productName
	 * @return List<Product>
	 */
	private List<Product> processWithVT_CF(String productName) {

		List<CompletableFuture<Product>> pListFuture = new ArrayList<>();
		List<Product> pList = new ArrayList<>();

		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var fUrl= storeUrlMap.get("Flipkart");
			var aUrl = storeUrlMap.get("Amazon");			
			pList = CompletableFuture.supplyAsync(() -> getProductFromStore("Flipkart",fUrl,productName), executor)
							 .thenCombine(CompletableFuture.supplyAsync(() -> getProductFromStore("Flipkart",aUrl,productName), executor),
									 (p1, p2) -> {	
										 List<Product> pcList = new ArrayList<>();
										 pcList.add(p1);
										 pcList.add(p2);
										 return pcList;
									 }).join();
		    }
			
			/*storeUrlMap.forEach( (name, url) -> {
				pListFuture.add(CompletableFuture.supplyAsync(() -> getProductFromStore(name,url,productName), executor));
			
			});
			pList= pListFuture.stream().map(p -> {
				try {
					return p.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());*/
			 
		return pList;
	}
	
	/**
	 * Process request with Virtual Thread and Future
	 * @param productName
	 * @return List<Product>
	 */
	private List<Product> processWithVT_F(String productName) {
		
		List<Future<Product>> pListFuture = new ArrayList<>(); 
		List<Product> pList = new ArrayList<>(); 
		
		try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
			
			storeUrlMap.forEach( (name, url) -> {
				pListFuture.add(executor.submit(() -> getProductFromStore(name,url,productName)));
	            });
			
			pList = pListFuture.stream().map(p -> {
				try {
					return p.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
		}
		return pList;
	}
	
	
	/**
	 * Process request with Platform Thread without Completable Future
	 * @param productName
	 * @return List<Product>
	 */
	private List<Product> processWithPT_CF(String productName) {
		
		List<Future<Product>> pListFuture = new ArrayList<>(); 
		List<Product> pList = new ArrayList<>(); 
		List<Product> pcList = new ArrayList<>();
		
		try(ExecutorService executor = Executors.newFixedThreadPool(2)){
			
			var fUrl= storeUrlMap.get("Flipkart");
			var aUrl = storeUrlMap.get("Amazon");			
			pList = CompletableFuture.supplyAsync(() -> getProductFromStore("Flipkart",fUrl,productName), executor)
							 .thenCombine(CompletableFuture.supplyAsync(() -> getProductFromStore("Flipkart",aUrl,productName), executor),
									 (p1, p2) -> {	
										 pcList.add(p1);
										 pcList.add(p2);
										 return pcList;
									 }).join();
		}
		return pList;
	}
	
	
	/**
	 * Process request with Platform Thread and Future
	 * @param productName
	 * @return List<Product>
	 * @throws Exception 
	 */
	private List<Product> processWithPT_F(String productName) throws Exception {
		
		List<Future<Product>> pListFuture = new ArrayList<>(); 
		List<Product> pList = new ArrayList<>(); 
		
		//TODO -> ScopeValue to be used
		//ScopedValue<APIPerfornaceData> TIMEMAP = BestProductDealController.TIMEMAP;
		 
		
		try(ExecutorService executor = Executors.newFixedThreadPool(2)){

			storeUrlMap.forEach( (name, url) -> {
				pListFuture.add(executor.submit(() -> getProductFromStore(name,url,productName)));
				//TODO -> //pListFuture.add(executor.submit(() -> ScopedValue.callWhere(TIMEMAP, TIMEMAP.get(), () -> getProductFromStore(name,url,productName))));
	            });
			
			pList = pListFuture.stream().map(p -> {
				try {
					return p.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
		}
		return pList;
	}
	
	

	/**
	 * Process request with sequencial call
	 * @param productName
	 * @return List<Product>
	 */
	private List<Product> processSequencially(String productName) {

		final List<Product> pList = new ArrayList<>();
 
			storeUrlMap.forEach((name, url) -> {
				Product p = getProductFromStore(name, url, productName);
				pList.add(p);
			});
		 
		return pList;
	}	  
	  
		/**
		 * Desc: The task to fetch record from different service
		 * @param storeName
		 * @param url
		 * @param productName
		 * @return Product
		 */
	    private Product getProductFromStore(String storeName, String url, String productName) {
	        long start = System.currentTimeMillis();
	        Product product = restClient.get()
	                .uri(url + "/product", t -> t.queryParam("productName", productName).build())
	                .retrieve()
	                .body(Product.class);
	        
	        long end = System.currentTimeMillis();
	       
	      if(BestProductDealController.TIMEMAP.isBound())  {
	      APIPerfornaceData timeObj = BestProductDealController.TIMEMAP.get();
	      timeObj.addTiming(storeName, end - start);
	      }
	        return product;
	    }
	
}
