package com.java4raju.vt.sv;

import java.util.concurrent.StructuredTaskScope;

public class Example_6_ScopeValueStructuredTakScope {
	
	static ScopedValue<User> userScope = ScopedValue.newInstance();
	
	public static void main(String[] args) {
		
		System.out.println("Main is bound: "+userScope.isBound());
		
		User user = new User("Main User");
		ScopedValue.runWhere(userScope, user, Example_6_ScopeValueStructuredTakScope::structureScope);
	}
	
	
     private static void structureScope() {
    	 
    	 try(var scope = new StructuredTaskScope<User>()){
    		 
    		 scope.fork(() -> {
    			 
    			 System.out.println("Child is bound: "+userScope.isBound());
    			 
    			 if(userScope.isBound())
    				 System.out.println("authorizeUser User: "+userScope.get());
    			 
    			 return null;
    		 });
    		 
    		 try {
				scope.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		 
    	 }
     }

}
