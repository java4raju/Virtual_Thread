package com.java4raju.vps.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java4raju.vps.products.entity.Product;

@Repository
public interface AmazonProductRepository extends JpaRepository<Product, Integer>{

	Optional<Product>  findProductByProductName(String productName);

}
