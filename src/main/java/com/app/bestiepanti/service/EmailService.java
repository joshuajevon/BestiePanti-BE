package com.app.bestiepanti.service;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.auth.MailRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender javaMailSender;    
    private final Configuration freemarkerConfig;
    private final ApplicationConfig applicationConfig;

    public void sendEmailOtp(MailRequest mailBody, Map<String, Object> variables) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(mailBody.getTo());
        helper.setFrom(applicationConfig.getMailUsername());
        helper.setSubject(mailBody.getSubject());

        Template template = freemarkerConfig.getTemplate("email-otp-template.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);

        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendSuccessFundDonationDetails(MailRequest mailBody, Map<String, Object> variables, Boolean isDonatur) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(mailBody.getTo());
        helper.setFrom(applicationConfig.getMailUsername());
        helper.setSubject(mailBody.getSubject());

        Template template;
        if(isDonatur) template = freemarkerConfig.getTemplate("email-fund-donation-details.ftl");
        else template = freemarkerConfig.getTemplate("email-panti-fund-donation-details.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);

        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendSuccessNonFundDonationDetails(MailRequest mailBody, Map<String, Object> variables, Boolean isDonatur) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(mailBody.getTo());
        helper.setFrom(applicationConfig.getMailUsername());
        helper.setSubject(mailBody.getSubject());

        Template template;
        if(isDonatur) template = freemarkerConfig.getTemplate("email-nonfund-donation-details.ftl");
        else template = freemarkerConfig.getTemplate("email-panti-nonfund-donation-details.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);

        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendSuccessMessageDetails(MailRequest mailBody, Map<String, Object> variables, Boolean isDonatur) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(mailBody.getTo());
        helper.setFrom(applicationConfig.getMailUsername());
        helper.setSubject(mailBody.getSubject());

        Template template;
        if(isDonatur) template = freemarkerConfig.getTemplate("email-message-details.ftl");
        else template = freemarkerConfig.getTemplate("email-panti-message-details.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);

        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }
}   
