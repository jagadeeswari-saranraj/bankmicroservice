package com.microservice_accounts.controller;

import com.microservice_accounts.dto.AccountsContactInfoDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.microservice_accounts.constants.AccountConstants;
import com.microservice_accounts.dto.CustomerDTO;
import com.microservice_accounts.dto.ResponseDTO;
import com.microservice_accounts.dto.ErrorResponseDTO;
import com.microservice_accounts.service.IAccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.util.concurrent.TimeoutException;

@Tag(
	name = "Account Contrller",
	description =  "REST API with CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
@RequestMapping(path="/api", produces= {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final IAccountService iAccountService;

	AccountController(IAccountService iAccountService) {
		this.iAccountService = iAccountService;
	}

	@Value("${build.version}")
	private String buildVersion;

	@Autowired
	private Environment environment;

	@Autowired
	private AccountsContactInfoDTO accountsContactInfoDTO;

	@GetMapping("/sayhello")
	public String sayHello() {
		return "Hello World";
	}


	@Operation(
			summary = "Fetch Account Details REST API",
			description = "REST API to fetch Customer &  Account details based on a mobile number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDTO> fetchAccountDetails(@RequestParam
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
		String mobileNumber) {
       CustomerDTO customerDto = iAccountService.fetchAccount(mobileNumber);
       return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

	@Operation(
			summary = "Create Account REST API",
			description = "REST API to create new Customer &  Account inside EazyBank"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "HTTP Status CREATED"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDto) {
		iAccountService.createAccount(customerDto);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDTO(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
	}

	@Operation(
			summary = "Update Account Details REST API",
			description = "REST API to update Customer &  Account details based on a account number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "417",
					description = "Expectation Failed"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	})
	@PutMapping("/update")
	public ResponseEntity<ResponseDTO> updateAccountDetails(@Valid @RequestBody CustomerDTO customerDto) {
		boolean isUpdated = iAccountService.updateAccount(customerDto);
		if(isUpdated) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDTO(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
		}else{
			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE));
		}
	}

	@Operation(
			summary = "Delete Account & Customer Details REST API",
			description = "REST API to delete Customer &  Account details based on a mobile number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "417",
					description = "Expectation Failed"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDTO> deleteAccountDetails(@RequestParam 
			@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
			String mobileNumber) {
		boolean isDeleted = iAccountService.deleteAccount(mobileNumber);
		if(isDeleted) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDTO(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
		}else{
			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
		}
	}

	@Operation(
			summary = "Get Build information",
			description = "REST API to get build information"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() throws TimeoutException {
		logger.debug("getBuildInfo() method Invoked");
//		throw new TimeoutException();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildVersion);
	}

	public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
		logger.debug("getBuildInfoFallback() method Invoked");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("0.9");
	}

	@Operation(
			summary = "Get Java Version",
			description = "Get Java version details"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(environment.getProperty("JAVA_HOME"));
	}

	public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Jdk 21");
	}

	@Operation(
			summary = "Get Contact infor Version",
			description = "Get Java contact info details"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDTO.class)
					)
			)
	}
	)
	@GetMapping("/contact-info")
	public ResponseEntity<AccountsContactInfoDTO> getContactInfo() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(accountsContactInfoDTO);
	}
	
}
