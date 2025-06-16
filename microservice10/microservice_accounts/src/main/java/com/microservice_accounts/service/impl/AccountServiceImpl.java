package com.microservice_accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import com.microservice_accounts.dto.AccountsMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.microservice_accounts.constants.AccountConstants;
import com.microservice_accounts.dto.AccountsDTO;
import com.microservice_accounts.dto.CustomerDTO;
import com.microservice_accounts.entity.Account;
import com.microservice_accounts.entity.Customer;
import com.microservice_accounts.exception.CustomerAlreadyExistsException;
import com.microservice_accounts.exception.ResourceNotFoundException;
import com.microservice_accounts.mapper.AccountMapper;
import com.microservice_accounts.mapper.CustomerMapper;
import com.microservice_accounts.repository.AccountRepository;
import com.microservice_accounts.repository.CustomerRepository;
import com.microservice_accounts.service.IAccountService;


@Service
public class AccountServiceImpl implements IAccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepository;
	private CustomerRepository customerRepository;
	private final StreamBridge streamBridge;

	@Autowired
	AccountServiceImpl(
			AccountRepository accountRepository,
			CustomerRepository customerRepository,
			StreamBridge streamBridge
	) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.streamBridge = streamBridge;
	}

	@Override
	public void createAccount(CustomerDTO customerDto) {
	  Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
	  Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
	  if(optionalCustomer.isPresent()) {
		throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber" + customerDto.getMobileNumber());
	  }
	  Customer savedCustomer = customerRepository.save(customer);
	  Account savedAccount = accountRepository.save(createNewAccount(savedCustomer));
	  sendCommuncation(savedAccount, savedCustomer);
	}

	private void sendCommuncation(Account account, Customer customer) {
		var accountsMsgDto = new AccountsMsgDTO(
				account.getAccountNumber(),
				customer.getName(),
				customer.getEmail(),
				customer.getMobileNumber()
		);
		logger.info("Sending Communcation request for the details: {}", accountsMsgDto);
		var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
		logger.info("Is the Communication request successfully triggered ? : {}", result);
	}

	private Account createNewAccount(Customer customer) {
		Account newAccount = new Account();
		newAccount.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L+new Random().nextInt(900000000);

		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType(AccountConstants.SAVINGS);
		newAccount.setBranchAddress(AccountConstants.ADDRESS);
		return newAccount;
	}

	/**
	* @param mobileNumber - Input Mobile Number
	* @return Accounts Details based on a given mobileNumber
	*/
	@Override
	public CustomerDTO fetchAccount(String mobileNumber) {
	  Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
			  () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
	  );
	  Account accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
			  () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
	  );
	  CustomerDTO customerDto = CustomerMapper.mapToCustomerDTO(customer, new CustomerDTO());
	  customerDto.setAccountDto(AccountMapper.mapToAccountsDTO(accounts, new AccountsDTO()));
	  return customerDto;
	}

	/**
	 * @param customerDto - CustomerDto Object
	 * @return boolean indicating if the update of Account details is successful or not
	 */
	@Override
	public boolean updateAccount(CustomerDTO customerDto) {
		boolean isUpdated = false;
		AccountsDTO accountsDto = customerDto.getAccountDto();
		if(accountsDto !=null ){
			Account accounts = accountRepository.findByAccountNumber(accountsDto.getAccountNumber()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
			);
			AccountMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountRepository.save(accounts);

			Long customerId = accounts.getCustomerId();
			Customer customer = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
			);
			CustomerMapper.mapToCustomer(customerDto,customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		return  isUpdated;
	}

	@Override
	public boolean deleteAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
		);
		accountRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;
	}

	/**
	 * @param accountNumber
	 * @return
	 */
	@Override
	public boolean updateCommincationStatus(Long accountNumber) {
		boolean isUpdated = false;
		if(accountNumber != null) {
			Account accounts = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
			);
			accounts.setCommunicationSw(true);
			accountRepository.save(accounts);
			isUpdated = true;
		}
		return isUpdated;
	}


}
