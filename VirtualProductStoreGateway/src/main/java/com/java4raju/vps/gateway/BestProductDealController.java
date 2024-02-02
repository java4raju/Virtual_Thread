package com.java4raju.vps.gateway;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BestProductDealController {

	public static final ScopedValue<APIPerfornaceData> TIMEMAP = ScopedValue.newInstance();
	
	@Autowired
	BestProductDealService service;

	@GetMapping("/product")
	public BestDealProduct getBestDeal(@RequestParam String productName) {
		System.out.println("Gateway Thread: "+Thread.currentThread());

		long start = System.currentTimeMillis();
		APIPerfornaceData timeObj = new APIPerfornaceData();
		try {
			List<Product> products = ScopedValue.callWhere(TIMEMAP, timeObj,
					() -> service.getProductFromAllStore(productName));

			return new BestDealProduct(timeObj,
					products.stream().min(Comparator.comparing(Product::price)).orElseThrow(), products);

		} catch (Exception e) {
			throw new RuntimeException("Exception while calling getProductFromAllStore", e);
		} finally {
			long end = System.currentTimeMillis();
			timeObj.addTiming("VirtualProductStoreGateway:", end - start);
		}
	}
}
