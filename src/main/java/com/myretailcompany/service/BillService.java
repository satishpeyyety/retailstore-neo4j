package com.myretailcompany.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myretailcompany.dataaccesslayer.entity.Bill;
import com.myretailcompany.dataaccesslayer.entity.LineItem;
import com.myretailcompany.dataaccesslayer.entity.Product;
import com.myretailcompany.dataaccesslayer.repository.BillRepository;
import com.myretailcompany.dataaccesslayer.repository.LineItemRepository;
import com.myretailcompany.dataaccesslayer.repository.ProductRepository;
import com.myretailcompany.rest.controller.CustomException;
import com.myretailcompany.rest.controller.bill.beans.BillUpdateInfo;
import com.myretailcompany.rest.controller.bill.beans.ProductInfoForBill;
import com.myretailcompany.util.ProductCategory;

@Service
public class BillService {
	
	private final int DEPTH=3;

	@Autowired
	private BillRepository billRepo;

	@Autowired
	private LineItemRepository lineItemRepo;

	final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ProductRepository productRepo;

	private Bill addProductToBill(Long billId, String barCodeId, int quantity) {
		Bill o1 = billRepo.findOne(billId,DEPTH);
		logger.info("** bill =  "+o1);
		Product selectedProduct1 = verifyIfProductExists(barCodeId);

		// create line item for a product
		LineItem l1 = new LineItem(selectedProduct1, quantity);
		lineItemRepo.save(l1);

		// add lineitem to bill.
		List<LineItem> currentLineItems = o1.getLineItems();
		if (currentLineItems != null) { // There are lineItems in the bill
										// already.
			logger.debug("There are lineItems in the bill already..Adding to list of items");
			LineItem existingLi = getLineItemWithBarCodeId(barCodeId, currentLineItems);
			if (existingLi == null) {

				o1.getLineItems().add(l1); // there is no line item with
											// existing product
			} else {
				long newQty = existingLi.getQuantity() + quantity;

				existingLi.setQuantity(newQty); // increment the quantity of the
												// product if it already exists
												// in the Bill.
			}

		} else {
			logger.debug("There are no line items currently in the Bill..Creating new list");
			currentLineItems = new ArrayList<>();
			currentLineItems.add(l1);
			o1.setLineItems(currentLineItems);
		}
		billRepo.save(o1);
		logger.debug("Product Added Successfully  to Bill : " + l1.getId());
		return o1;
	}

	// Read

	private void computeTotalValues(Bill bill) {

		int noOfItems = 0;
		double totalValue = 0;
		double totalCost = 0;

		logger.info("***** BILL inside computeTotalValues " +bill);
		if (null != bill.getLineItems()) {
			List<LineItem> lineItems = bill.getLineItems();
			Iterator<LineItem> lineItemsIter = lineItems.iterator();
			while (lineItemsIter.hasNext()) {
				LineItem li = lineItemsIter.next();
				if (li.getProduct()==null){
					logger.info("product for line item is null ..serious error->" + li);
				}else{
					double saleValue = computeValueForItem(li.getQuantity(), li.getProduct().getProductCategory(),li.getProduct().getRate());
					logger.debug("SaleValue &  Line Item  : " + saleValue + "   " + li);
					totalValue += saleValue;
					totalCost += li.getQuantity() * li.getProduct().getRate();
					noOfItems++;
				}
			}
		}
		bill.setNoOfItems(noOfItems);
		bill.setTotalValue(totalValue);
		bill.setTotalCost(totalCost);
		bill.setTotalTax(totalValue - totalCost);
		billRepo.save(bill);
	}

	private double computeValueForItem(long quantity, ProductCategory productCategory, double rate) {
		logger.debug("productCategory : " + productCategory + "  quantity = " + quantity + "  rate = " + rate);
		double saleValue = 0;
		if (productCategory.equals(ProductCategory.A)) {
			saleValue = quantity * rate * 1.1; // 10% levy

		} else if (productCategory.equals(ProductCategory.B)) {
			saleValue = quantity * rate * 1.2; // 10% levy

		} else if (productCategory.equals(ProductCategory.C)) {
			saleValue = quantity * rate;
		}
		return saleValue;
	}

	public Bill createBill(Bill bill) {
		logger.info("Input recieved to create Bill = " + bill);
		Bill bill1 = billRepo.save(bill);
		logger.info("Created product with id = " + bill1.getId());
		return bill1;

	}

	public void deleteBill(Long id) {
		verifyBillExists(id);
		billRepo.deleteBillAndLineItems(id); //deletes only if the relationships exists
		billRepo.delete(id); // deletes an orphan node
	}
	
	public void deleteAllBills() {
		billRepo.deleteAllBills();
	}
	

	public Iterable<Bill> getAllBills() {
		Iterable<Bill> bill = billRepo.findAll(DEPTH);
		logger.info("returning all products = "+bill);
		return bill;
	}

