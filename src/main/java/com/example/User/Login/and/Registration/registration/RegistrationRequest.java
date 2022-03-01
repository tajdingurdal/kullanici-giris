package com.example.User.Login.and.Registration.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {
	
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String password;
	
	//private final String userName;

}
