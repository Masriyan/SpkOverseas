package id.co.map.spk.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import id.co.map.spk.model.Email;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final Configuration freeMarkerConfiguration;

    public EmailService(JavaMailSender emailSender, Configuration freeMarkerConfiguration) {
        this.emailSender = emailSender;
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    public void sendEmail(Email email) throws MessagingException, IOException, TemplateException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                email.getContentType());

        freeMarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/emails");
        Template t = freeMarkerConfiguration.getTemplate(email.getTemplate());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, email.getModel());

        helper.setTo(email.getMailTo());
        //helper.setCc(email.getMailFrom());
        //helper.setBcc(email.getMailFrom());
        helper.setCc("Adminspk.Online@map.co.id");
        helper.setBcc("Adminspk.Online@map.co.id");
        helper.setText(html, true);
        helper.setSubject(email.getMailSubject());
        //helper.setFrom(email.getMailFrom());
        helper.setFrom("Auto Mail SPK Online <admin.invoice@map-vendors.com>");

        emailSender.send(message);
    }

}
