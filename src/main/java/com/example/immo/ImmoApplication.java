package com.example.immo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.immo.models.Message;
import com.example.immo.models.Rental;
import com.example.immo.models.Role;
import com.example.immo.models.User;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.services.MessageService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;

@SpringBootApplication
public class ImmoApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	@Autowired
	private RentalService rentalService;
	@Autowired
	private MessageService messageService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ImmoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// init role table
		if (roleRepository.findByAuthority("ADMIN").isPresent())
			return;
		Role adminRole = roleRepository.save(new Role("ADMIN"));
		roleRepository.save(new Role("USER"));

		Role userRole = roleRepository.findByAuthority("USER").get();
		Set<Role> userAuthority = new HashSet<>();
		userAuthority.add(userRole);

		Set<Role> adminAuthority = new HashSet<>();
		adminAuthority.add(adminRole);

		this.initDB(adminAuthority, userAuthority);
	}

	private void initDB(Set<Role> adminAuthority, Set<Role> userAuthority) {
		this.createBaseUsers(adminAuthority, userAuthority);
		this.createRentals();
		this.createMessages();
	}

	private void createBaseUsers(Set<Role> adminAuthority, Set<Role> userAuthority) {
		userService.saveUser(new User(null, "Laurent GINA", "laurentgina@mail.com",
				passwordEncoder.encode("laurent"), adminAuthority));
		userService.saveUser(new User(null, "Sophie FONCEK", "sophiefoncek@mail.com",
				passwordEncoder.encode("sophie"), userAuthority));
		userService.saveUser(new User(null, "Agathe FEELING", "agathefeeling@mail.com",
				passwordEncoder.encode("agathe"), userAuthority));
	}

	private void createRentals() {
		Rental rental1 = Rental.builder().name("name1").rentalId(1L).owner(userService.getUser(1L))
				.description("description1")
				.picture("picture1.jpg").surface(31F).price(501F).build();
		Rental rental2 = Rental.builder().name("name2").rentalId(2L).owner(userService.getUser(2L))
				.description("description2")
				.picture("picture2.jpg").surface(32F).price(502F).build();
		Rental rental3 = Rental.builder().name("name3").rentalId(3L).owner(userService.getUser(1L))
				.description("description3")
				.picture("picture3.jpg").surface(33F).price(503F).build();

		rentalService.saveRental(rental1);
		rentalService.saveRental(rental2);
		rentalService.saveRental(rental3);
	}

	private void createMessages() {
		Message message1 = Message.builder().message("message1").user(userService.getUser(1L))
				.rental(rentalService.getRental(1L)).build();
		Message message2 = Message.builder().message("message2").user(userService.getUser(3L))
				.rental(rentalService.getRental(2L)).build();
		messageService.saveMessage(message1);
		messageService.saveMessage(message2);
	}

}
