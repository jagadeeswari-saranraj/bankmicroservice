package com.microservice_accounts.service;

import com.microservice_accounts.dto.CustomerDTO;

public interface IAccountService {
	
    /**
    *
    * @param customerDto - CustomerDTO Object
    */
    void createAccount(CustomerDTO customerDto);

    /**
    *
    * @param mobileNumber - Input Mobile Number
    * @return Accounts Details based on a given mobileNumber
    */
    CustomerDTO fetchAccount(String mobileNumber);

    /**
    *
    * @param customerDto - CustomerDto Object
    * @return boolean indicating if the update of Account details is successful or not
    */
    boolean updateAccount(CustomerDTO customerDto);

    /**
     *
     * @param mobileNumber - Input MobileNumber
     * @return boolean indicating if the delete of Account details is successful or not
     */
     boolean deleteAccount(String mobileNumber);

    /**
     * @param accountNumber
     * @return
     */
     boolean updateCommincationStatus(Long accountNumber);
}
