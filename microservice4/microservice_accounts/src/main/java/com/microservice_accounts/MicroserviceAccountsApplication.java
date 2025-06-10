package com.microservice_accounts;

import com.microservice_accounts.dto.AccountsContactInfoDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScans({ @ComponentScan("com.microservice_accounts.controller") })
//@EnableJpaRepositories("com.microservice_accounts.repository")
//@EntityScan("com.microservice_accounts.model")
@EnableConfigurationProperties(value={AccountsContactInfoDTO.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts Microservice REST API Documentation",
				description = "Microservice Documentation",
				version = "v1",
				contact = @Contact(
						name = "Jaga",
						email = "jaga@gmail.com",
						url = "https://www.eans.in"
				)
		)
)
public class MicroserviceAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAccountsApplication.class, args);
	}

}
