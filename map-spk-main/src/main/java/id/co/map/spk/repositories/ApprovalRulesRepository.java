package id.co.map.spk.repositories;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.model.AppRules;
import id.co.map.spk.model.AppUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Awie on 8/26/2019
 */
public interface ApprovalRulesRepository {

    List<ApprovalRulesEntity> findById(String approvalRulesId);
    List<ApprovalRulesEntity> findByCompanyId(String companyId);
    List<ApprovalRulesEntity> findBySbuId(String sbuId);
    List<ApprovalRulesEntity> findBySbuIdCountryId(String sbuId, String countryId);
    /**
     * show list of distinct rules
     * @return
     */
    List<ApprovalRulesEntity> findAllDistinctRules();
    ApprovalRulesEntity findLasRuleId();
    void add(AppRules newrule) throws ClassNotFoundException, SQLException;
    void delete(String rule) throws ClassNotFoundException, SQLException;
    void update(AppRules apprule) throws ClassNotFoundException, SQLException;
    ApprovalRulesEntity getObject(String rule);
    Map<String, Object> findAppRuleForPagination(String approvalRulesId, String assetTypeId,String poType,
                                                 String approvalLevel,String roleId, String minAmount,
                                                 String maxAmount, Integer pageNumber, Integer size, Integer draw);



}
