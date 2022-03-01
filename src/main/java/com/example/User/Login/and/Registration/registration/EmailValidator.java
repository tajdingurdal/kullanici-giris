package com.example.User.Login.and.Registration.registration;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{	
	
	private static final String EMAIL_PATTERN = 
			"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
		            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	@Override
	public boolean test(final String email) {
		 Matcher matcher = pattern.matcher(email);
	     return matcher.matches();
	}
}
