package com.example.User.Login.and.Registration.registration;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.User.Login.and.Registration.appuser.AppUser;
import com.example.User.Login.and.Registration.appuser.AppUserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

	private final RegistrationService registrationService;
	private final AppUserService appUserService;

	@PostMapping("/new")
	public String register(@RequestBody RegistrationRequest request) throws Exception {

		return registrationService.register(request);
	}

	@GetMapping(path = "/confirm")
	public String confirm(@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}
	

	@GetMapping(path = "/sendTokenAgain")
	public String sendTokenAgain(@RequestBody RegistrationRequest request) {
		 String sendTokenAgain = appUserService.sendTokenAgain(request);
		 return sendTokenAgain;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<AppUser>> getAll() {
		List<AppUser> list = this.appUserService.getAll();

		return new ResponseEntity<List<AppUser>>(list, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus
	public void deleteAppUserById(@PathVariable("id") Long id) {
		this.appUserService.deleteAppUserById(id);
	}
}

























