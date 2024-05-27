package id.co.map.spk.services;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.SpkHistoryEntity;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.entities.UserRoleEntity;
import id.co.map.spk.enums.PoType;
import id.co.map.spk.model.AppRules;
import id.co.map.spk.model.response.ClientApprovalResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Awie on 8/27/2019
 */
public interface ApprovalRulesService {

    List<ApprovalRulesEntity> getRulesBySpk(SuratPerintahKerjaEntity spk, String poType);
    String getSpkAction(String userName, SuratPerintahKerjaEntity spk, List<SpkHistoryEntity> histories, String assetTypeId, PoType poType);
    String getSpkCloseAction(String username, SuratPerintahKerjaEntity spk, List<SpkHistoryEntity> histories, String assetTypeId);

    /**
     * Check spk to completely closed when user click approved closed
     * @param userName
     * @param spk
     * @param assetTypeId
     * @return
     */
    boolean isClosed(String userName, SuratPerintahKerjaEntity spk, String assetTypeId, List<ApprovalRulesEntity> closeRules, List<UserRoleEntity> userRoles);
    ClientApprovalResponse add(AppRules newrule);
    ClientApprovalResponse delete(String rule);
    ClientApprovalResponse update(AppRules apprule);
    Map<String, Object> findAppRulePagination(String approvalRulesId, String assetTypeId,String poType,
                                              String approvalLevel,String roleId, String minAmount,
                                              String maxAmount, Integer pageNumber, Integer size, Integer draw);
    List<ApprovalRulesEntity> findDistinct();
}
