package id.co.map.spk.services.impl;

import id.co.map.spk.counters.SpkMonthlyCounter;
import id.co.map.spk.emails.*;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.PoType;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.model.response.ClientSpkResponse;
import id.co.map.spk.repositories.*;
import id.co.map.spk.services.ApprovalRulesService;
import id.co.map.spk.services.EmailService;
import id.co.map.spk.services.InternalOrderService;
import id.co.map.spk.services.SpkService;
import id.co.map.spk.utils.AppProperty;
import id.co.map.spk.utils.ExcelExporter;
import id.co.map.spk.utils.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpkServiceImpl implements SpkService {

    private static final Logger logger = LoggerFactory.getLogger(SpkServiceImpl.class);
    private final SpkRepository spkRepository;
    private final CompanyRepository companyRepository;
    private final SbuRepository sbuRepository;
    private final EmailService emailService;
    private final AppUserRepository appUserRepository;
    private final AppProperty appProperty;
    private final AssetClassRepository assetClassRepository;
    private final ApprovalRulesService approvalRulesService;
    private final TextFormatter textFormatter;
    private final UserRoleRepository userRoleRepository;
    private final VendorRepository vendorRepository;
    private final StoreRepository storeRepository;
    private final SpkNextRoleRepository spkNextRoleRepository;
    private final InternalOrderService internalOrderService;
    private final SpkHistoryRepository spkHistoryRepository;

    public SpkServiceImpl(SpkRepository spkRepository, CompanyRepository companyRepository,SbuRepository sbuRepository, EmailService emailService, AppUserRepository appUserRepository, AppProperty appProperty, AssetClassRepository assetClassRepository, ApprovalRulesService approvalRulesService, TextFormatter textFormatter, UserRoleRepository userRoleRepository, VendorRepository vendorRepository, StoreRepository storeRepository, SpkNextRoleRepository spkNextRoleRepository, InternalOrderService internalOrderService, SpkHistoryRepository spkHistoryRepository) {
        this.spkRepository = spkRepository;
        this.companyRepository = companyRepository;
        this.sbuRepository = sbuRepository;
        this.emailService = emailService;
        this.appUserRepository = appUserRepository;
        this.appProperty = appProperty;
        this.assetClassRepository = assetClassRepository;
        this.approvalRulesService = approvalRulesService;
        this.textFormatter = textFormatter;
        this.userRoleRepository = userRoleRepository;
        this.vendorRepository = vendorRepository;
        this.storeRepository = storeRepository;
        this.spkNextRoleRepository = spkNextRoleRepository;
        this.internalOrderService = internalOrderService;
        this.spkHistoryRepository = spkHistoryRepository;
    }

    @Override
    public ClientSpkResponse addSpk(SuratPerintahKerjaEntity spk, String userName) {

        ClientSpkResponse response = new ClientSpkResponse();
        ApprovalRulesEntity rule = new ApprovalRulesEntity();
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDt = sdf.format(new Date());
            String spkId = generateSpkId(spk.getStoreId(), spk.getVendorId(), spk.getApprovedQuotationDate());
            CompanyEntity company = companyRepository.findByCompanyId(spk.getCompanyId());

            spk.setSpkId(spkId);
            spk.setProcessingGroup(company.getProcessingGroup());
            spk.setCoa(company.getCoa());
            spk.setCreatedTimeStamp(currentDt);
            spk.setDataTimeStamp(currentDt);
            spk.setStatus(SpkStatus.SUBMITTED);

            //add spk to db
            spkRepository.add(spk, userName);

            //get by country id
            Integer countryId = companyRepository.findCountryByCompanyId(spk.getCompanyId()).getCountryId();
            //get rule

            logger.debug("CountryID       : " +countryId);
            logger.debug("getCompanyId       : " +spk.getCompanyId());
            List<ApprovalRulesEntity> rules = approvalRulesService.getRulesBySpk(spk, PoType.APPROVAL.name());

            rules = rules.stream()
                    .filter(r -> r.getApprovalLevel().equals(1))
                    .collect(Collectors.toList())
            ;

            if(rules.size() > 0){
                rule = rules.stream().findFirst().get();
            } else{
                //Please check your asset type
                response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                response.setResponseMessage("Please check your asset type");
                logger.error("Please check your asset type");
                return response;
            }


            //insert into spk next role table
            spkNextRoleRepository.insert(spkId, rule.getRoleId());


            //send email notification to approver.

            Thread submittedMail = new Thread(
                    new SubmittedSpkMailService(emailService, textFormatter, appProperty, appUserRepository, spk, userName, vendorRepository, storeRepository, sbuRepository, rule));
            submittedMail.start();

            response.setResponseCode(ClientResponseStatus.SPK_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ADDED.getMessage());
            response.setSuratPerintahKerja(spk);

        }
        catch (ClassNotFoundException ce){
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage()+ " " + ce.getMessage());
            logger.error(ce.getMessage());
        }
        catch (SQLException se){
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " + se.getMessage());
            logger.error(se.getMessage());
        }

        return response;
    }

    @Override
    public ClientSpkResponse editSpk(SuratPerintahKerjaEntity spk, String userName) {

        ClientSpkResponse response = new ClientSpkResponse();
        ApprovalRulesEntity rule = new ApprovalRulesEntity();
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDt = sdf.format(new Date());
            CompanyEntity company = companyRepository.findByCompanyId(spk.getCompanyId());
            String spkId = spk.getSpkId();

            spk.setProcessingGroup(company.getProcessingGroup());
            spk.setCoa(company.getCoa());
            spk.setCreatedTimeStamp(currentDt);
            spk.setDataTimeStamp(currentDt);
            spk.setStatus(SpkStatus.UPDATED);

            //add spk to db
            spkRepository.updte(spk, userName);



            List<ApprovalRulesEntity> rules = approvalRulesService.getRulesBySpk(spk, PoType.APPROVAL.name());

            rules = rules.stream()
                    .filter(r -> r.getApprovalLevel().equals(1))
                    .collect(Collectors.toList())
            ;

            if(rules.size() > 0){
                rule = rules.stream().findFirst().get();
            } else{
                //Please check your asset type
                response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
                response.setResponseMessage("Please check your asset type");
                logger.error("Please check your asset type");
                return response;
            }


            //insert into spknextrole table
            spkNextRoleRepository.insert(spkId, rule.getRoleId());


            //send email notification to approver.
            Thread revisedMail = new Thread(
                    new SubmittedSpkMailService(emailService, textFormatter, appProperty, appUserRepository, spk, userName, vendorRepository, storeRepository, sbuRepository, rule));
            revisedMail.start();

            response.setResponseCode(ClientResponseStatus.SPK_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_UPDATED.getMessage());
            response.setSuratPerintahKerja(spk);

        }
        catch (ClassNotFoundException ce){
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage()+ " " + ce.getMessage());
            logger.error(ce.getMessage());
        }
        catch (SQLException se){
            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " + se.getMessage());
            logger.error(se.getMessage());
        }

        return response;
    }

    @Override
    public ClientSpkResponse updateSpkStatus(SpkHistoryEntity spkHistory) {
        ClientSpkResponse response = new ClientSpkResponse();
        SuratPerintahKerjaEntity spk = spkRepository.findById(spkHistory.getSpkId());

        switch (spkHistory.getStatus()){
            case CANCELED:
                response = updateSpkToCanceled(spkHistory, spk);
                break;
            case DONE_APPROVED:
                response = updateSpkDoneApproved(spkHistory, spk);
                break;
            case APPROVED:
                response = updateSpkToApproved(spkHistory, spk);
                break;
            case REJECTED:
                response = updateSpkToRejected(spkHistory, spk);
                break;
            case VERIFIED:
                response = updateSpkToVerified(spkHistory, spk);
                break;
            case CLOSE_REQUEST:
                response = updateSpkToRequestClose(spkHistory, spk);
                break;
            case CLOSE_APPROVED:
                response = updateSpkToCloseApproved(spkHistory, spk);
                break;
            case UPDATE_ASSET_CLASS_ID:
                response = updateSpkAssetClassId(spkHistory, spk);
                break;
        }
        return response;
    }

    @Override
    public Map<String, Object> findSpkPagination(String username, String spkId, String spkDescription, String companyId, String sbuId,  String agreementDate, String storeId, String vendorId, String status, Integer pageNumber, Integer size, Integer draw) {

        Map<String, Object> findResult = spkRepository.findSpkForPagination(username, spkId, spkDescription, companyId, sbuId, agreementDate, storeId,vendorId, status, pageNumber, size);
        Map<String, Object> map = new HashMap<>();
        map.put("draw", draw);
        map.put("recordsTotal", findResult.get("totalCount"));
        map.put("recordsFiltered", findResult.get("totalCount"));
        map.put("data", findResult.get("spkList"));

        return map;
    }

    @Override
    public void createExc(HttpServletResponse response, String username, String spkId, String spkDescription, String companyId, String sbuId, String agreementDate, String storeId, String vendorId, String status) throws IOException {
        try {
            logger.info("====================== Create Excel Service ============================");
            List<SuratPerintahKerjaEntity> listSpk = spkRepository.findListSpkforExcel(username, spkId, spkDescription, companyId, sbuId, agreementDate, storeId,vendorId, status);

            logger.info("Count List in Service SPK = " + listSpk.size());

            ExcelExporter exporter = new ExcelExporter(listSpk);
            exporter.export(response);
            logger.info("====================== Create Excel Service ============================");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private ClientSpkResponse updateSpkToVerified(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {

        ClientSpkResponse response = new ClientSpkResponse();
        spkHistory.setSpkNote("Asset Number changed to " + spkHistory.getAssetNo());

        if(spk.getStatus().equals(SpkStatus.CANCELED)){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_CANCELED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_CANCELED.getMessage());
            response.setSuratPerintahKerja(spk);
        }
        else if(spk.getStatus().equals(SpkStatus.REJECTED)){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_REJECTED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_REJECTED.getMessage());
            response.setSuratPerintahKerja(spk);
        }
        else{
            spk.setAssetNo(spkHistory.getAssetNo());
            response = updateSpkStatusAndAssetNoToDb(spkHistory, spk);
        }

        return  response;
    }

    private ClientSpkResponse updateSpkToRejected(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {
        ClientSpkResponse response = new ClientSpkResponse();

        if(spk.getStatus() == SpkStatus.REJECTED){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_REJECTED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_REJECTED.getMessage());
            response.setSuratPerintahKerja(spk);
        } else{
            response = updateSpkStatusToDb(spkHistory, spk);

            spkNextRoleRepository.update(spkHistory.getSpkId(), 0);

            //send rejected email notification to previous user
            Thread rejectedSpkMailService = new Thread(
                    new RejectedSpkMailService(spk, spkHistory.getUserName(), spkHistoryRepository, appUserRepository, appProperty, textFormatter, emailService, spkHistory.getSpkNote(), vendorRepository, storeRepository)
            );
            rejectedSpkMailService.start();
        }

        return  response;
    }

    private ClientSpkResponse updateSpkToCanceled(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk){
        ClientSpkResponse response = new ClientSpkResponse();

        if(spk.getStatus() != SpkStatus.SUBMITTED){
            response.setResponseCode(ClientResponseStatus.SPK_ERROR_CANCELED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ERROR_CANCELED.getMessage());
            response.setSuratPerintahKerja(spk);

        }else{
            response = updateSpkStatusToDb(spkHistory, spk);

            //udpate spknextrole to 0
            spkNextRoleRepository.update(spkHistory.getSpkId(), 0);
        }
        return response;
    }

    private ClientSpkResponse updateSpkToApproved(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {

        ClientSpkResponse response = new ClientSpkResponse();

        if(spk.getStatus() == SpkStatus.CANCELED){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_CANCELED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_CANCELED.getMessage());
            response.setSuratPerintahKerja(spk);
        }else if(spk.getStatus() == SpkStatus.REJECTED){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_REJECTED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_REJECTED.getMessage());
            response.setSuratPerintahKerja(spk);
        }else{
            boolean isAprOnce = isApprovOnce(spkHistory);
            if(isAprOnce){
                response.setResponseCode(ClientResponseStatus.SPK_HAS_APPROVED.getCode());
                response.setResponseMessage(ClientResponseStatus.SPK_HAS_APPROVED.getMessage());
                response.setSuratPerintahKerja(spk);
                return  response;
            }

            response = updateSpkStatusToDb(spkHistory, spk);

            //send email notification to next approver or finance.
            Thread approvedSpkMailService = new Thread(
                    new ApprovedSpkMailService(appUserRepository, userRoleRepository, approvalRulesService, appProperty, textFormatter, emailService, spk, spkHistory.getUserName(), vendorRepository, storeRepository,sbuRepository, spkNextRoleRepository , companyRepository)
            );
            approvedSpkMailService.start();
        }
        return  response;
    }

    private ClientSpkResponse updateSpkDoneApproved(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {

        ClientSpkResponse response = new ClientSpkResponse();

        if(spk.getStatus() == SpkStatus.CANCELED){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_CANCELED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_CANCELED.getMessage());
            response.setSuratPerintahKerja(spk);
        }else if(spk.getStatus() == SpkStatus.REJECTED){
            response.setResponseCode(ClientResponseStatus.SPK_ALREADY_REJECTED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ALREADY_REJECTED.getMessage());
            response.setSuratPerintahKerja(spk);
        }else{
            boolean isAprOnce = isApprovOnce(spkHistory);
            if(isAprOnce){
                response.setResponseCode(ClientResponseStatus.SPK_HAS_APPROVED.getCode());
                response.setResponseMessage(ClientResponseStatus.SPK_HAS_APPROVED.getMessage());
                response.setSuratPerintahKerja(spk);
                return  response;
            }

            response = updateSpkStatusToDb(spkHistory, spk);

            // update for send email to admin / operator to create PO
            Thread dapprovedSpkMailService = new Thread(
                    new DoneApprovedSpkMailService(appUserRepository, userRoleRepository, approvalRulesService, appProperty, textFormatter, emailService, spk, spkHistory.getUserName(), vendorRepository, storeRepository,sbuRepository, spkNextRoleRepository)
            );
            dapprovedSpkMailService.start();
        }
        return  response;
    }

    private ClientSpkResponse updateSpkToRequestClose(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk){
        ClientSpkResponse response = new ClientSpkResponse();

        //status should PURCHASE_ORDER_CREATED or NEED_TO_CLOSE to make request close
        if(!(spk.getStatus() == SpkStatus.PURCHASE_ORDER_CREATED || spk.getStatus() == SpkStatus.NEED_TO_CLOSE || spk.getStatus() == SpkStatus.GR1 || spk.getStatus() == SpkStatus.GR2 || spk.getStatus() == SpkStatus.GR3 || spk.getStatus() == SpkStatus.GR4 || spk.getStatus() == SpkStatus.GR5)){
            response.setResponseCode(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getCode());
            response.setResponseMessage(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getMessage());
            response.setSuratPerintahKerja(spk);

            return response;
        }

        //closed amount should equals to total GR
        if(!spkHistory.getActualAmount().equals(spk.getTotalGr())){
            response.setResponseCode(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getCode());
            response.setResponseMessage(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getMessage() +" Actual amount should equal to " + textFormatter.toRupiahFormat(spk.getTotalGr()));
            response.setSuratPerintahKerja(spk);

            return response;
        }

        try {
            spkRepository.updateStatusAndActualAmount(spkHistory.getSpkId(), spkHistory.getStatus().name(), spkHistory.getSpkNote(), spkHistory.getUserName(), spkHistory.getActualAmount());

            spk.setStatus(spkHistory.getStatus());
            spk.setActualAmount(spkHistory.getActualAmount());
            response.setResponseCode(ClientResponseStatus.SPK_STATUS_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_STATUS_UPDATED.getMessage());
            response.setSuratPerintahKerja(spk);

            List<ApprovalRulesEntity> rules = approvalRulesService.getRulesBySpk(spk, PoType.CLOSING.name());
            ApprovalRulesEntity nextRule = rules.get(0);

            //update spk next approval
            spkNextRoleRepository.update(spkHistory.getSpkId(), nextRule.getRoleId());

            //send request close SPK email notification
            Thread ReqClsapprovedSpkMailService = new Thread(
                    new RequestCloseSpkMailService(
                            spk,
                            vendorRepository,
                            storeRepository,
                            appUserRepository,
                            appProperty,
                            spkHistory.getUserName(),
                            textFormatter,
                            spkHistory.getSpkNote(),
                            emailService,
                            nextRule));
            ReqClsapprovedSpkMailService.start();

            return  response;
        }
        catch (ClassNotFoundException | SQLException e) {
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + ". " + e.getMessage());
            response.setSuratPerintahKerja(null);

            return  response;
        }
    }

    private ClientSpkResponse updateSpkAssetClassId(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {
        ClientSpkResponse response = new ClientSpkResponse();

        try {
            spkRepository.updateAssetClassId(spkHistory.getSpkId(), spkHistory.getAssetClassId(), spkHistory.getSpkNote(), spkHistory.getUserName(), spkHistory.getStatus().name());
            spk.setAssetClassId(spkHistory.getAssetClassId());

            response.setResponseCode(ClientResponseStatus.SPK_ASSET_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_ASSET_UPDATED.getMessage());
            response.setSuratPerintahKerja(spk);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + ". " + e.getMessage());
            response.setSuratPerintahKerja(null);
        }

        return response;
    }

    private ClientSpkResponse updateSpkToCloseApproved(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk)
    {
        ClientSpkResponse response = new ClientSpkResponse();
        AppUserEntity userApprover = appUserRepository.findByUsername(spkHistory.getUserName());

        if(spk.getStatus() == SpkStatus.CLOSE_REQUEST || spk.getStatus() == SpkStatus.CLOSE_APPROVED)
        {
            boolean isAprOnce = isApprovOnce(spkHistory);
            if(isAprOnce){
                response.setResponseCode(ClientResponseStatus.SPK_HAS_APPROVED.getCode());
                response.setResponseMessage(ClientResponseStatus.SPK_HAS_APPROVED.getMessage());
                response.setSuratPerintahKerja(spk);
                return  response;
            }

            response = updateSpkStatusToDb(spkHistory, spk);
            AssetClassEntity assetClass = assetClassRepository.findById(spk.getAssetClassId());
            List<ApprovalRulesEntity> closeRules = approvalRulesService.getRulesBySpk(spk, PoType.CLOSING.name());
            List<UserRoleEntity> userRoles = userRoleRepository.findByUser(spkHistory.getUserName());

            //close approved completed, but need to make API request to SAP
            if(approvalRulesService.isClosed(spkHistory.getUserName(), spk, assetClass.getAssetTypeId(),
                    closeRules, userRoles))
            {
                //make api request close Internal Order
                response = internalOrderService.closeInternalOrder(spkHistory, spk);

                if(response.getResponseCode() == 1008)
                {
                    response.setResponseCode(ClientResponseStatus.SPK_STATUS_UPDATED.getCode());
                    response.setResponseMessage(ClientResponseStatus.SPK_STATUS_UPDATED.getMessage());
                }
                else
                {
                    //send email notification to operation
                    Thread closeSpkFailedMailService = new Thread(
                            new SpkCloseFailedMailService(
                                    spk,
                                    userApprover,
                                    vendorRepository,
                                    storeRepository,
                                    appUserRepository,
                                    appProperty,
                                    textFormatter,
                                    spkHistoryRepository,
                                    emailService
                            ));
                    closeSpkFailedMailService.start();

                }

            }
            //close approved not completed yet.
            else
            {
                //send approved email notification to next user.
                //send request close SPK email notification
                Thread approvedCloseSpkMailService = new Thread(
                        new CloseApprovedSpkMailService(
                                closeRules,
                                userApprover,
                                appUserRepository,
                                spk,
                                appProperty,
                                textFormatter,
                                vendorRepository,
                                storeRepository,
                                emailService,
                                userRoles.get(0),
                                spkHistory.getSpkNote(),
                                spkNextRoleRepository
                        ));
                approvedCloseSpkMailService.start();
            }
        }
        else
        {
            response.setResponseCode(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getCode());
            response.setResponseMessage(ClientResponseStatus.REQUEST_CLOSE_UNABLE.getMessage());
            response.setSuratPerintahKerja(spk);
        }
        return response;
    }

    private ClientSpkResponse updateSpkStatusToDb(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {
        ClientSpkResponse response = new ClientSpkResponse();

        try {
            //update spk status
            spkRepository.updateStatus(spkHistory.getSpkId(), spkHistory.getStatus().name(), spkHistory.getSpkNote(), spkHistory.getUserName());
            spk.setStatus(spkHistory.getStatus());

            response.setResponseCode(ClientResponseStatus.SPK_STATUS_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_STATUS_UPDATED.getMessage());
            response.setSuratPerintahKerja(spk);

        } catch (SQLException | ClassNotFoundException se) {
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + ". " + se.getMessage());
            response.setSuratPerintahKerja(null);
        }

        return response;
    }

    private ClientSpkResponse updateSpkStatusAndAssetNoToDb(SpkHistoryEntity spkHistory, SuratPerintahKerjaEntity spk) {
        ClientSpkResponse response = new ClientSpkResponse();

        try {
            //update spk status
            spkRepository.updateStatusAndAssetNo(spkHistory.getSpkId(), spkHistory.getStatus().name(), spkHistory.getAssetNo(), spkHistory.getSpkNote(), spkHistory.getUserName());
            spk.setStatus(spkHistory.getStatus());

            response.setResponseCode(ClientResponseStatus.SPK_STATUS_UPDATED.getCode());
            response.setResponseMessage(ClientResponseStatus.SPK_STATUS_UPDATED.getMessage());
            response.setSuratPerintahKerja(spk);

        } catch (SQLException | ClassNotFoundException se) {
            response.setResponseCode(ClientResponseStatus.SERVER_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SERVER_ERROR.getMessage() + ". " + se.getMessage());
            response.setSuratPerintahKerja(null);
        }

        return response;
    }

    /**
     * @param storeId
     * @param vendorId
     * @param agreementDate prefix should yyyy
     * @return
     */
    private String generateSpkId(String storeId, String vendorId, String agreementDate){

        int spkNum = ++SpkMonthlyCounter.numOfMonthlySpk;
        SpkMonthlyCounter.numOfMonthlySpk =spkNum;

        return "P"+ String.format("%04d", spkNum) + "-" + storeId.substring(0,2) + "-" + vendorId + "-"+ agreementDate.substring(5, 7) +"-" + agreementDate.substring(2,4);
    }

    private boolean isApprovOnce(SpkHistoryEntity spkHistory)
    {
        boolean result = false;

        //Search collection data in table SPK History by Status in 1 SPK
        List<SpkHistoryEntity> listspkHist = spkHistoryRepository.findBySpkId(spkHistory.getSpkId())
                .stream()
                .filter(r -> r.getStatus() == spkHistory.getStatus())
                .collect(Collectors.toList());

        int sizeSpkHist = listspkHist.size();

        //Search size collection in table SPK History by Status = Updated in 1 SPK
        int sizeUpd = spkHistoryRepository.findBySpkId(spkHistory.getSpkId())
                .stream()
                .filter(r -> r.getStatus() == SpkStatus.UPDATED)
                .collect(Collectors.toList()).size();

        if(sizeUpd > 0)
        {
            //Search size collection in table SPK History by Status + by Username in 1 SPK
            int sizehistUsr = listspkHist
                    .stream()
                    .filter(r -> r.getUserName().equals(spkHistory.getUserName()))
                    .collect(Collectors.toList()).size();

            if(sizehistUsr - sizeUpd > 0)
            {
                result = true;
            }
            else if(spkHistory.getStatus() == SpkStatus.CLOSE_APPROVED || spkHistory.getStatus() == SpkStatus.DONE_APPROVED)
            {
                result = (sizehistUsr == 0) ? true : false;
            }
            else
            {
                //Check Role from user
                UserRoleEntity RoleApprover = userRoleRepository.findByUser(spkHistory.getUserName()).get(0);
                //Get list UserRole which is same with Approver
                List<UserRoleEntity> listUser = userRoleRepository.findByRoleId(RoleApprover.getRoleId())
                        .stream()
                        .filter(r -> r.getUserName().equals(spkHistory.getUserName()) == false)
                        .collect(Collectors.toList());

                for (int i = 0; i < listUser.size(); i++)
                {
                    UserRoleEntity usr = listUser.get(i);

                    //Search collection data in table SPK History by Status + by Username which has same role with Approver in 1 SPK
                    List<SpkHistoryEntity> listspkF = listspkHist
                            .stream()
                            .filter(r -> r.getUserName().equals(usr.getUserName()))
                            .collect(Collectors.toList());

                    if(sizehistUsr + listspkF.size() - sizeUpd > 0)
                    {
                        result = true;
                        break;
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < sizeSpkHist; i++)
            {
                SpkHistoryEntity spkHist = listspkHist.get(i);

                if (spkHist.getUserName().equals(spkHistory.getUserName()))
                {
                    result = true;
                    break;
                }
                else
                {
                    UserRoleEntity RoleApprover = userRoleRepository.findByUser(spkHistory.getUserName()).get(0);
                    UserRoleEntity RoleHistory = userRoleRepository.findByUser(spkHist.getUserName()).get(0);

                    if (RoleApprover.getRoleId() == RoleHistory.getRoleId())
                    {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

}
