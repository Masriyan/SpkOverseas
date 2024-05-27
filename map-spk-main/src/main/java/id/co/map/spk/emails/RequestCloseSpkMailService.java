package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.model.Email;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.StoreRepository;
import id.co.map.spk.repositories.VendorRepository;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.utils.AppProperty;
import id.co.map.spk.utils.TextFormatter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 10/24/2019
 */
public class RequestCloseSpkMailService extends Thread {

    private SuratPerintahKerjaEntity spk;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private AppUserRepository appUserRepository;
    private AppProperty appProperty;
    private String requestorUsername;
    private TextFormatter textFormatter;
    private String spkNote;
    private EmailService emailService;
    private ApprovalRulesEntity approvalRulesEntity;

    public RequestCloseSpkMailService(SuratPerintahKerjaEntity spk, VendorRepository vendorRepository,
                                      StoreRepository storeRepository,
                                      AppUserRepository appUserRepository,
                                      AppProperty appProperty,
                                      String requestorUsername,
                                      TextFormatter textFormatter,
                                      String spkNote,
                                      EmailService emailService,
                                      ApprovalRulesEntity approvalRulesEntity){
        this.spk = spk;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.appUserRepository = appUserRepository;
        this.appProperty = appProperty;
        this.requestorUsername = requestorUsername;
        this.textFormatter = textFormatter;
        this.spkNote = spkNote;
        this.emailService = emailService;
        this.approvalRulesEntity = approvalRulesEntity;
    }

    @Override
    public void run() {

        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());

        //List<AppUserEntity> users = appUserRepository.findByCompanyAndRole(spk.getCompanyId(), approvalRulesEntity.getRoleId());
        List<AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), approvalRulesEntity.getRoleId());
        AppUserEntity requestor = appUserRepository.findByUsername(requestorUsername);

        String subject = "SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        Map<String, Object> model = new HashMap<>();
        model.put("requestor", requestor.getFirstName());
        model.put("requestorEmail", requestor.getEmail());
        model.put("spkDescription", spk.getSpkDescription());
        model.put("spkId", spk.getSpkId());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("closeAmount", textFormatter.toRupiahFormat(spk.getActualAmount()));
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("spkNote", spkNote);
        model.put("detailLink", detailLink);

        for(AppUserEntity user:users){
            Email email = new Email();
            String name = user.getFirstName();

            email.setMailTo(user.getEmail());
            //email.setMailBcc(appProperty.getEmail());
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            email.setMailFrom("admin.invoice@map-vendors.com");
            email.setTemplate(EmailTemplate.REQUEST_CLOSE_SPK.getMessage());
            email.setMailSubject(subject);

            model.put("name", name);
            email.setModel(model);

            try {
                emailService.sendEmail(email);
            } catch (MessagingException | TemplateException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
