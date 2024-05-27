package id.co.map.spk.controllers;

import id.co.map.spk.repositories.ApprovalRulesRepository;
import id.co.map.spk.repositories.CountryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Rommy 06/12/2023
 */
@Controller
@RequestMapping(CountriesController.BASE_URL)
public class CountriesController {
  public static final String BASE_URL = "countries";
  private final ApprovalRulesRepository approvalRulesRepository;
  private final CountryRepository countryRepository;

  public CountriesController(ApprovalRulesRepository approvalRulesRepository, CountryRepository countryRepository) {
    this.approvalRulesRepository = approvalRulesRepository;
    this.countryRepository = countryRepository;
  }

  @GetMapping({"", "/"})
  public ModelAndView showListOfCountries(ModelAndView m){
    m.setViewName("countries/list-of-countries");
    return m;
  }

  @GetMapping({"/create-new-country"})
  public ModelAndView showCreateNewCountry(ModelAndView m){

    m.addObject("approvalRules", approvalRulesRepository.findAllDistinctRules());
    m.setViewName("countries/create-new-country");

    return m;
  }

  @GetMapping("/{countryId}/")
  public ModelAndView showCountryDetail(ModelAndView m, @PathVariable("countryId") Integer countryId){

    m.addObject("countries", countryRepository.findbyId(countryId));
    m.setViewName("countries/country-details");
    return m;
  }
}
