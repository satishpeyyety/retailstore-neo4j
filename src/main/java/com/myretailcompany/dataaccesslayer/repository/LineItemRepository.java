package com.myretailcompany.dataaccesslayer.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import com.myretailcompany.dataaccesslayer.entity.LineItem;

public interface LineItemRepository extends GraphRepository<LineItem> {
	
	
	@Query("match (l:LINEITEM)-[r:USES]-(p:PRODUCT) where ID(p)={prodId} return l")
	public List<LineItem> findByProduct_id(@Param("prodId") long prodId);

}
