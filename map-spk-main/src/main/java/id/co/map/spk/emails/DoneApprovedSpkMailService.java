package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.enums.PoType;
import id.co.map.spk.model.Email;
import id.co.map.spk.repositories.*;
import id.co.map.spk.services.ApprovalRulesService;
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
public class DoneApprovedSpkMailService extends Thread{

    private AppUserRepository appUserRepository;
    private UserRoleRepository userRoleRepository;
    private ApprovalRulesService approvalRulesService;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private EmailService emailService;
    private SuratPerintahKerjaEntity spk;
    private String username;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private SbuRepository sbuRepository;
    private SpkNextRoleRepository spkNextRoleRepository;

    public DoneApprovedSpkMailService (AppUserRepository appUserRepository, UserRoleRepository userRoleRepository, ApprovalRulesService approvalRulesService,
                                   AppProperty appProperty, TextFormatter textFormatter, EmailService emailService, SuratPerintahKerjaEntity spk,
                                   String username, VendorRepository vendorRepository, StoreRepository storeRepository,SbuRepository sbuRepository, SpkNextRoleRepository spkNextRoleRepository){
        this.appUserRepository = appUserRepository;
        this.userRoleRepository = userRoleRepository;
        this.approvalRulesService = approvalRulesService;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.emailService = emailService;
        this.spk = spk;
        this.username = username;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.sbuRepository = sbuRepository;
        this.spkNextRoleRepository = spkNextRoleRepository;
    }

    @Override
    public void run() {

        AppUserEntity approveUser = appUserRepository.findByUsername(username);
        UserRoleEntity userRole = userRoleRepository.findByUser(approveUser.getUserName()).stream().findFirst().get();

        List<ApprovalRulesEntity> rules = approvalRulesService.getRulesBySpk(spk, PoType.APPROVAL.name());
        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());
        SbuEntity sbu = sbuRepository.findBySbuId(spk.getSbuId());

        //update next role
        spkNextRoleRepository.update(spk.getSpkId(), 2);

        //List <AppUserEntity> users = appUserRepository.findByCompanyAndRole(spk.getCompanyId(), 2);
        List <AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), 2);

        String subject = "SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        Map<String, Object> model = new HashMap<>();
        model.put("spkId", spk.getSpkId());
        model.put("spkDescription", spk.getSpkDescription());
        model.put("approvedQuotationDate", textFormatter.toDateFormat(spk.getApprovedQuotationDate()));
        model.put("amount", textFormatter.toRupiahFormat(spk.getAmount()));
        model.put("sbuCode", sbu.getSbuCode());
        model.put("sbuDesc", sbu.getSbuDesc());
        model.put("vendorId", spk.getVendorId());
        model.put("vendorName", vendor.getVendorName());
        model.put("storeId", spk.getStoreId());
        model.put("storeName", store.getStoreName());
        model.put("approver", approveUser.getFirstName());
        model.put("approverEmail", approveUser.getEmail());
        model.put("detailLink", detailLink);

        for(AppUserEntity user:users){

            Email email = new Email();
            String name = user.getFirstName();

            email.setMailTo(user.getEmail());
            //email.setMailBcc(appProperty.getEmail());
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailBcc("milton.santoso@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            email.setMailFrom("admin.invoice@map-vendors.com");
            email.setTemplate(EmailTemplate.DONE_APPROVED_SPK.getMessage());
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