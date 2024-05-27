package id.co.map.spk.emails;

import freemarker.template.TemplateException;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.EmailTemplate;
import id.co.map.spk.model.Email;
import id.co.map.spk.repositories.AppUserRepository;
import id.co.map.spk.repositories.SpkNextRoleRepository;
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
 * @author Awie on 10/28/2019
 *
 * Send email notification to next user when Close Request Approved.
 */
public class CloseApprovedSpkMailService extends Thread {

    private List<ApprovalRulesEntity> closeRules;
    private AppUserEntity userApprove;
    private AppUserRepository appUserRepository;
    private SuratPerintahKerjaEntity spk;
    private AppProperty appProperty;
    private TextFormatter textFormatter;
    private VendorRepository vendorRepository;
    private StoreRepository storeRepository;
    private EmailService emailService;
    private UserRoleEntity userRole;
    private String spkNote;
    private SpkNextRoleRepository spkNextRoleRepository;

    public CloseApprovedSpkMailService (List<ApprovalRulesEntity> closeRules, AppUserEntity userApprove, AppUserRepository appUserRepository, SuratPerintahKerjaEntity spk, AppProperty appProperty, TextFormatter textFormatter, VendorRepository vendorRepository, StoreRepository storeRepository, EmailService emailService, UserRoleEntity userRole, String spkNote, SpkNextRoleRepository spkNextRoleRepository) {
        this.closeRules = closeRules;
        this.userApprove = userApprove;
        this.appUserRepository = appUserRepository;
        this.spk = spk;
        this.appProperty = appProperty;
        this.textFormatter = textFormatter;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.emailService = emailService;
        this.userRole = userRole;
        this.spkNote = spkNote;
        this.spkNextRoleRepository = spkNextRoleRepository;
    }

    @Override
    public void run() {

        VendorEntity vendor = vendorRepository.findByVendorIdAndCompanyId(spk.getVendorId(), spk.getCompanyId());
        StoreEntity store = storeRepository.findByStoreIdAndCompanyId(spk.getStoreId(), spk.getCompanyId());

        int currentApproval = closeRules.stream()
                .filter(r -> r.getRoleId().equals(userRole.getRoleId()))
                .mapToInt(ApprovalRulesEntity::getApprovalLevel)
                .findFirst()
                .getAsInt();

        ApprovalRulesEntity nextRule = closeRules.stream()
                .filter(r -> r.getApprovalLevel().equals(currentApproval + 1))
                .findFirst()
                .get();

        //update spk next role repository
        spkNextRoleRepository.update(spk.getSpkId(), nextRule.getRoleId());

        //List<AppUserEntity> users = appUserRepository.findByCompanyAndRole(spk.getCompanyId(), nextRule.getRoleId());
        List<AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), nextRule.getRoleId());

        String subject = "SPK - " + spk.getSpkDescription();
        String detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

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

        for(AppUserEntity user:users){

            Email email = new Email();
            String name = user.getFirstName();

            email.setMailTo(user.getEmail());
            //email.setMailBcc(appProperty.getEmail());
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailBcc("milton.santoso@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            email.setMailFrom("admin.invoice@map-vendors.com");
            email.setTemplate(EmailTemplate.APPROVED_CLOSE_SPK.getMessage());
            email.setMailSubject(subject);

            String detailLink2 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",CLOSE_APPROVED";

            model.put("detailLink2", detailLink2);
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
