package br.com.efo.bens.service.inter;

public interface MailService
{
	public void sendForgottenPasswordEmail(String email, String name);
	public void send(String from, String to, String subject, String text);
}
