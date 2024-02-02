package com.java4raju.vt.sv;

public class Example_2_InheritableThreadLocalVisited {

	/**
	 * If do not want child value to propagate to parent either create immutable user or override as below
	 */
	
	//final static InheritableThreadLocal<User> user = new InheritableThreadLocal<>();
	
	final static InheritableThreadLocal<User> user = new InheritableThreadLocal<>() {
		
		@Override
		protected User childValue(User parentValue) {
			return super.childValue(new User(parentValue.getName())); // make immutable
		}
	};
	
	public static void main(String[] args) {
		System.out.println("=======Starts main thread======");
		System.out.println("User: "+user.get());		
		
		user.set(new User(Thread.currentThread().getName()));
		
		System.out.println("User: "+user.get());
		
		authorizeUser();
		
		System.out.println("User: "+user.get());
		System.out.println("=======Ends main thread======");
	}

	private static void authorizeUser() {
		authorize();
		authorizeWithVirtual();
		
	}

	
	private static void authorize() {
		System.out.println("authorize User: "+user.get());
	}
	
	private static void authorizeWithVirtual(){
		
		Thread t = Thread.ofVirtual().start(() -> {
			
		System.out.println("authorizeWithVirtual User: "+user.get());
			
		 user.get().setName("Virtual");
		 
		 System.out.println("authorizeWithVirtual set to: "+user.get());
			
		});
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


