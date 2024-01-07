package util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    public static void sendEmail(String toEmail, String subject, String body) {
        try {
            Properties properties = new Properties();
            properties.load(SendEmail.class.getClassLoader().getResourceAsStream("conf/credentials/email.properties"));
            String mailServer = properties.getProperty("mail-server");
            String fromEmail = properties.getProperty("email");
            String password = properties.getProperty("password");
            boolean authWithDomain = Boolean.parseBoolean(properties.getProperty("auth-with-domain"));

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", mailServer);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authWithDomain ? fromEmail : fromEmail.split("@")[0], password);
                    }
                });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));

            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));

            message.setSubject(subject);

            message.setText(body);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
