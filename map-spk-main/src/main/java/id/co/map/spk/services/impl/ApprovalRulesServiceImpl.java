package id.co.map.spk.services.impl;

import id.co.map.spk.emails.NewUserMailService;
import id.co.map.spk.entities.*;
import id.co.map.spk.enums.ClientResponseStatus;
import id.co.map.spk.enums.PoType;
import id.co.map.spk.enums.SpkStatus;
import id.co.map.spk.model.AppRules;
import id.co.map.spk.model.response.ClientApprovalResponse;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.repositories.AssetClassRepository;
import id.co.map.spk.repositories.CompanyRepository;
import id.co.map.spk.repositories.UserRoleRepository;
import id.co.map.spk.repositories.impl.SpkRepositoryImpl;
import id.co.map.spk.services.ApprovalRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Milton on 2023-03-08
 */
@Service
public class ApprovalRulesServiceImpl implements ApprovalRulesService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalRulesServiceImpl.class);
    private final ApprovalRulesRepository approvalRulesRepository;
    private final UserRoleRepository userRoleRepository;
    private final AssetClassRepository assetClassRepository;
    private final CompanyRepository companyRepository;

    public ApprovalRulesServiceImpl(ApprovalRulesRepository approvalRulesRepository,
                                    UserRoleRepository userRoleRepository,
                                    AssetClassRepository assetClassRepository,
                                    CompanyRepository companyRepository) {
        this.approvalRulesRepository = approvalRulesRepository;
        this.userRoleRepository = userRoleRepository;
        this.assetClassRepository = assetClassRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<ApprovalRulesEntity> getRulesBySpk(SuratPerintahKerjaEntity spk, String poType) {

        AssetClassEntity assetClass = assetClassRepository.findById(spk.getAssetClassId());
        CompanyEntity company = companyRepository.findByCompanyId(spk.getCompanyId());

        return approvalRulesRepository.findBySbuIdCountryId(spk.getSbuId(), company.getCountryId())
                .stream()
                .filter(r -> spk.getAmount() >= r.getMinAmount()  && spk.getAmount() <= r.getMaxAmount()
                        && r.getAssetTypeId().equals(assetClass.getAssetTypeId())
                        && r.getPoType().equals(poType)
                )

                .collect(Collectors.toList());
    }

    @Override
    public String getSpkAction(String userName, SuratPerintahKerjaEntity spk, List<SpkHistoryEntity> histories, String assetTypeId, PoType poType){

        String res = "";
        List<ApprovalRulesEntity> spkRules = getRulesBySpk(spk, poType.name());
        List<UserRoleEntity> userRoles = userRoleRepository.findByUser(userName);
        long numOfApproved;
        long totalApproval = spkRules.size();

        logger.debug("=========================== SPK STATUS = "+ spk.getStatus() +" ===========================");
        logger.debug("=========================== Logger Approval ===========================");
        if(spk.getStatus() == SpkStatus.REJECTED || spk.getStatus() == SpkStatus.CANCELED){
            res =  "No Action";
        }
        else
        {
            numOfApproved = histories.stream()
                    .filter(h -> h.getStatus() == SpkStatus.APPROVED || h.getStatus() == SpkStatus.VERIFIED || h.getStatus() == SpkStatus.DONE_APPROVED)
                    .count();

            if(totalApproval > numOfApproved)
            {

                if(spk.getSpkType().equalsIgnoreCase("Expense")){

                    if (totalApproval >= numOfApproved + 2) {

                        logger.debug("=========================== Logger Expense Approval ===========================");
                        logger.debug("tot approve       : " + totalApproval);
                        logger.debug("numb approve       : " + numOfApproved);

                        ApprovalRulesEntity rules = spkRules.stream()
                            .filter(r -> r.getApprovalLevel().equals((int) (numOfApproved + 2))) // level saat ini + 1
                            .findFirst()
                            .get();

                        ApprovalRulesEntity rule = spkRules.stream()
                            .filter(r -> r.getApprovalLevel().equals((int) (numOfApproved + 1))) // level saat ini
                            .findFirst()
                            .get();

                        logger.debug("rules roleid       : " + rules.getRoleId());
                        logger.debug("rule roleid       : " + rule.getRoleId());

                        if (rules.getRoleId() == 12) {
                            if (userRoles.stream().anyMatch(ur -> ur.getRoleId().equals(rule.getRoleId())))
                               res = "DoneApproveOrReject";
                        } else {
                            if (userRoles.stream().anyMatch(ur -> ur.getRoleId().equals(rule.getRoleId()))) {
                                res = "ApproveOrReject";
                            } else {
                                res = "No Action";
                            }
                        }
                    }
                   logger.debug("action       : " + res);
                   logger.debug("=========================== Logger Expense Approval ===========================");

                }else {

                    logger.debug("=========================== Logger Asset Approval ===========================");
                    logger.debug("tot approve       : " + totalApproval);
                    logger.debug("numb approve       : " + numOfApproved);

                    ApprovalRulesEntity rule = spkRules.stream()
                            .filter(r -> r.getApprovalLevel().equals((int) (numOfApproved + 1))) // level saat ini
                            .findFirst()
                            .get();

                    logger.debug("rule roleid       : " + rule.getRoleId());

                    //other Roles
                    if (rule.getRoleId() != 12) {
                        if (userRoles.stream().anyMatch(ur -> ur.getRoleId().equals(rule.getRoleId())))
                            res = "ApproveOrReject";
                    }
                    //Role Accountant
                    else {
                        if (userRoles.stream().anyMatch(ur -> ur.getRoleId().equals(rule.getRoleId()))) {
                            res = "VerifyOrReject";
                        }else{
                            res = "No Action";
                        }
                    }

                    logger.debug("action       : " + res);
                    logger.debug("=========================== Logger Asset Approval ===========================");
                }
            }
            else
            {
                res = "No Action";
            }
        }
        logger.debug("final action       : " + res);
        logger.debug("=========================== Logger Approval ===========================");
        return res;
    }

    @Override
    public String getSpkCloseAction(String username, SuratPerintahKerjaEntity spk, List<SpkHistoryEntity> histories, String assetTypeId) {

        List<ApprovalRulesEntity> closeRules = getRulesBySpk(spk, PoType.CLOSING.name());
        List<UserRoleEntity> userRoles = userRoleRepository.findByUser(username);
        long numOfApproved;
        long totalApproval = closeRules.size();

        if(spk.getStatus() == SpkStatus.CLOSE_REQUEST || spk.getStatus() == SpkStatus.CLOSE_APPROVED){

            numOfApproved = histories.stream()
                    .filter(h -> h.getStatus() == SpkStatus.CLOSE_APPROVED)
                    .count();

            if(totalApproval > numOfApproved){

                ApprovalRulesEntity rule = closeRules.stream()
                        .filter(r -> r.getApprovalLevel().equals((int) (numOfApproved + 1)))
                        .findFirst()
                        .get();

                if(userRoles.stream().anyMatch(ur -> ur.getRoleId().equals(rule.getRoleId()))) return "CloseApprove";
            }
            return "No Action";
        }else{
            return "No Action";
        }
    }

    @Override
    public boolean isClosed(String userName, SuratPerintahKerjaEntity spk, String assetTypeId,
                            List<ApprovalRulesEntity> closeRules, List<UserRoleEntity> userRoles) {

        ApprovalRulesEntity currentApprovedRule = new ApprovalRulesEntity();

        for (ApprovalRulesEntity rule: closeRules){
            for (UserRoleEntity userRole : userRoles){
                if(rule.getRoleId().equals(userRole.getRoleId())) currentApprovedRule = rule;
            }
        }
        logger.debug("** INDEX OF = "+closeRules.indexOf(currentApprovedRule)+"===========================");

        int x = closeRules.indexOf(currentApprovedRule) + 1;
        logger.debug("** INDEX OF +1 (X) = "+x+"===========================");
        logger.debug("** CLOSE Rule Size = "+closeRules.size()+"===========================");

        return x == closeRules.size();
    }

    @Override
    public ClientApprovalResponse add(AppRules newrule) {

        ClientApprovalResponse response = new ClientApprovalResponse();
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        newrule.setDataTimeStamp(currentDateTime);

        try {
            /*for (LvlRoleEntity levelRole: newrule.getAppruleList()) {
                logger.debug("List Level Serv = " + levelRole.getApprovalLevel());
                logger.debug("List Role Serv = " + levelRole.getRoleId());
            }*/
            approvalRulesRepository.add(newrule);

            response.setResponseCode(ClientResponseStatus.APPROVAL_RULE_ADDED.getCode());
            response.setResponseMessage(ClientResponseStatus.APPROVAL_RULE_ADDED.getMessage());
            response.setRule(newrule);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("============================== Error Add New Approval Rule ==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("============================== Error Add New Approval Rule ==============================");

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
        }

        return response;
    }

    @Override
    public ClientApprovalResponse delete(String rule) {
        ClientApprovalResponse response = new ClientApprovalResponse();
        try {
            approvalRulesRepository.delete(rule);
            response.setResponseCode(ClientResponseStatus.ROLE_DELETED.getCode());
            response.setResponseMessage(ClientResponseStatus.ROLE_DELETED.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage());
            response.setRule(null);
        }
        return response;
    }

    @Override
    public ClientApprovalResponse update(AppRules apprule) {

        ClientApprovalResponse response = new ClientApprovalResponse();
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apprule.setDataTimeStamp(currentDateTime);

        try {
            approvalRulesRepository.update(apprule);

            response.setResponseCode(ClientResponseStatus.APPROVAL_RULE_UPDATE.getCode());
            response.setResponseMessage(ClientResponseStatus.APPROVAL_RULE_UPDATE.getMessage());
            response.setRule(apprule);

        } catch (ClassNotFoundException | SQLException e) {
            logger.error("============================== Error Update New Approval Rule ==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("============================== Error Update New Approval Rule ==============================");

            response.setResponseCode(ClientResponseStatus.SQL_ERROR.getCode());
            response.setResponseMessage(ClientResponseStatus.SQL_ERROR.getMessage() + " " +e.getMessage());
        }

        return response;

    }

    @Override
    public Map<String, Object> findAppRulePagination(String approvalRulesId, String assetTypeId,String poType,
                                                     String approvalLevel,String roleId, String minAmount,
                                                     String maxAmount, Integer pageNumber, Integer size, Integer draw) {
        Map<String, Object> findResult = approvalRulesRepository.findAppRuleForPagination(approvalRulesId, assetTypeId,poType,
                approvalLevel,roleId, minAmount,maxAmount, pageNumber, size, draw);

        Map<String, Object> map = new HashMap<>();
        map.put("draw", draw);
        map.put("recordsTotal", findResult.get("totalCount"));
        map.put("recordsFiltered", findResult.get("totalCount"));
        map.put("data", findResult.get("apprules"));

        return map;
    }

    @Override
    public List<ApprovalRulesEntity> findDistinct(){
        return approvalRulesRepository.findAllDistinctRules();
    }
}