	public Bill getBillById(Long id) {
		verifyBillExists(id);
		Bill o1 = billRepo.findOne(id,DEPTH);
		logger.info("** bill =  "+o1);
		
		return o1;
	}

	// Iterate through line items to get the LineItem with product.
	private LineItem getLineItemWithBarCodeId(String barCodeId, List<LineItem> currentLineItems) {
		if (null != currentLineItems) {
			for (int i = 0; i < currentLineItems.size(); i++) {
				LineItem li = currentLineItems.get(i);
				if (null != li && null != li.getProduct() && barCodeId.equals(li.getProduct().getBarCodeId())) {

					// assumes there will only be one item per product. Save
					// method
					// to ensure that there are no duplicates.
					logger.info(" Line Items has product: " + barCodeId);
					return li;
				}
			}

		}
		logger.info(" Current list of Line Items do not have product: " + barCodeId);
		return null;
	}

	private Bill removeProductFromBill(Long billId, String barCodeId) {
		Bill o1 = billRepo.findOne(billId,DEPTH);
		logger.info("****** o1 =  " + o1);
		List<LineItem> currentLineItems = o1.getLineItems();
		// check if the product exists in product master
		verifyIfProductExists(barCodeId);
		logger.info("Bar Code Id to be removed  = " + barCodeId);

		if (currentLineItems != null && !currentLineItems.isEmpty()) {
			LineItem lineItem = getLineItemWithBarCodeId(barCodeId, currentLineItems);
			// check if current list of line items have this product.
			if (null == lineItem) {
				logger.info(
						"Product does not exist in current list of products. Cannot remove productId : " + barCodeId);
				throw new CustomException(
						"Problem with input data: Product does not exist in current list of products. Cannot remove product with BarCode ID "
								+ barCodeId);

			}
			logger.info("line item to be deleted " + lineItem);
			currentLineItems.remove(lineItem);
			o1.setLineItems(currentLineItems);
			billRepo.save(o1);
			// lineItemRepo.delete(lineItem); //delete if it exists
		} else {
			logger.info("There are no line items currently in the Bill..Cannot remove productId : " + barCodeId);
			throw new CustomException(
					"Problem with input data: There are no line items currently in the Bill. Cannot remove product with BarCode ID "
							+ barCodeId);
		}
		billRepo.save(o1);
		return o1;
	}

	public Bill updateBill(BillUpdateInfo billUpdateInfo, Long billId) {

		logger.info("Request recieved for update of  : " + billId);
		if (null == billUpdateInfo) {
			throw new CustomException("There is no information to be updated for id " + billId);
		}
		verifyBillExists(billId);

		logger.info("Processing products to be added");
		if (null != billUpdateInfo.getProductsToBeAdded()) {
			List<ProductInfoForBill> prodToBeAdded = billUpdateInfo.getProductsToBeAdded();
			Iterator<ProductInfoForBill> prodToBeAddedIter = prodToBeAdded.iterator();
			while (prodToBeAddedIter.hasNext()) {
				ProductInfoForBill pInfo = prodToBeAddedIter.next();
				logger.debug("Product to be added : " + pInfo);
				addProductToBill(billId, pInfo.getBarCodeId(), pInfo.getQuantity());
			}
		}

		logger.info("Processing products to be removed");
		if (null != billUpdateInfo.getProductsToBeRemoved()) {
			List<ProductInfoForBill> prodToBeRemoved = billUpdateInfo.getProductsToBeRemoved();
			Iterator<ProductInfoForBill> prodToBeRemovedIter = prodToBeRemoved.iterator();
			while (prodToBeRemovedIter.hasNext()) {
				ProductInfoForBill pInfo = prodToBeRemovedIter.next();
				logger.info("Product to be removed : " + pInfo);
				removeProductFromBill(billId, pInfo.getBarCodeId());
			}
		}

		Bill bill = billRepo.findOne(billId,DEPTH);
		bill.setBillStatus(billUpdateInfo.getStatus());
		logger.info("***** Computing total for the bill ; bill = "+bill);
		computeTotalValues(bill);
		return bill;
	}

	private void verifyBillExists(Long id) {
		Bill bill = billRepo.findOne(id);
		if (bill == null) {
			throw new CustomException("Bill with id " + id + " not found");
		}
		logger.info(" Bill exists with an id = " + id);
	}

	private Product verifyIfProductExists(String barCodeId) {
		List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
		if (null == productsByBarCodeID || productsByBarCodeID.isEmpty()) {
			logger.info("Problem with input data: BarCode ID  " + barCodeId + " does not exist in Product Master");
			throw new CustomException(
					"Problem with input data: BarCode ID " + barCodeId + " does not exist in Product Master");
		} else {
			logger.debug("selectedProduct1  = " + productsByBarCodeID.get(0).getId());
		}
		return productsByBarCodeID.get(0);
	}

}
