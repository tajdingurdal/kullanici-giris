package com.example.User.Login.and.Registration.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
	 private final JavaMailSender mailSender;
	 
	@Override
	@Async
	public void send(String to, String text) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			
			helper.setText(text, true);
			helper.setTo(to);
			helper.setSubject("Confirm your email");
			helper.setFrom("noreply@gurdal.com");
			mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			LOGGER.error("failed to send email", e);
			throw new IllegalStateException("failed to send email");
		}
		
	}

}
