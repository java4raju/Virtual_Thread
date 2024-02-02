package com.java4raju.vps.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.java4raju.vps.products.entity.Product;
import com.java4raju.vps.products.record.ProductDTO;
import com.java4raju.vps.products.repositories.FlipkartProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Component
@Profile("Flipkart")
@Log4j2
public class FlipkartProductService implements ProductService {

	@Value("${product.store.name}")
	String storeName;
	
	@Value("${product.store.temporary}")
	boolean temporary;
	
	@Autowired
	FlipkartProductRepository repository;
	
	List<ProductDTO> pList = new ArrayList<>();

	@PostConstruct
	void init() {
		log.info("Initialize Flipkart data storage");
		pList.add(new ProductDTO(storeName, "TV","CurveTv", "LG", 11000));
		pList.add(new ProductDTO(storeName, "Frige", "Fridge", "Samsung", 21000));
		pList.add(new ProductDTO(storeName, "Mobile", "iPhone15", "Apple", 102000));
		pList.add(new ProductDTO(storeName, "Book", "QuantumPhysics","AB Publication", 500));
		
		if(!temporary) {
			persist(pList);
		}
	}

	@Override
	public ProductDTO findProduct(String name) {
		if(temporary) {
		return pList.stream().filter(n -> n.productName().equalsIgnoreCase(name))
				.findFirst().orElseThrow(() ->  new NoSuchElementException("No Such Product Found"));
	}else {
		Product product = repository.findProductByProductName(name).orElseThrow(() -> new NoSuchElementException("No Such Product Found"));
		return new ProductDTO(storeName, product.getProductCategory(), product.getProductName(), product.getCompany(), product.getPrice());
		}
	}
	
	private void persist(List<ProductDTO> pdList) {

		if (repository.findAll().isEmpty()) {

			repository.saveAll(pdList.stream().map(p -> {
				Product product = new Product();
				BeanUtils.copyProperties(p, product);
				return product;
			}).collect(Collectors.toList()));

			repository.saveAll(IntStream.range(1, 100).mapToObj(i -> {
				ProductDTO productDTO = new ProductDTO(storeName, "Category-" + i, "Product-" + i,"Company-" + i, ThreadLocalRandom.current().nextInt(5000, 10000));
				Product product = new Product();
				BeanUtils.copyProperties(productDTO, product);
				return product;
			}).collect(Collectors.toList()));
		}
	}

	@Override
	public List<ProductDTO> allProduct() {
		return repository.findAll().stream().map(p -> {
			return new ProductDTO(storeName, p.getProductCategory(), p.getProductName(), p.getCompany(), p.getPrice());
		}).collect(Collectors.toList());
	}

}
