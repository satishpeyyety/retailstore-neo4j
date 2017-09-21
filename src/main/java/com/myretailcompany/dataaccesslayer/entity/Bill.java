package com.myretailcompany.dataaccesslayer.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import com.myretailcompany.util.BillStatus;


@NodeEntity(label="BILL")
public class Bill {

	
	@GraphId
	@Property(name="billId")
	private Long id;
	

	private int noOfItems;
	private double totalCost;

	private double totalTax;

	private double totalValue;


	private BillStatus billStatus;
	
	@Relationship(type="CONTAINS", direction=Relationship.UNDIRECTED)
	private List<LineItem> lineItems;

	public Bill() {
		super();
	}

	public Bill(double totalValue, int noOfItems, BillStatus billStatus) {
		super();
		this.totalValue = totalValue;
		this.noOfItems = noOfItems;
		this.billStatus = billStatus;
	}

	public Bill(List<LineItem> lineItems) {
		super();
		this.lineItems = lineItems;
	}

	public BillStatus getBillStatus() {
		return billStatus;
	}

	public long getId() {
		return id;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public int getNoOfItems() {
		return noOfItems;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setBillStatus(BillStatus billStatus) {
		this.billStatus = billStatus;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
