package com.java4raju.vps.products.service;

import java.util.List;

import com.java4raju.vps.products.record.ProductDTO;

public interface ProductService {
	
	public ProductDTO findProduct(String name);
	public List<ProductDTO> allProduct();

}
