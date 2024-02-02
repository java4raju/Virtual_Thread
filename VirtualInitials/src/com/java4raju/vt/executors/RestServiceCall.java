package com.java4raju.vt.executors;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class RestServiceCall {
	
	String callType;
	
	public RestServiceCall(String callType) {
		this.callType = callType;
		
	}
	
	public String request(int delayinSec) throws URISyntaxException, MalformedURLException, IOException {
	
		System.out.println(callType+"Starts: "+Thread.currentThread());
		
		URI uri = new URI("https://httpbin.org/delay/"+delayinSec);		
		InputStream stream = uri.toURL().openStream();
		
		System.out.println(callType+"Ends: "+Thread.currentThread());
		
		return new String(stream.readAllBytes());
		
	}

}
