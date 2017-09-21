package com.myretailcompany.rest.controller;

import java.net.URI;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.myretailcompany.dataaccesslayer.entity.Bill;
import com.myretailcompany.rest.controller.bill.beans.BillUpdateInfo;
import com.myretailcompany.service.BillService;
import com.myretailcompany.util.BillStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "onlinestore",description="Manage Bills")
@RestController
public class BillController {
	@Autowired
	private BillService billService;

	final Logger logger = LogManager.getLogger(getClass());

	@ApiOperation(produces = "application/json", value = "Creates an Bill and returns an id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bill details") })
	@RequestMapping(value = "/bills", method = RequestMethod.POST)
	public ResponseEntity<Bill> createBill() {
		logger.info("Request recieved to create Bill = ");
		Bill bill = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));
		logger.info("Created Bill with id = " + bill.getId());
		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bill.getId())
				.toUri();
		logger.info("Setting header url with newly created Bill= " + bill.getId());
		responseHeaders.setLocation(newPollUri);
		return new ResponseEntity<>(bill, responseHeaders, HttpStatus.CREATED);
	}

	@ApiOperation(produces = "application/json", value = "Deletes Bill")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Status of request"),
			@ApiResponse(code = 404, message = "Bill does not exist") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteBill(@PathVariable Long id) {
		billService.deleteBill(id);
		return new ResponseEntity<>("{\"status\": \"success\"}", HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "fetches all bills from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@RequestMapping(value = "/bills", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Bill>> getAllBills() {
		return new ResponseEntity<>(billService.getAllBills(), HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "fetches a particular bill details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Bill details"),
			@ApiResponse(code = 404, message = "Bill Not Found") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.GET)
	public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
		return new ResponseEntity<>(billService.getBillById(id), HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "Add or Remove products from the Bill")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bill details"),
			@ApiResponse(code = 404, message = "Data validation error") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Bill> updateBill(@RequestBody BillUpdateInfo billUpdateInfo, @PathVariable Long id) {
		Bill updated = billService.updateBill(billUpdateInfo, id);
		logger.info("Request recieved =  " + billUpdateInfo);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

}
