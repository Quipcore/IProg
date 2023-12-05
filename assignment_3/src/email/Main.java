package email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {

    private static final int Port  = 587;
    private static final String SMTP  = "smtp.gmail.com";


    public static void main(String[] args) throws IOException, MessagingException {
        List<String> passwords = Files.readAllLines(Path.of("assignment_3/password/email")).stream().toList();
        final String username  = passwords.get(0);
        final String password = passwords.get(1);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP);
        properties.put("mail.smtp.port", Port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            InternetAddress[] addresses = {new InternetAddress(username)};

            Message message = new MimeMessage(session);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(username)
            );
            message.setSubject("Testing Gmail TLS");
            message.setText("Hello World!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
