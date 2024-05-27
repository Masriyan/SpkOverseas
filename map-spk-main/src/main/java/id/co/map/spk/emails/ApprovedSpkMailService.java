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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 9/9/2019
 */
public class ApprovedSpkMailService extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(ApprovedSpkMailService.class);
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

    private CompanyRepository companyRepository;

    public ApprovedSpkMailService (AppUserRepository appUserRepository, UserRoleRepository userRoleRepository, ApprovalRulesService approvalRulesService,
                                   AppProperty appProperty, TextFormatter textFormatter, EmailService emailService, SuratPerintahKerjaEntity spk,
                                   String username, VendorRepository vendorRepository, StoreRepository storeRepository,SbuRepository sbuRepository, SpkNextRoleRepository spkNextRoleRepository,CompanyRepository companyRepository){
        this.appUserRepository = appUserRepository;
        this.companyRepository = companyRepository;
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

        int currentApproval = rules.stream()
                .filter(r -> r.getRoleId().equals(userRole.getRoleId()))
                .mapToInt(ApprovalRulesEntity::getApprovalLevel)
                .findFirst()
                .getAsInt();

        //get by country id
        Integer countryId = companyRepository.findCountryByCompanyId(spk.getCompanyId()).getCountryId();

        ApprovalRulesEntity nextRule = rules.stream()
                .filter(r -> r.getApprovalLevel().equals(currentApproval + 1))
                .filter(r -> r.getCountryId().equals(countryId))
                .findFirst()
                .get();

        //update next role
        spkNextRoleRepository.update(spk.getSpkId(), nextRule.getRoleId());
        List<AppUserEntity> users = appUserRepository.findBySbuAndRole(spk.getSbuId(), nextRule.getRoleId());

        String subject = "SPK - " + spk.getSpkDescription();

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
        String tipe,detailLink,detailLink1,detailLink2 = "";
        detailLink = appProperty.getBaseUrl()+"/surat-perintah-kerja/"+spk.getSpkId();

        logger.debug("spk Type = "+spk.getSpkType());
        logger.debug("next Rule = "+nextRule.getRoleId());

        for(AppUserEntity user:users){

            detailLink2 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",REJECTED";

            if(spk.getSpkType().equals("Asset"))
            {
                if(nextRule.getRoleId() == 12)
                {
                    tipe = "a";
                    detailLink1 = "";
                }
                else
                {
                    tipe = "c";
                    detailLink1 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",APPROVED";
                }
            }
            else
            {
                ApprovalRulesEntity nextRule2 = rules.stream()
                        .filter(r -> r.getApprovalLevel().equals(currentApproval + 2))
                        .findFirst()
                        .get();

                if(nextRule2.getRoleId() == 12)
                {
                    tipe = "b";
                    detailLink1 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",DONE_APPROVED";
                }
                else
                {
                    tipe = "c";
                    detailLink1 = appProperty.getBaseUrl()+"/surat-perintah-kerja/upd/"+user.getUserName().replace('.','^')+","+spk.getSpkId()+",APPROVED";
                }

            }

            logger.debug("tipe = "+tipe);
            model.put("tipe", tipe);

            Email email = new Email();
            String name = user.getFirstName();

            email.setMailTo(user.getEmail());
            //email.setMailTo("Bartholomeus.B@map.co.id");
            //email.setMailBcc(appProperty.getEmail());
            //email.setMailCc("Adminspk.Online@map.co.id");
            email.setMailBcc("Adminspk.Online@map.co.id");
            //email.setMailFrom(appProperty.getEmail());
            //email.setMailFrom("admin.invoice@map-vendors.com");
            email.setMailFrom("Auto Mail SPK Online <admin.invoice@map-vendors.com>");
            email.setTemplate(EmailTemplate.APPROVED_SPK.getMessage());
            email.setMailSubject(subject);

            model.put("name", name);
            model.put("detailLink", detailLink);
            model.put("detailLink1", detailLink1);
            model.put("detailLink2", detailLink2);
            email.setModel(model);

            try {
                emailService.sendEmail(email);
            } catch (MessagingException | TemplateException | IOException e) {
                e.printStackTrace();
            }

            //break; //for test
        }
    }
}