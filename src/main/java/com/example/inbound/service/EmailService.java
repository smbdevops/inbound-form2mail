package com.example.inbound.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.List;

/**
 * Service to send Emails
 */
@Component
public final class EmailService {

    private static final String EMAIL_DEFAULT_FROM = "sales@example.com";
    private static final String UTF_8 = "UTF-8";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    @Qualifier("email_template_engine")
    private TemplateEngine templateEngine;

    public void sendTemplateEmail(final List<String> to, final String subject, final String templateFilename, final Context templateVariableContext) throws MessagingException {
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, UTF_8);
        message.setSubject(subject);
        message.setFrom(EMAIL_DEFAULT_FROM);
        to.forEach(to1 -> {
            try {
                message.addTo(to1);
            } catch (final MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        final String htmlContent = this.templateEngine.process(templateFilename, templateVariableContext);
        message.setText(htmlContent, true);
        this.emailSender.send(mimeMessage);
    }
}
