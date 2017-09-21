package com.myretailcompany;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.myretailcompany.dataaccesslayer.entity.Bill;
import com.myretailcompany.dataaccesslayer.repository.BillRepository;
import com.myretailcompany.rest.controller.bill.beans.BillUpdateInfo;
import com.myretailcompany.rest.controller.bill.beans.ProductInfoForBill;
import com.myretailcompany.rest.controller.product.beans.ProductInfo;
import com.myretailcompany.service.BillService;
import com.myretailcompany.service.ProductService;
import com.myretailcompany.util.BillStatus;
import com.myretailcompany.util.ProductCategory;

@Component
public class SampleDataSetupRunner implements CommandLineRunner {

	@Autowired
	private BillService billService;
	
	@Autowired
	private BillRepository billRepo;

	
	final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ProductService productService;

	
	
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("Inside Runner..");
		
		//clearDatabase();   TODO: Investigate reason for failure.
		
		//temperory hack to clean database..To solve clearDatabase() problem
		billRepo.deleteRelationalData();
		billRepo.deleteOrphanNodes();
		
		
		setUpProductData();
		setupBillData();
		logger.info("Exiting Runner.. ");
	}

	public void setupBillData() {
		
				// create a new Bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billUpdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0001", 1));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0002", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0003", 3));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0004", 4));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0005", 5));
		billUpdateInfo.setProductsToBeAdded(productsToBeAdded);
		billUpdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billUpdateInfo.setStatus(BillStatus.RELEASED);

		logger.info("billUpdateInfo = " + billUpdateInfo);
		billService.updateBill(billUpdateInfo, billId);
		Bill retrieveUpdatedbill = billService.getBillById(o1.getId());
		logger.info("retrieveUpdatedbill = " + retrieveUpdatedbill.getNoOfItems() + "  value ="
				+ retrieveUpdatedbill.getTotalValue());

	}

	private void setUpProductData() {
		productService.createProduct(new ProductInfo("ABC-abc-0001", 20.0, "Tomato", ProductCategory.A));
		productService.createProduct(new ProductInfo("ABC-abc-0002", 30.0, "Onion", ProductCategory.B));
		productService.createProduct(new ProductInfo("ABC-abc-0003", 40.0, "Potato", ProductCategory.C));
		productService.createProduct(new ProductInfo("ABC-abc-0004", 50.0, "Bread", ProductCategory.A));
		productService.createProduct(new ProductInfo("ABC-abc-0005", 60.0, "Apples", ProductCategory.B));
		productService.createProduct(new ProductInfo("ABC-abc-0006", 70.0, "Banana", ProductCategory.C));
		productService.createProduct(new ProductInfo("ABC-abc-0007", 80.0, "Strawberry", ProductCategory.A));
		productService.createProduct(new ProductInfo("ABC-abc-0008", 90.0, "Apricot", ProductCategory.B));
		productService.createProduct(new ProductInfo("ABC-abc-0009", 100.0, "Raisins", ProductCategory.C));
		productService.createProduct(new ProductInfo("ABC-abc-0010", 110.0, "CashewNut", ProductCategory.A));
	}
}
