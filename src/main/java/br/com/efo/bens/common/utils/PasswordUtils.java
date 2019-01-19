package br.com.efo.bens.common.utils;

import java.security.MessageDigest;

import org.springframework.stereotype.Component;

/**
 * 
 * @author emanuelfoliveira
 *
 */
@Component
public class PasswordUtils {

	public static final String DEFAULT_PASSWORD = "BENS_PASS";

	public static String generateSecurePassword(String password) {

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(password.getBytes());

			byte byteData[] = messageDigest.digest();

			StringBuffer sbCryptoPassword = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sbCryptoPassword.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sbCryptoPassword.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static boolean verifyUserPassword(String providedPassword, String securedPassword) {
		if (providedPassword.equals(securedPassword)) {
			return true;
		}
		String newSecurePassword = generateSecurePassword(providedPassword);
		return newSecurePassword.equalsIgnoreCase(securedPassword);
	}

}