package com.java4raju.vt;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlateformVirtualLink {
	
	public static void main(String[] args) throws InterruptedException {
		
		
		IntStream.range(0, 10).mapToObj(i -> Thread.startVirtualThread(() -> {
			
			System.out.println("Thread: "+Thread.currentThread());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread: "+Thread.currentThread());
			
			
		})).collect(Collectors.toList()).stream().forEach(i -> {
			try {
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		/*
		
		Thread t = new Thread(() -> {
			
			System.out.println("Thread: "+Thread.currentThread());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread: "+Thread.currentThread());
			
			
		});
		
		t.start();
		*/
		
		
		
	}

}
