package id.co.map.spk.controllers;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.repositories.AssetClassRepository;
import id.co.map.spk.repositories.CountryRepository;
import id.co.map.spk.repositories.RoleRepository;
import id.co.map.spk.utils.TextFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(AppRuleController.BASE_URL)
public class AppRuleController {

    public static final String BASE_URL = "rules";

    private final AssetClassRepository assetRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final ApprovalRulesRepository approvalRulesRepository;

    public AppRuleController(AssetClassRepository assetRepository, RoleRepository roleRepository, CountryRepository countryRepository, ApprovalRulesRepository approvalRulesRepository) {
        this.assetRepository = assetRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.approvalRulesRepository = approvalRulesRepository;
    }

    @GetMapping({"", "/"})
    public ModelAndView showListOfSbus(ModelAndView m){

        m.addObject("rules", approvalRulesRepository.findAllDistinctRules());
        m.addObject("roles", roleRepository.findAll());
        m.setViewName("apprules/list-of-rules");

        return m;
    }

    @GetMapping({"/create-new-app-rule"})
    public ModelAndView showCreateNewSbu(ModelAndView m){

        ApprovalRulesEntity lastRule = approvalRulesRepository.findLasRuleId();
        String newruleId = "RULE0001";

        if(!lastRule.equals(null)) {
            String ruleId = lastRule.getApprovalRulesId();
            int newid = Integer.parseInt(ruleId.substring(4)) + 1;
            newruleId = "RULE" + TextFormatter.padZeroLeft(newid, 4);
        }
        m.addObject("countryList", countryRepository.findAllDistinctRules());
        m.addObject("roles", roleRepository.findAll());
        m.addObject("rules", approvalRulesRepository.findAllDistinctRules());
        m.addObject("NewRuleId", newruleId);
        m.setViewName("apprules/create-new-app-rule");

        return m;
    }

    @GetMapping("/{rule}/")
    public ModelAndView showUserDetail(ModelAndView m, @PathVariable("rule") String rule){

        m.addObject("rule", approvalRulesRepository.getObject(rule));
        m.addObject("roles", roleRepository.findAll());
        m.setViewName("apprules/rule-updates");
        return m;
    }

}
