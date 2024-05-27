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
 * @author Awie on 11/19/2019
 */
public class SpkCloseFailedMailService extends Thread {

    private SuratPerintahKerjaEntity spk;
    private AppUserEntity userApprove;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private AppUserRepository appUserRepository;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private SpkHistoryRepository spkHistoryRepository;
    private EmailService emailService;

    public SpkCloseFailedMailService(SuratPerintahKerjaEntity spk, AppUserEntity userApprove, VendorRepository vendorRepository, StoreRepository storeRepository, AppUserRepository appUserRepository, AppProperty appProperty, TextFormatter textFormatter, SpkHistoryRepository spkHistoryRepository,EmailService emailService){
        this.spk = spk;
        this.userApprove = userApprove;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.appUserRepository = appUserRepository;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.spkHistoryRepository = spkHistoryRepository;
        this.emailService = emailService;
    }

    @Override
    public void run() {

        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());
        //List<AppUserEntity> users = appUserRepository.findByCompanyAndRole(spk.getCompanyId(), 2);
        List<AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), 2);
        String subject = "[ERROR] SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        List<SpkHistoryEntity> histories = spkHistoryRepository.findBySpkId(spk.getSpkId());
        String spkNote = histories.stream()
                .filter(h -> h.getStatus().equals(SpkStatus.CLOSE_REQUEST))
                .findFirst()
                .get()
                .getSpkNote();

        String errorMessage = histories.stream()
                .filter(h -> h.getStatus().equals(SpkStatus.INTERNAL_ORDER_CLOSE_FAILED))
                .findFirst()
                .get()
                .getSpkNote();

        Map<String, Object> model = new HashMap<>();
        model.put("spkId", spk.getSpkId());
        model.put("spkDescription", spk.getSpkDescription());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("closeAmount", textFormatter.toRupiahFormat(spk.getActualAmount()));
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("approver", userApprove.getFirstName());
        model.put("approverEmail", userApprove.getEmail());
        model.put("detailLink", detailLink);
        model.put("spkNote", spkNote);
        model.put("errorMessage", errorMessage);

        for(AppUserEntity user:users){

            Email email = new Email();
            String name = user.getFirstName();

            email.setMailTo(user.getEmail());
            //email.setMailBcc(appProperty.getEmail());
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            email.setMailFrom("admin.invoice@map-vendors.com");
            email.setTemplate(EmailTemplate.CLOSED_FAILED.getMessage());
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
