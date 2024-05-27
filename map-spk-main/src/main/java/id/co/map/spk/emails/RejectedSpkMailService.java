package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.model.Email;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.SpkHistoryRepository;
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
 * @author Awie on 9/9/2019
 */
public class RejectedSpkMailService extends Thread {

    private SuratPerintahKerjaEntity spk;
    private String rejectorName;
    private SpkHistoryRepository spkHistoryRepository;
    private AppUserRepository appUserRepository;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private EmailService emailService;
    private String spkNote;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;

    public RejectedSpkMailService(SuratPerintahKerjaEntity spk, String rejectorName, SpkHistoryRepository spkHistoryRepository,  AppUserRepository appUserRepository,
                                  AppProperty appProperty, TextFormatter textFormatter, EmailService emailService, String spkNote,
                                  VendorRepository vendorRepository, StoreRepository storeRepository){

        this.spk = spk;
        this.rejectorName = rejectorName;
        this.spkHistoryRepository = spkHistoryRepository;
        this.appUserRepository = appUserRepository;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.emailService = emailService;
        this.spkNote = spkNote;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public void run() {
        AppUserEntity rejector = appUserRepository.findByUsername(rejectorName);
        String subject = "SPK - " +spk.getSpkDescription();

        SpkHistoryEntity spkHistory = spkHistoryRepository.findBySpkId(spk.getSpkId())
                .stream()
                .filter(h -> h.getStatus().equals(SpkStatus.SUBMITTED))
                .findFirst()
                .get();

        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/edit/"+spk.getSpkId();
        AppUserEntity submitter = appUserRepository.findByUsername(spkHistory.getUserName());

        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());

        Map<String, Object> model = new HashMap<>();
        model.put("spkId", spk.getSpkId());
        model.put("spkDescription", spk.getSpkDescription());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("rejector", rejector.getFirstName());
        model.put("rejectorEmail", rejector.getEmail());
        model.put("detailLink", detailLink);
        model.put("spkNote", spkNote);

        Email email = new Email();

        email.setMailTo(submitter.getEmail());
        //email.setMailBcc(appProperty.getEmail());
        email.setMailBcc("Adminspk.Online@map.co.id");
        //email.setMailFrom(appProperty.getEmail());
        email.setMailFrom("admin.invoice@map-vendors.com");
        email.setTemplate(EmailTemplate.REJECTED_SPK.getMessage());
        email.setMailSubject(subject);

        model.put("name", submitter.getFirstName());

        email.setModel(model);

        try {
            emailService.sendEmail(email);
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
