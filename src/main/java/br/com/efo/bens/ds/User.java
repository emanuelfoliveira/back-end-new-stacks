package br.com.efo.bens.ds;

import org.springframework.data.annotation.Id;

import br.com.efo.bens.common.exception.InvalidConstructorException;
import br.com.efo.bens.common.utils.PasswordUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder
public class User {
	@Id
	private Integer id;
	private String name;
	private String email;
	private String gender;
	private String userName;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String passwordText;
	@Builder.Default
	private Role role = Role.USER;
	private boolean originalPass;

	public User(String userName, String passwordText, String name, String email, String gender, Integer branch,
			Integer team, Integer seniority) {

		if (isInvalidUser(userName, passwordText, name, email, gender, branch, team)) {
			throw new InvalidConstructorException();
		}

		this.userName = userName;
		this.passwordText = passwordText;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.role = Role.USER;
	}

	public boolean isInvalidUser(String userName, String passwordText, String name, String email, String gender,
			Integer branch, Integer team) {
		return (userName == null || name == null || email == null || branch == null || team == null);
	}

	public String getPasswordText() {
		if (this.passwordText == null) {
			return null;
		}
		if (this.passwordText.equals(PasswordUtils.DEFAULT_PASSWORD)) {
			this.passwordText = PasswordUtils.generateSecurePassword(PasswordUtils.DEFAULT_PASSWORD);
		}
		return this.passwordText;
	}

	public void setPasswordText(String passwordText) {
		if (originalPass) {
			this.passwordText = passwordText;
			this.originalPass = false;
			return;
		}
		this.passwordText = PasswordUtils.generateSecurePassword(passwordText);
	}

}
