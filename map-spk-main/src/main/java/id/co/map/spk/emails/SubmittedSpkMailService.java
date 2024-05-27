package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.model.Email;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.SbuRepository;
import id.co.map.spk.repositories.StoreRepository;
import id.co.map.spk.repositories.VendorRepository;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.services.impl.SpkServiceImpl;
import id.co.map.spk.utils.AppProperty;
import id.co.map.spk.utils.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmittedSpkMailService extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(SubmittedSpkMailService.class);
    private EmailService emailService;
    private TextFormatter textFormatter;
    private AppProperty appProperty;
    private AppUserRepository appUserRepository;
    private SuratPerintahKerjaEntity spk;
    private String username;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private SbuRepository sbuRepository;
    private ApprovalRulesEntity rule;

    public SubmittedSpkMailService(EmailService emailService, TextFormatter textFormatter, AppProperty appProperty,  AppUserRepository appUserRepository, SuratPerintahKerjaEntity spk, String username, VendorRepository vendorRepository, StoreRepository storeRepository,SbuRepository sbuRepository, ApprovalRulesEntity rule){
        this.emailService = emailService;
        this.textFormatter = textFormatter;
        this.appProperty = appProperty;
        this.appUserRepository = appUserRepository;
        this.spk = spk;
        this.username = username;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.sbuRepository = sbuRepository;
        this.rule = rule;
    }

    @Override
    public void run() {

        AppUserEntity submitterObj = appUserRepository.findByUsername(username);

        List<AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), rule.getRoleId());
        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());
        SbuEntity sbu = sbuRepository.findBySbuId(spk.getSbuId());

        String subject = "SPK - " + spk.getSpkDescription();
        //String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        Map<String, Object> model = new HashMap<>();
        model.put("spkDescription", spk.getSpkDescription());
        model.put("spkId", spk.getSpkId());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("sbuCode", sbu.getSbuCode());
        model.put("sbuDesc", sbu.getSbuDesc());
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("submitter", submitterObj.getFirstName());
        model.put("submitterEmail", submitterObj.getEmail());
        //model.put("detailLink", detailLink);

        for(AppUserEntity user:users){

            String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();
            String detailLink1 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",APPROVED";
            String detailLink2 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",REJECTED";
            Email email = new Email();

            email.setMailTo(user.getEmail());
            //email.setMailTo("Bartholomeus.B@map.co.id");
            //email.setMailBcc(appProperty.getEmail());
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            email.setMailFrom("Auto Mail SPK Online <admin.invoice@map-vendors.com>");
            email.setTemplate(EmailTemplate.SUBMITTED_SPK.getMessage());
            email.setMailSubject(subject);

            model.put("name", user.getFirstName());
            model.put("detailLink", detailLink);
            model.put("detailLink1", detailLink1);
            model.put("detailLink2", detailLink2);

            email.setModel(model);

            try {
                logger.info("Send Email");
                emailService.sendEmail(email);
                logger.info("Sending Email Done");
            } catch (MessagingException | TemplateException | IOException e) {
                e.printStackTrace();
            }
            //break; //for test
        }
    }
}
