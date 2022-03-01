package com.example.User.Login.and.Registration.registration.token;

import java.time.LocalDate;
import java.time.LocalDateTime;import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.example.User.Login.and.Registration.appuser.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

	@Id
	@SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
	private Long id;
	
	@Column(nullable = false)
	private String token;
	
	@Column(nullable = false)
	private LocalDateTime createDateTime;
	
	@Column(nullable = false)
	private LocalDateTime expiredDateTime;
	
	private LocalDateTime confirmedDateTime;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = true, name = "app_user_id")
	private AppUser appUser;
	
	public ConfirmationToken(String token, LocalDateTime createDateTime, LocalDateTime expiredDateTime,
			 AppUser appUser) {
		super();
		this.token = token;
		this.createDateTime = createDateTime;
		this.expiredDateTime = expiredDateTime;
		this.appUser = appUser;
	}

}
