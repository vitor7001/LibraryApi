package com.vitor.libraryapi.service.implementacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.vitor.libraryapi.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Value("${application.mail.default-remetent}")
	private String remetent;

	private final JavaMailSender mailSender;

	@Override
	public void sendMails(String message, List<String> mailsList) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(remetent);

		mailMessage.setSubject("Livro com empréstimo atrasado");

		mailMessage.setText(message);

		String[] mails = mailsList.toArray(new String[mailsList.size()]);

		mailMessage.setTo(mails);

		mailSender.send(mailMessage);
	}

}
