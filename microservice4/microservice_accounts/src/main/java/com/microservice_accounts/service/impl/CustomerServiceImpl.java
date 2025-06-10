package com.microservice_accounts.service.impl;


import com.microservice_accounts.dto.AccountsDTO;
import com.microservice_accounts.dto.CardsDTO;
import com.microservice_accounts.dto.CustomerDetailsDTO;
import com.microservice_accounts.dto.LoansDTO;
import com.microservice_accounts.entity.Account;
import com.microservice_accounts.entity.Customer;
import com.microservice_accounts.exception.ResourceNotFoundException;
import com.microservice_accounts.mapper.AccountMapper;
import com.microservice_accounts.mapper.CustomerMapper;
import com.microservice_accounts.repository.AccountRepository;
import com.microservice_accounts.repository.CustomerRepository;
import com.microservice_accounts.service.ICustomerService;
import com.microservice_accounts.service.client.CardsFiegnClient;
import com.microservice_accounts.service.client.LoansFiegnClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CardsFiegnClient cardsFiegnClient;

    @Autowired
    private LoansFiegnClient loansFiegnClient;

    public CustomerServiceImpl(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            CardsFiegnClient cardsFiegnClient,
            LoansFiegnClient loansFiegnClient
    ) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.cardsFiegnClient = cardsFiegnClient;
        this.loansFiegnClient = loansFiegnClient;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Customer Details based on given mobileNumber
     */
    @Override
    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );

        CustomerDetailsDTO customerDetailsDto = CustomerMapper.mapToCustomerDetailsDTO(customer, new CustomerDetailsDTO());
        customerDetailsDto.setAccountDto(AccountMapper.mapToAccountsDTO(account, new AccountsDTO()));

        ResponseEntity<LoansDTO> loansDTOResponseEntity = loansFiegnClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoanDto(loansDTOResponseEntity.getBody());

        ResponseEntity<CardsDTO> cardsDTOResponseEntity = cardsFiegnClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardDto(cardsDTOResponseEntity.getBody());

        return customerDetailsDto;
    }
}
