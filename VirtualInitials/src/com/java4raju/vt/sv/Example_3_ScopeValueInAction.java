package com.java4raju.vt.sv;

public class Example_3_ScopeValueInAction {
	
	static ScopedValue<User> userScope = ScopedValue.newInstance();
		
	public static void main(String[] args) {
		
		System.out.println("userScope is bound: "+userScope.isBound());	
		//userScope.get(); //will result in exception as it is not bound to method yet
		
		User user = new User("Main Value");
		
		ScopedValue.runWhere(userScope, user, Example_3_ScopeValueInAction::authorizeUser);
		
		System.out.println("userScope is bound: "+userScope.isBound());		
	}

	private static void authorizeUser() {
		authorize();
	}

	
	private static void authorize() {
	System.out.println("ScopeValue is bound: "+Example_3_ScopeValueInAction.userScope.isBound());	
		
	System.out.println("authorize User: "+Example_3_ScopeValueInAction.userScope.get());
	}
}

