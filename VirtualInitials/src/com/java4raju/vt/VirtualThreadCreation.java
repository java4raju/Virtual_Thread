package com.java4raju.vt;

import java.lang.Thread.Builder.OfVirtual;
import java.util.concurrent.ThreadFactory;

public class VirtualThreadCreation {
	
	public static void main(String[] args) throws InterruptedException {
		
		//Using static method
		Thread.startVirtualThread(() -> {
			System.out.println("VT created name : "+Thread.currentThread());
		}).join();
		
	 			
		Thread pt = Thread.ofPlatform().unstarted(() -> {}); //Plateform thread
		
		Thread vt = Thread.ofVirtual().start(() -> {});  //Vrtual thread
		
		//Create using builder
		/*
		OfVirtual virtual = Thread.ofVirtual().name("vt1");		
		Thread t1 = virtual.unstarted(() -> {
			System.out.println("Vt name: "+Thread.currentThread().getName());
		});		
		Thread t2 = virtual.unstarted(() -> {
			System.out.println("Vt name: "+Thread.currentThread().getName());
		});		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		*/
		
		//Thread factory - thread safe
		ThreadFactory factory = Thread.ofVirtual().name("vt2").factory();
		
		Thread t1 = factory.newThread(() -> {
			System.out.println("Thread executing :"+Thread.currentThread());
		});
		
		Thread t2 = factory.newThread(() -> {
			System.out.println("Thread executing :"+Thread.currentThread());
		});
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		
		
	}

}
