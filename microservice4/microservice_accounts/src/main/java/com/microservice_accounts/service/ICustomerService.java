package com.microservice_accounts.service;

import com.microservice_accounts.dto.CustomerDetailsDTO;

public interface ICustomerService {

    /**
     *
     * @param mobileNumber - Input mobile Number
     * @return Customer Details based on given mobileNumber
     */
    CustomerDetailsDTO fetchCustomerDetails(String mobileNumber);

}
