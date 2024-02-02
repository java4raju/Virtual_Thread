package com.java4raju.vt.sv;

public class Example_1_ThreadLocalVisited {
	
	final static ThreadLocal<User> user = new ThreadLocal<>();
	//final static ThreadLocal<User> user = ThreadLocal.withInitial(() -> new User("Unknown"));
	
	public static void main(String[] args) {
		
		System.out.println("=======Starts main thread======");
		
		System.out.println("User in main thread: "+user.get());	
		user.set(new User(Thread.currentThread().getName())); //Problem: mutability constraint, set can be called for any place
		System.out.println("User in main thread: "+user.get());
		authorizeUser();
		
		user.remove(); //make sure to remove the value
		System.out.println("======Ends main thread=======");
	}

	private static void authorizeUser() {
		authorize();
		authorizeWithVirtual();
	}

	
	private static void authorize() {
		System.out.println("User in calling method in main thread: "+user.get());
	}
	
	private static void authorizeWithVirtual(){
		Thread t = Thread.ofVirtual().start(() -> {
			System.out.println("User in spown thread: "+user.get());
		});
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


