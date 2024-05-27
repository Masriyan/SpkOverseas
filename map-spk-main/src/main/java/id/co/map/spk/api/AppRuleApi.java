package id.co.map.spk.api;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.LvlRoleEntity;
import id.co.map.spk.entities.SbuEntity;
import id.co.map.spk.model.AppRules;
import id.co.map.spk.model.response.ClientApprovalResponse;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.services.ApprovalRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AppRuleApi.BASE_URL)
public class AppRuleApi {

    static final String BASE_URL = "api/rule";
    private static final Logger logger = LoggerFactory.getLogger(AppRuleApi.class);
    private final ApprovalRulesService appRuleService;

    public AppRuleApi(ApprovalRulesService appRuleService) {
        this.appRuleService = appRuleService;
    }

    @PostMapping("")
    public ClientApprovalResponse addNewRule(@RequestBody AppRules apprule){
        /*for (LvlRoleEntity levelRole: apprule.getAppruleList()) {
            logger.debug("List Level Api = " + levelRole.getApprovalLevel());
            logger.debug("List Role Api = " + levelRole.getRoleId());
        }*/
        return appRuleService.add(apprule);
    }

    @GetMapping("")
    public Map<String, Object> findRulesPagination(
            @RequestParam(name ="approvalRulesId", required = false) String approvalRulesId,
            @RequestParam(name ="assetTypeId", required = false) String assetTypeId,
            @RequestParam(name ="poType", required = false) String poType,
            @RequestParam(name ="approvalLevel", required = false) String approvalLevel,
            @RequestParam(name ="roleId", required = false) String roleId,
            @RequestParam(name ="minAmount", required = false) String minAmount,
            @RequestParam(name ="maxAmount", required = false) String maxAmount,
            @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
            @RequestParam(name = "pageNumber") Integer pageNumber
    ){
        return appRuleService.findAppRulePagination(approvalRulesId, assetTypeId,poType, approvalLevel,
                roleId, minAmount,maxAmount,pageNumber, 10, draw);
    }

    @GetMapping("/dist/")
    public List<ApprovalRulesEntity> findRulesDistinct(){
        return appRuleService.findDistinct();
    }

    @DeleteMapping("/{rule}/")
    public ClientApprovalResponse deleteRule(@PathVariable("rule") String rule)
    {
        return appRuleService.delete(rule);
    }

    @PutMapping("")
    public ClientApprovalResponse updateNewRule(@RequestBody AppRules apprule){
        return appRuleService.update(apprule);
    }

}
