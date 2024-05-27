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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 11/19/2019
 */
public class SpkClosedMailService extends Thread {

    private SuratPerintahKerjaEntity spk;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private SpkHistoryRepository spkHistoryRepository;
    private AppUserRepository appUserRepository;
    private EmailService emailService;

    public SpkClosedMailService(SuratPerintahKerjaEntity spk, VendorRepository vendorRepository, StoreRepository storeRepository, AppProperty appProperty, TextFormatter textFormatter, SpkHistoryRepository spkHistoryRepository, AppUserRepository appUserRepository, EmailService emailService){

        this.spk = spk;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.spkHistoryRepository = spkHistoryRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
    }

    @Override
    public void run(){
        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());

        List<SpkHistoryEntity> histories = spkHistoryRepository.findBySpkId(spk.getSpkId());
        String spkNote = histories.stream()
                .filter(h -> h.getStatus().equals(SpkStatus.CLOSE_REQUEST))
                .findFirst()
                .get()
                .getSpkNote();

        //get accountant list
        //List<AppUserEntity> users = appUserRepository.findByCompanyAndRole(spk.getCompanyId(), 12);
        List<AppUserEntity> users = new ArrayList<>();
        if(spk.getSpkType().equalsIgnoreCase("Asset")) {
            users = appUserRepository.findBySbuAndRole(spk.getSbuId(), 12);
        }

        String subject = "[CLOSED] SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        Map<String, Object> model = new HashMap<>();
        model.put("spkDescription", spk.getSpkDescription());
        model.put("spkId", spk.getSpkId());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("closedAmount", textFormatter.toRupiahFormat(spk.getActualAmount()));
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
            email.setTemplate(EmailTemplate.CLOSED_SPK.getMessage());
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
