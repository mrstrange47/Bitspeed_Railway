package com.example.bitespeed.backend;

import com.example.bitespeed.backend.entity.Contact;
import com.example.bitespeed.backend.service.ContactService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialCreate(ContactService contactService) {
		return (args) -> {
//			Contact contact1 = new Contact("123456","lorraine@hillvalley.edu",null,"primary","2023-04-01 00:00:00"
//					,"2023-04-01 00:00:00", null);
//			contactService.addContact(contact1);
//
//			Contact contact2 = new Contact("123456","mcfly@hillvalley.edu",1,"secondary","2023-04-20 05:30:00"
//					,"2023-04-01 00:00:00", null);
//			contactService.addContact(contact2);

			//Example 2
//			Contact contact3 = new Contact("919191","george@hillvalley.edu",null,"primary","2023-04-11 00:00:00"
//					,"2023-04-11 00:00:00", null);
//			contactService.addContact(contact3);
//
//          Contact contact4 = new Contact("717171","biffsucks@hillvalley.edu",null,"primary","2023-04-20 00:00:00"
//					,"2023-04-20 00:00:00", null);
//			contactService.addContact(contact4);

            //Example 3 => add new secondary record
//            Contact contact5 = new Contact("123456","lorraine@hillvalley.edu",null,"primary","2023-04-20 00:00:00"
//                    ,"2023-04-20 00:00:00", null);
//            contactService.addContact(contact5);

		};
	}
}
