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
import java.util.Map;

/**
 * @author Awie on 9/9/2019
 *
 * Notify spk's creator when internal order created.
 */
public class IoSpkMailService extends Thread {

    private AppUserRepository appUserRepository;
    private SuratPerintahKerjaEntity spk;
    private String verifierName;
    private SpkHistoryRepository spkHistoryRepository;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private EmailService emailService;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;

    public IoSpkMailService(AppUserRepository appUserRepository, SuratPerintahKerjaEntity spk, String verifierName, SpkHistoryRepository spkHistoryRepository, AppProperty appProperty, TextFormatter textFormatter, EmailService emailService, VendorRepository vendorRepository, StoreRepository storeRepository){
        this.appUserRepository = appUserRepository;
        this.spk = spk;
        this.verifierName = verifierName;
        this.spkHistoryRepository = spkHistoryRepository;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.emailService = emailService;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public void run() {

        SpkHistoryEntity spkHistory = spkHistoryRepository.findBySpkId(spk.getSpkId())
                .stream()
                .filter(h -> h.getStatus().equals(SpkStatus.SUBMITTED))
                .findFirst()
                .get();

        AppUserEntity creator = appUserRepository.findByUsername(spkHistory.getUserName());
        AppUserEntity verifier = appUserRepository.findByUsername(verifierName);
        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());

        String subject = "SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        Map<String, Object> model = new HashMap<>();
        model.put("spkId", spk.getSpkId());
        model.put("spkDescription", spk.getSpkDescription());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("verifier", verifier.getFirstName());
        model.put("verifierEmail", verifier.getEmail());
        model.put("detailLink", detailLink);
        model.put("assetNo", spk.getAssetNo());
        model.put("internalOrder", spk.getInternalOrder());
        model.put("name", creator.getFirstName());

        Email email = new Email();
        email.setMailTo(creator.getEmail());
        //email.setMailBcc(appProperty.getEmail());
        email.setMailBcc("Adminspk.Online@map.co.id");
        //email.setMailFrom(appProperty.getEmail());
        email.setMailFrom("admin.invoice@map-vendors.com");
        email.setTemplate(EmailTemplate.IO_CREATED_SPK.getMessage());
        email.setMailSubject(subject);
        email.setModel(model);

        try {
            emailService.sendEmail(email);
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
