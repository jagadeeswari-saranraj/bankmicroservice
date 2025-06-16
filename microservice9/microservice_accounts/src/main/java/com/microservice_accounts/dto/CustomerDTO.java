package com.microservice_accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
		name="Customer",
		description="Schema to hold Customer and Account information"
)
public class CustomerDTO {

	@NotEmpty(message = "Name can not be null or empty")
	@Schema(
			description="Name of customer", example="Jhon peter"
	)
	@Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
	private String name;

	@NotEmpty(message = "Email address can not be null or empty")
	@Schema(
			description = "Email of Customer", example = "jhon@gmail.com"
	)
	@Email(message="Email address should be a valid value")
	private String email;

	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	private AccountsDTO accountDto;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public AccountsDTO getAccountDto() {
		return accountDto;
	}

	public void setAccountDto(AccountsDTO accountDto) {
		this.accountDto = accountDto;
	}
	
	
}
