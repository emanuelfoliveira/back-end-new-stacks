package br.com.efo.bens.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.efo.bens.service.inter.MailService;

@Service("mailService")
public class MailServiceImpl implements MailService
{
	private static final String URL_RESET_PASSWORD = "https://XXX.XXX.XXX";
	
	private static final String SENDER_FROM_NO_REPLY = "no-reply@XXX.com";
	private static final String FORGOTTEN_PSW_SUBJECT = "BENS - Password Change";
	
    @Autowired
    public JavaMailSender mailSender;

    @Override
    public void send(String from, String to, String subject, String text) {
    	
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
	    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(text, "text/html");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(SENDER_FROM_NO_REPLY, SENDER_FROM_NO_REPLY);
			helper.setSentDate(new Date());

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void sendForgottenPasswordEmail(String name, String email) {
		String template = getTemplateForgottenPsw(name, email);
		send(SENDER_FROM_NO_REPLY, email, FORGOTTEN_PSW_SUBJECT, template);
	}
	
	@SuppressWarnings("unused")
	private String getTemplateForgottenPsw(String name, String email) {
		String link = "<a href='" + URL_RESET_PASSWORD.concat(email) + "'>";
		
		StringBuilder sb = new StringBuilder();
		sb.append("TO IMPLEMENT");
		return sb.toString();
	}
	
}
