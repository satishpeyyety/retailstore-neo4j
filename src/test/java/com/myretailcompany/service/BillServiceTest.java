package com.myretailcompany.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretailcompany.dataaccesslayer.entity.Bill;
import com.myretailcompany.rest.controller.CustomException;
import com.myretailcompany.rest.controller.bill.beans.BillUpdateInfo;
import com.myretailcompany.rest.controller.bill.beans.ProductInfoForBill;
import com.myretailcompany.util.BillStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BillServiceTest {

	@Autowired
	private BillService billService;

	@Test(expected = CustomException.class)
	public void testBillNotExist() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeRemoved.add(new ProductInfoForBill("DDD-abc-0003", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, (long) 9999);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue() + "   list of items = " + retrieveUpdatedBill.getLineItems());

	}

	@Test
	public void testBillUpdateAddAndRemoveProducts() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0001", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0002", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0003", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0004", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0005", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);

		//productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-0005", 2));   TODO: Defect, not working as expected.
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue() + "   list of items = " + retrieveUpdatedBill.getLineItems());

		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(4); // 5
																		// products
																		// add
		assertThat(retrieveUpdatedBill.getTotalValue())
				.isEqualTo(20 * 2 * 1.1 + 30 * 2 * 1.2 + 40 * 2 * 1 + 50 * 2 * 1.1);
	}

	@Test
	public void testBillUpdateAddMultipleProducts() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0001", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0002", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0003", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0004", 2));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0005", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue());
		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(5); // 5
																		// products
																		// add
		assertThat(retrieveUpdatedBill.getTotalValue())
				.isEqualTo(20 * 2 * 1.1 + 30 * 2 * 1.2 + 40 * 2 * 1 + 50 * 2 * 1.1 + 60 * 2 * 1.2);
	}

	@Test(expected = CustomException.class)
	public void testBillUpdateAddNonExistingProduct() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("DDD-abc-0003", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue() + "   list of items = " + retrieveUpdatedBill.getLineItems());
		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedBill.getTotalValue()).isEqualTo(40 * 2 * 1);
	}

	@Test
	public void testBillUpdateAddSingleProductCatA() {
		// create a new Bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0001", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue());
		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedBill.getTotalValue()).isEqualTo(20 * 2 * 1.1);
	}

	@Test
	public void testBillUpdateAddSingleProductCatB() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0002", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue());
		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedBill.getTotalValue()).isEqualTo(30 * 2 * 1.2);

	}

	@Test
	public void testBillUpdateAddSingleProductCatC() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-0003", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue());
		assertThat(retrieveUpdatedBill.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedBill.getTotalValue()).isEqualTo(40 * 2 * 1);
	}

	@Test(expected = CustomException.class)
	public void testBillUpdateRemoveExistingProductFromEmptyBill() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-0001", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue() + "   list of items = " + retrieveUpdatedBill.getLineItems());

	}

	@Test(expected = CustomException.class)
	public void testBillUpdateRemoveNonExistingProduct() {

		// create a new bill to update information.
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));

		Long billId = o1.getId();
		BillUpdateInfo billupdateInfo = new BillUpdateInfo();
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();

		productsToBeRemoved.add(new ProductInfoForBill("DDD-abc-0003", 2));
		billupdateInfo.setProductsToBeAdded(productsToBeAdded);
		billupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		billupdateInfo.setStatus(BillStatus.RELEASED);

		System.out.println("billupdateInfo = " + billupdateInfo);
		billService.updateBill(billupdateInfo, billId);
		Bill retrieveUpdatedBill = billService.getBillById(o1.getId());
		System.out.println("retrieveUpdatedBill = " + retrieveUpdatedBill.getNoOfItems() + "  value ="
				+ retrieveUpdatedBill.getTotalValue() + "   list of items = " + retrieveUpdatedBill.getLineItems());

	}

	@Test
	public void testCreateBill() {
		Bill o1 = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));
		Bill o2 = billService.getBillById(o1.getId());
		assertThat(o1.getId()).isEqualTo(o2.getId());
	}

}
