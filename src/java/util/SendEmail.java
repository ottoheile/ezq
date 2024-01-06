/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    
    public static void send(String playerEmail, String subject, String body){
      String to = playerEmail;

      String from = playerEmail;
      final String username = playerEmail.split("@")[0];
      final String password = "*******";// CHANGE ACCORDINGLY 

      String host = "smtp.kth.se";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
	   }
         });

      try {
	   Message message = new MimeMessage(session);
	
	   message.setFrom(new InternetAddress(from));
	
	   message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
	
	   message.setSubject(subject);
	
	   message.setText(body);

	   Transport.send(message);

	   System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
    }
}