package com.java4raju.vps.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java4raju.vps.products.record.ProductDTO;
import com.java4raju.vps.products.service.ProductService;

@RestController
public class ProductStoreController {
	
	@Autowired
	ProductService productStore;
	
	@GetMapping("/product")
	public ProductDTO findProduct(@RequestParam String productName){
		System.out.println("ProductStore Thread: "+Thread.currentThread());
		blockingOperation();
		return productStore.findProduct(productName);
	}
	
	@GetMapping("/product/all")
	public List<ProductDTO> findAllProduct(){
		System.out.println("ProductStore Thread: "+Thread.currentThread());
		return productStore.allProduct();
	}
	
	
	//Processing computation time
	private void blockingOperation() {
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
