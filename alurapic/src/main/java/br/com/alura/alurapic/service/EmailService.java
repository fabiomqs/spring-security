package br.com.alura.alurapic.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {

    @Value("${mail.smtp}")
    private String SIMPLE_MAIL_TRANSFER_PROTOCOL;
    @Value("${mail.server}")
    private String GMAIL_SMTP_SERVER;
    @Value("${mail.username}")
    private String USERNAME;
    @Value("${mail.password}")
    private String PASSWORD;
    @Value("${mail.from}")
    private String FROM_EMAIL;
    private String CC_EMAIL = "";
    @Value("${mail.subject}")
    private String EMAIL_SUBJECT;

    @Value("${mail.host}")
    private String SMTP_HOST;
    @Value("${mail.auth}")
    private String SMTP_AUTH;
    @Value("${mail.port}")
    private String SMTP_PORT;
    @Value("${mail.default}")
    private String DEFAULT_PORT;
    @Value("${mail.startttls}")
    private String SMTP_STARTTLS_ENABLE;
    @Value("${mail.required}")
    private String SMTP_STARTTLS_REQUIRED;

    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String firstName, String password, String email)
            throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + firstName +
                ", \n \n Your new account password is: " +
                password +
                "\n \n The Support Team");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}
