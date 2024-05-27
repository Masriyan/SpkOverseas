package id.co.map.spk.controllers;

import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.repositories.CompanyRepository;
import id.co.map.spk.repositories.CountryRepository;
import id.co.map.spk.repositories.SbuRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Awie on 11/1/2019
 */
@Controller
@RequestMapping(CompaniesController.BASE_URL)
class CompaniesController {

    public static final String BASE_URL = "companies";

    private final ApprovalRulesRepository approvalRulesRepository;

    private final CountryRepository countryRepository;
    private final CompanyRepository companyRepository;
    private final SbuRepository sbuRepository;

    public CompaniesController(ApprovalRulesRepository approvalRulesRepository, CountryRepository countryRepository, CompanyRepository companyRepository, SbuRepository sbuRepository) {
        this.approvalRulesRepository = approvalRulesRepository;
        this.countryRepository = countryRepository;
        this.companyRepository = companyRepository;
        this.sbuRepository = sbuRepository;
    }

    @GetMapping({"", "/"})
    public ModelAndView showListOfCompanies(ModelAndView m){

        m.addObject("sbus", sbuRepository.findAll());
        m.setViewName("companies/list-of-companies");

        return m;
    }

    @GetMapping({"/create-new-company"})
    public ModelAndView showCreateNewCompany(ModelAndView m){

        m.addObject("approvalRules", approvalRulesRepository.findAllDistinctRules());
        m.addObject("countryList", countryRepository.findAllDistinctRules());
        m.setViewName("companies/create-new-company");

        return m;
    }

    @GetMapping("/img/{headerImg}/")
    public ModelAndView showHeaderImg(ModelAndView m, @PathVariable("headerImg") String headerImg){

        m.addObject("imgheader", headerImg);
        m.setViewName("companies/header-images");
        return m;
    }

    @GetMapping("/{sbuDetails}/")
    public ModelAndView showUserDetail(ModelAndView m, @PathVariable("sbuDetails") String sbuDetails){

        String[] sbutext = sbuDetails.split(",");
        String compid  = sbutext[0];
        String sbuCode = sbutext[1];
        String sbuDesc = sbutext[2];

        m.addObject("companies", companyRepository.findByCompanyId(compid));
        m.addObject("sbus", sbuRepository.findbySbuCodeDesc(sbuCode,sbuDesc));
        m.addObject("countryList", countryRepository.findAllDistinctRules());
        m.setViewName("companies/company-details");
        return m;
    }

}
