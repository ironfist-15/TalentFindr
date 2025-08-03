package com.project.TalentFindr;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TalentFindrApplication {

	public static void main(String... args) {
		SpringApplication.run(TalentFindrApplication.class, args);
	}

	// ensure gmail is accurate
	// then make sure the page is dynamically loaded for chats .
}


//in this code uploadDir says where to upload the photo to , while mvc config helps in routing the http
//request to the exact loaction on the disk,i.e serve the frontend using this loaction on disk
