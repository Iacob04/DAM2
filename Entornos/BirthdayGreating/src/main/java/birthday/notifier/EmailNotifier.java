package birthday.notifier;

import birthday.model.Friend;
import birthday.model.Message;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailNotifier implements Notifier {

    private final Session session;
    private final String from;

    public EmailNotifier(String host, int port, String user, String password, String from) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        this.from = from;
    }

    @Override
    public void send(Friend recipient, Message message) {
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(from));
            mail.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(recipient.email()));
            mail.setSubject(message.subject(), "UTF-8");
            mail.setText(message.body(), "UTF-8");
            Transport.send(mail);
        } catch (MessagingException e) {
            throw new NotificationException("Error enviando correo a " + recipient.email(), e);
        }
    }
}
