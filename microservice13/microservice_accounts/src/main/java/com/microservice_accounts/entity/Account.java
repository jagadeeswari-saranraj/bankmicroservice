package com.microservice_accounts.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
public class Account extends BaseEntity {
	
	@Column(name="customer_id")
	private Long customerId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="account_id")
	private Long accountId;
	
	@Column(name="account_number")
	private Long accountNumber;
	
	@Column(name="account_type")
	private String accountType;

	@Column(name="branch_address")	
	private String branchAddress;

	@Column(name = "communication_sw")
	private Boolean communicationSw;

	public Long getCustomerId() {
		return customerId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

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

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public Boolean getCommunicationSw() {
		return communicationSw;
	}

	public void setCommunicationSw(Boolean communicationSw) {
		this.communicationSw = communicationSw;
	}
}
