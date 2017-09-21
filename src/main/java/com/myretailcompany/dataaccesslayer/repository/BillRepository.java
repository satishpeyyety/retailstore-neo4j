package com.myretailcompany.dataaccesslayer.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import com.myretailcompany.dataaccesslayer.entity.Bill;

public interface BillRepository extends GraphRepository<Bill> {

	// There is no Cascade delete option in API currently.
	@Query("MATCH (b:BILL)-[r:CONTAINS]-(l:LINEITEM)-[r1:USES]-(p:PRODUCT) WHERE ID(b) = {prop} delete l,r,b,r1")
	void deleteBillAndLineItems(@Param("prop") Long ID);
	
	@Query("match (b:BILL)-[r]-(a) delete a,r,b")
	void deleteAllBills();
	
	@Query(" match (n)-[r]-(m) delete n,r,m ")
	void deleteRelationalData();
	
	@Query(" match (n)delete n ")
	void deleteOrphanNodes();
	

	

}
