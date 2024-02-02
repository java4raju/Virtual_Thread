package com.java4raju.vt.sv;

public class Example_5_ScopeValueInChildThread {
	
	static ScopedValue<User> userScope = ScopedValue.newInstance();
		
	public static void main(String[] args) {
		
		System.out.println("userScope is bound: "+userScope.isBound());	
		//userScope.get(); //will result in exception
		
		User user = new User("Main Value");
		
		ScopedValue.runWhere(userScope, user, Example_5_ScopeValueInChildThread::authorizeUser);
		
		System.out.println("userScope is bound: "+userScope.isBound());		
	}

	private static void authorizeUser() {
		authorize();
		 authorizeWithVirtual();
	}
	
	private static void authorizeWithVirtual(){
		Thread t = Thread.ofVirtual().start(() -> {
			System.out.println("authorizeWithVirtual ScopeValue is bound: "+Example_5_ScopeValueInChildThread.userScope.isBound());	
			if(Example_5_ScopeValueInChildThread.userScope.isBound())
			System.out.println("authorizeWithVirtual authorize User: "+Example_5_ScopeValueInChildThread.userScope.get());
		});
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	private static void authorize() {
	System.out.println("authorize ScopeValue is bound: "+Example_5_ScopeValueInChildThread.userScope.isBound());	
		
	System.out.println("authorize User: "+Example_5_ScopeValueInChildThread.userScope.get());
	}
}

