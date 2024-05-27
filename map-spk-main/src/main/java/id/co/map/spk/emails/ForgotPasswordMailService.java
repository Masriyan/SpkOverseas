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
 * @author Awie on 3/3/2020
 */
public class ForgotPasswordMailService extends Thread{

    private AppProperty appProperty;
    private String forgotPasswordKey;
    private String firstName;
    private String recipientEmail;
    private EmailService emailService;
    private String userName;

    public ForgotPasswordMailService(AppProperty appProperty, String forgotPasswordKey, String firstName, String recipientEmail, EmailService emailService, String userName){

        this.appProperty = appProperty;
        this.forgotPasswordKey = forgotPasswordKey;
        this.firstName = firstName;
        this.recipientEmail = recipientEmail;
        this.emailService = emailService;
        this.userName = userName;

    }

    @Override
    public void run() {
        String subject = "SPK Account - Reset your password";
        String forgotPasswordLink = appProperty.getBaseUrl() + "/forgot-account-or-password?forgotPasswordKey=" + forgotPasswordKey;
        Email email = new Email();

        Map<String, Object> model = new HashMap<>();
        model.put("fistName", firstName);
        model.put("forgotPasswordLink", forgotPasswordLink);
        model.put("userName", userName);

        email.setMailTo(recipientEmail);
      //email.setMailBcc(appProperty.getEmail());
        email.setMailBcc("Adminspk.Online@map.co.id");
        email.setMailBcc("milton.santoso@map.co.id");
        //email.setMailFrom(appProperty.getEmail());
        email.setMailFrom("admin.invoice@map-vendors.com");
        email.setTemplate(EmailTemplate.FORGOT_PASSWORD.getMessage());
        email.setMailSubject(subject);
        email.setModel(model);

        try {
            emailService.sendEmail(email);
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
