package id.co.map.spk.api;

import id.co.map.spk.entities.ApprovalRulesEntity;
import id.co.map.spk.entities.CountryEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientCountryResponse;
import id.co.map.spk.repositories.CountryRepository;
import id.co.map.spk.services.CountryService;
import id.co.map.spk.utils.AppProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Rommy on 06/12/2023
 */
@RestController
@RequestMapping(CountriesApi.BASE_URL)
public class CountriesApi {
  static final String BASE_URL = "api/countries";
  private final CountryService countryService;
  private final AppProperty appProperty;
  private final CountryRepository countryRepository;
  private static final Logger logger = LoggerFactory.getLogger(CompaniesApi.class);

  public CountriesApi(CountryService countryService, AppProperty appProperty, CountryRepository countryRepository) {
    this.countryService = countryService;
    this.appProperty = appProperty;
    this.countryRepository = countryRepository;
  }

  @GetMapping("")
  public Map<String, Object> findCountryPagination(
      @RequestParam(name ="countryCode", required = false) String countryCode,
      @RequestParam(name ="countryName", required = false) String countryName,
      @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
      @RequestParam(name = "pageNumber") Integer pageNumber
  ){
    Map<String,Object>asd = countryService.findCountryPagination(countryCode, countryName, pageNumber, 10, draw);
    return asd;
  }

  @DeleteMapping("/{countryId}/")
  public ClientCountryResponse deleteCountry(@PathVariable("countryId") String countryId){
    return countryService.delete(countryId);
  }

  @PostMapping("")
  public ClientCountryResponse addNewCountry(@RequestBody CountryEntity country)
  {
    return countryService.add(country);
  }

  @PatchMapping("/{countryId}/countrycode")
  public ClientCountryResponse changeCountryCode(@RequestBody Map<String, String> map, @PathVariable("countryId") Integer countryId){
    String countryCode = map.get("countryCode");
    return countryService.updateCountryCode(countryCode, countryId);
  }

  @PatchMapping("/{countryId}/countryname")
  public ClientCountryResponse changeCountryName(@RequestBody Map<String, String> map, @PathVariable("countryId") Integer countryId){
    String countryName = map.get("countryName");
    return countryService.updateCountryName(countryName, countryId);
  }

  @PatchMapping("/{countryId}/{labelName}")
  public ClientCountryResponse changeLabel(@RequestBody Map<String, String> map, @PathVariable("countryId") Integer countryId,@PathVariable("labelName") String labelName){
    String label = map.get(labelName);
    return countryService.updateLabel(label,labelName, countryId);
  }

  @GetMapping("/dist/")
  public List<CountryEntity> getAllCountryList(){
    return countryRepository.findAllDistinctRules();
  }
}
