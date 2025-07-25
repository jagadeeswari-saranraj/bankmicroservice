package com.microservice_accounts.mapper;

import com.microservice_accounts.dto.CustomerDTO;
import com.microservice_accounts.entity.Customer;

public class CustomerMapper {

	public static CustomerDTO mapToCustomerDTO(Customer customer, CustomerDTO customerDto) {
		customerDto.setName(customer.getName());
		customerDto.setEmail(customer.getEmail());
		customerDto.setMobileNumber(customer.getMobileNumber());		
		return customerDto;
	}
	
	public static Customer mapToCustomer(CustomerDTO customerDto, Customer customer) {
		customer.setName(customerDto.getName());
		customer.setEmail(customerDto.getEmail());
		customer.setMobileNumber(customerDto.getMobileNumber());	
		return customer;
	}
}
