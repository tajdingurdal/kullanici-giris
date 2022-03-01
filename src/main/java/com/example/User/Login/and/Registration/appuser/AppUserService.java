package com.example.User.Login.and.Registration.appuser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.User.Login.and.Registration.email.EmailSender;
import com.example.User.Login.and.Registration.registration.EmailValidator;
import com.example.User.Login.and.Registration.registration.RegistrationRequest;
import com.example.User.Login.and.Registration.registration.token.ConfirmationToken;
import com.example.User.Login.and.Registration.registration.token.ConfirmationTokenRepository;
import com.example.User.Login.and.Registration.registration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	
	@Autowired
	private final AppUserRepository appUserRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService tokenService;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;


	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}

	public String signUpUser(AppUser appUser) throws Exception {
		boolean isExistUser = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

		if (isExistUser) {

			throw new IllegalArgumentException("Email already taken");
		}
		String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
		appUser.setPassword(encodedPassword);

		appUserRepository.save(appUser);

		// TODO: Send confirmation token
		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(1), appUser);
		this.tokenService.saveConfirmationToken(confirmationToken);

		return token;
	}
	public String sendTokenAgain(RegistrationRequest request) {
// TODO: Kayıtlı olan AppUser'in tokene tıklama süresi geçtiyse tekrar token talep etmesini sağla.

		boolean isValidEmail = this.emailValidator.test(request.getEmail());
		
		if (!isValidEmail) {
			throw new IllegalStateException("Invalid Email");
		}
		
		boolean isExistUser = appUserRepository.findByEmail(request.getEmail()).isPresent();

		if (isExistUser) {
			String encodePassword = bCryptPasswordEncoder.encode(request.getPassword());
			
			AppUser appUser = new AppUser(request.getFirstName(), request.getLastName(), encodePassword,
					request.getEmail(), AppUserRole.USER);
			
			deleteAppUserByEmail(appUser);
			
			String newtoken = UUID.randomUUID().toString();
			
			ConfirmationToken token = new ConfirmationToken(newtoken, LocalDateTime.now(),
					LocalDateTime.now().plusMinutes(5), appUser);
			
			this.tokenService.saveConfirmationToken(token);

			String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

			// emailSender.send(email, buildEmail(email, link));

			return link;
			
		}else {
			throw new IllegalArgumentException("Email not found");
		}
	}
	
	public void deleteAppUserByEmail(AppUser appUser) {
		appUserRepository.deleteAppUserByEmail(appUser);
	}
	
	public void deleteAppUserById(Long id) {
		appUserRepository.deleteById(id);
	}
	
	public int enableAppUser(String email) {
		return appUserRepository.enableAppUser(email);
	}

	public List<AppUser> getAll() {
		return this.appUserRepository.findAll();
	}

	private String buildEmail(String name, String link) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n"
				+ "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n"
				+ "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+ "    <tbody><tr>\n" + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n"
				+ "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
				+ "          <tbody><tr>\n" + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
				+ "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+ "                  <tbody><tr>\n" + "                    <td style=\"padding-left:10px\">\n"
				+ "                  \n" + "                    </td>\n"
				+ "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
				+ "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
				+ "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n"
				+ "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n"
				+ "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n"
				+ "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+ "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
				+ "      <td>\n" + "        \n"
				+ "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+ "                  <tbody><tr>\n"
				+ "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
				+ "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n"
				+ "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n"
				+ "  </tbody></table>\n" + "\n" + "\n" + "\n"
				+ "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+ "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
				+ "      <td width=\"10\" valign=\"middle\"><br></td>\n"
				+ "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
				+ "        \n"
				+ "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name
				+ ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
				+ link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>"
				+ "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
				+ "    </tr>\n" + "    <tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
				+ "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>";
	}


}
