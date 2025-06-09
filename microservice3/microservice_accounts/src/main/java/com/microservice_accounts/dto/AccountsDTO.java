package com.microservice_accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
		name="Account",
		description="Schema to hold Customer and Account information"
)
public class AccountsDTO {

	@NotEmpty(message = "AccountNumber can not be a null or empty")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "AccountNumber must be 10 digits")
	private Long accountNumber;
	
	@NotEmpty(message = "AccountType can not be a null or empty")
	private String accountType;
	
	@NotEmpty(message = "BranchAddress can not be a null or empty")
	private String branchAdderess;

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBranchAdderess() {
		return branchAdderess;
	}

	public void setBranchAdderess(String branchAdderess) {
		this.branchAdderess = branchAdderess;
	}
	
	
}
