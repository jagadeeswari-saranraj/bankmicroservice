package com.microservice_accounts.dto;


public class ResponseDTO {

	public ResponseDTO(String statusCode, String statusMsg) {
		// TODO Auto-generated constructor stub
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
	}
	
	public ResponseDTO() {
	// TODO Auto-generated constructor stub
	}

	private String statusCode;
	
	private String statusMsg;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
	
}
