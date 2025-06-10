package com.microservice_accounts.mapper;

import com.microservice_accounts.dto.AccountsDTO;
import com.microservice_accounts.entity.Account;

public class AccountMapper {

		public static AccountsDTO mapToAccountsDTO(Account account, AccountsDTO accountsDto) {
			accountsDto.setAccountNumber(account.getAccountNumber());
			accountsDto.setAccountType(account.getAccountType());
			accountsDto.setBranchAdderess(account.getBranchAddress());		
			return accountsDto;
		}
		
		public static Account mapToAccounts(AccountsDTO accountsDto, Account account) {
			account.setAccountNumber(accountsDto.getAccountNumber());
			account.setAccountType(accountsDto.getAccountType());
			account.setBranchAddress(accountsDto.getBranchAdderess());		
			return account;
		}
	
}
