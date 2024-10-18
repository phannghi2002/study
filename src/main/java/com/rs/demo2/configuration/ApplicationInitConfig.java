package com.rs.demo2.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rs.demo2.entity.User;
import com.rs.demo2.enums.Role;
import com.rs.demo2.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor // dung de khoi tao cac constructor co tham so la final, tiem su phu thuoc dung no
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // dung de log
public class ApplicationInitConfig {

	PasswordEncoder passwordEncoder;

	@Bean
	// vi H2Database co syntax khac voi mySQL nen khi ta start application thi no se gay ra loi cu phap khi ta test
	// su dung ConditionalOnProperty de quyet dinh xem khi nao no se chay cai Bean nay, Bean nay duoc chay khi
	// ta ket noi no voi mySQL thoi do do ta can rang buoc no voi gia tri trong application
	// spring:
	//  datasource:
	//    driver-class-name: "com.mysql.cj.jdbc.Driver"

	// prefix: la spring, value la datasource.driver-class-name
	@ConditionalOnProperty(
			prefix = "spring",
			value = "datasource.driver-class-name",
			havingValue = "com.mysql.cj.jdbc.Driver")
	ApplicationRunner applicationRunner(UserRepository userRepository) {
		log.info("Init application...");
		return args -> {
			if (userRepository.findByUserName("admin").isEmpty()) {
				var roles = new HashSet<String>();
				roles.add(Role.ADMIN.name());

				User user = User.builder()
						.userName("admin")
						.password(passwordEncoder.encode("admin"))
						// .roles(roles)
						.build();

				userRepository.save(user);

				log.warn("admin user has been created with default password: admin, please change it");
			}
		};
	}
}
