package vn.ptit.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import vn.ptit.models.CreditAccount;
import vn.ptit.models.Customer;
import vn.ptit.models.DepositAccount;
import vn.ptit.models.Transaction;

@Service
public class SendMailService {
	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void sendMailPayment(Transaction transaction, Customer customerOtherPay) throws MessagingException {
		Context ctx = new Context();
		ctx.setVariable("transaction", transaction);
		ctx.setVariable("customerOtherPay", customerOtherPay);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("THANH TOÁN THÀNH CÔNG");
		message.setFrom("actbankptit999@gmail.com");
		String[] to = { transaction.getCustomer().getEmail(), customerOtherPay.getEmail() };
		message.setTo(to);
//		message.addAttachment("logo.png", new ClassPathResource("static/img/logo.png"));

		String htmlContent = templateEngine.process("send_mail/payment.html", ctx);
		message.setText(htmlContent, true); // true = isHtml

		javaMailSender.send(mimeMessage);
	}

	public void sendMailDeal(Transaction transaction) throws MessagingException {
		Context ctx = new Context();
		ctx.setVariable("transaction", transaction);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("GIAO DỊCH THÀNH CÔNG");
		message.setFrom("actbankptit999@gmail.com");
		message.setTo(transaction.getCustomer().getEmail());
//		message.addAttachment("logo.png", new ClassPathResource("static/img/logo.png"));

		String htmlContent = templateEngine.process("send_mail/deal.html", ctx);
		message.setText(htmlContent, true); // true = isHtml

		javaMailSender.send(mimeMessage);
	}

	public void sendMailDeposit(Transaction transaction) throws MessagingException {
		Context ctx = new Context();
		ctx.setVariable("transaction", transaction);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("GỬI TIỀN THÀNH CÔNG");
		message.setFrom("actbankptit999@gmail.com");
		message.setTo(transaction.getCustomer().getEmail());
//		message.addAttachment("logo.png", new ClassPathResource("static/img/logo.png"));

		String htmlContent = templateEngine.process("send_mail/deposit.html", ctx);
		message.setText(htmlContent, true); // true = isHtml

		// Send mail
		javaMailSender.send(mimeMessage);
	}

	public void sendMailCreateAccount(DepositAccount depositAccount, CreditAccount creditAccount, Customer customer)
			throws MessagingException {
		Context ctx = new Context();
		if (depositAccount != null) {
			ctx.setVariable("depositAccount", depositAccount);
		} else {
			ctx.setVariable("creditAccount", creditAccount);
		}

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("ĐĂNG KÝ THÀNH CÔNG");
		message.setFrom("actbankptit999@gmail.com");
		message.setTo(customer.getEmail());
//		message.addAttachment("logo.png", new ClassPathResource("static/img/logo.png"));

		String htmlContent = templateEngine.process("send_mail/signup.html", ctx);
		message.setText(htmlContent, true); // true = isHtml

		javaMailSender.send(mimeMessage);
	}

}
