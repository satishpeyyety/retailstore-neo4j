package com.myretailcompany.dataaccesslayer.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.myretailcompany.dataaccesslayer.entity.Product;

public interface ProductRepository extends GraphRepository<Product> {

	public long count();

	public List<Product> findByBarCodeId(String barCodeId);
	
	
	
	
	@Query("MATCH (b:PRODUCT)delete b")
	void deleteAllProducts();

}
