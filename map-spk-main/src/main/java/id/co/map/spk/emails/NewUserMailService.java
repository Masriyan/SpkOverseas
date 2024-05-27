package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.model.Email;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.utils.AppProperty;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Awie on 11/4/2019
 */
public class NewUserMailService extends Thread{

    private AppProperty appProperty;
    private String activationKey;
    private String firstName;
    private String recipientEmail;
    private EmailService emailService;
    private String userName;

    public NewUserMailService(AppProperty appProperty, String activationKey, String firstName, String recipientEmail, EmailService emailService, String userName){

        this.appProperty = appProperty;
        this.activationKey = activationKey;
        this.firstName = firstName;
        this.recipientEmail = recipientEmail;
        this.emailService = emailService;
        this.userName = userName;
    }

    @Override
    public void run() {
        String subject = "SPK User Activation";
        String activationLink = appProperty.getBaseUrl()+"/users/activation?registrationKey="+activationKey;
        Email email = new Email();

        Map<String, Object> model = new HashMap<>();
        model.put("fistName", firstName);
        model.put("activationLink", activationLink);
        model.put("userName", userName);

        email.setMailTo(recipientEmail);
        email.setMailBcc(appProperty.getEmail());
        email.setMailFrom(appProperty.getEmail());
        email.setTemplate(EmailTemplate.NEW_USER.getMessage());
        email.setMailSubject(subject);
        email.setModel(model);

        try {
            emailService.sendEmail(email);
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
