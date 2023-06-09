package com.fluffy.universe.services;

import com.fluffy.universe.models.User;
import com.fluffy.universe.utils.Configuration;
import com.google.common.io.Resources;
import org.apache.commons.text.StringSubstitutor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public final class MailService {
    private MailService() {}

    private static final String mailFrom;
    private static final Session session;

    static {
        mailFrom = Configuration.get("mail.from");
        Properties properties = new Properties();
        properties.put("mail.smtp.host", Configuration.get("mail.host"));
        properties.put("mail.smtp.port", Configuration.get("mail.port"));
        properties.put("mail.smtp.ssl", Configuration.get("mail.ssl"));
        properties.put("mail.smtp.auth", Configuration.get("mail.auth"));
        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Configuration.get("mail.user"), Configuration.get("mail.password"));
            }
        });
    }

    public static void sendResetLink(User user) throws MessagingException {
        String url = Configuration.get("application.url");
        String token = user.getResetPasswordToken();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailFrom));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(user.getEmail()));
        message.setSubject("Password Reset");
        message.setContent(render("password-reset", Map.of("firstName", user.getFirstName(), "url", url, "token", token)));
        Transport.send(message);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static String getResource(String resource) {
        try {
            return Resources.toString(Resources.getResource(resource), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            return null;
        }
    }

    private static Multipart render(String template, Map<String, String> values) throws MessagingException {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(values);
        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart textPart = new MimeBodyPart();
        String text = getResource("mails/" + template + ".txt");
        if (text != null) {
            textPart.setText(stringSubstitutor.replace(text), "utf-8");
            multipart.addBodyPart(textPart);
        }

        MimeBodyPart htmlPart = new MimeBodyPart();
        String html = getResource("mails/" + template + ".html");
        if (html != null) {
            htmlPart.setContent(stringSubstitutor.replace(html), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
        }

        return multipart;
    }
}
