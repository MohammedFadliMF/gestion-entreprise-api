package com.example.springbootoauthjwt;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import com.example.springbootoauthjwt.dao.UserRepository;
// import com.example.springbootoauthjwt.model.Role;
// import com.example.springbootoauthjwt.model.User;

@SpringBootApplication
public class SpringBootOauthJwtApplication {
	// @Autowired
	// private PasswordEncoder passwordEncoder;
	// @Autowired
	// private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootOauthJwtApplication.class, args);
	}
	
	//@Bean
	// CommandLineRunner commandLineRunner() {
	// 	return args -> {
			
	// 			User user = new User();
	// 			user.setUsername("mohammed");
	// 			user.setPassword(passwordEncoder.encode( "mohammed"));
	// 			user.setRoles(List.of());
	// 			userRepository.save(user);
			
	// 	};
	// }

}
