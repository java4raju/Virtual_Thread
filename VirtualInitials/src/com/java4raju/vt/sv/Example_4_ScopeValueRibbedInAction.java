package com.java4raju.vt.sv;

/**
 * Change within dynamic scope
 */
public class Example_4_ScopeValueRibbedInAction {
	static ScopedValue<User> userScope = ScopedValue.newInstance();
		
	public static void main(String[] args) {
		
		System.out.println("userScope is bound: "+userScope.isBound());	
		//userScope.get(); //will result in exception
		
		User user = new User("Main Value");
		
		ScopedValue.runWhere(userScope, user, Example_4_ScopeValueRibbedInAction::authorizeUser);
		
		System.out.println("userScope is bound: "+userScope.isBound());		
	}

	private static void authorizeUser() {
		User user = new User("Child Value");
		ScopedValue.runWhere(userScope, user, Example_4_ScopeValueRibbedInAction::authorize);
		System.out.println("authorizeUser User: "+Example_4_ScopeValueRibbedInAction.userScope.get());
		System.out.println("authorizeUser userScope is bound: "+userScope.isBound());
	}

	
	private static void authorize() {
	System.out.println("authorize ScopeValue is bound: "+Example_4_ScopeValueRibbedInAction.userScope.isBound());			
	System.out.println("authorize User: "+Example_4_ScopeValueRibbedInAction.userScope.get());
	}

}

