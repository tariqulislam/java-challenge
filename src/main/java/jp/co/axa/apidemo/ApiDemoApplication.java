package jp.co.axa.apidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.*;


@SpringBootApplication(scanBasePackages = {"jp.co.axa.apidemo"})
@EnableWebSecurity
@EnableCaching
@EnableSwagger2
public class ApiDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiDemoApplication.class, args);
	}

}
