package id.co.map.spk.controllers;

import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.repositories.CompanyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(SbuController.BASE_URL)
public class SbuController {

    public static final String BASE_URL = "sbus";

    private final CompanyRepository companyRepository;
    private final ApprovalRulesRepository approvalRulesRepository;

    public SbuController(CompanyRepository companyRepository, ApprovalRulesRepository approvalRulesRepository) {
        this.companyRepository = companyRepository;
        this.approvalRulesRepository = approvalRulesRepository;
    }

    @GetMapping({"", "/"})
    public String showListOfSbus(){

        return "sbus/list-of-sbus";
    }

    @GetMapping({"/create-new-sbu"})
    public ModelAndView showCreateNewSbu(ModelAndView m){

        m.addObject("companies", companyRepository.findAll());
        m.addObject("approvalRules", approvalRulesRepository.findAllDistinctRules());
        m.setViewName("sbus/create-new-sbu");

        return m;
    }

}
