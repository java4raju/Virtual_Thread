package com.java4raju.vps.gateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class APIPerfornaceData {
	
    private final Map<String, Long> timeMap = Collections.synchronizedMap(new HashMap<String, Long>());
   
    void addTiming(String storeName, long time) {
    	timeMap.put(storeName, time);
    }
  
    
    public Map<String, Long> getTimeMap() {
    	return timeMap;
    }
        
}
