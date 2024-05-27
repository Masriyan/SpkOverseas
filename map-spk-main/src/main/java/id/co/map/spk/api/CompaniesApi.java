package id.co.map.spk.api;

import id.co.map.spk.entities.CompanyEntity;
import id.co.map.spk.model.AppCompany;
import id.co.map.spk.model.response.ClientCompanyResponse;
import id.co.map.spk.model.response.ClientUserResponse;
import id.co.map.spk.repositories.CompanyRepository;
import id.co.map.spk.repositories.impl.CompanyRepositoryImpl;
import id.co.map.spk.services.CompanyService;
import id.co.map.spk.utils.AppProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Awie on 11/3/2019
 * @Modified Bartholomeus 1/9/2020
 */
@RestController
@RequestMapping(CompaniesApi.BASE_URL)
class CompaniesApi {

    static final String BASE_URL = "api/companies";

    private final CompanyService companyService;
    private final AppProperty appProperty;
    private final CompanyRepository companyRepository;
    private static final Logger logger = LoggerFactory.getLogger(CompaniesApi.class);

    public CompaniesApi(CompanyService companyService, AppProperty appProperty, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.appProperty = appProperty;
        this.companyRepository = companyRepository;
    }

    @PostMapping("")
    //public ClientCompanyResponse addNewCompany(@RequestBody CompanyEntity company){
    public ClientCompanyResponse addNewCompany(@RequestBody AppCompany company)
    {

        return companyService.add(company);
    }

    @PostMapping("/letter-head")
    public ResponseEntity<?> uploadSpkQuotation(@RequestParam("letterHead") MultipartFile letterHead){

        String savedFileName, extension;
        String[] fileFrags;

        if (letterHead.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {
            fileFrags = letterHead.getOriginalFilename().split("\\.");
            extension = fileFrags[fileFrags.length-1];

            savedFileName = UUID.randomUUID().toString() + "." + extension;

            // Get the file and save it somewhere
            byte[] bytes = letterHead.getBytes();
            Path path = Paths.get(appProperty.getCompHeaderImagePath() + savedFileName);
            Files.write(path, bytes);

            return new ResponseEntity(savedFileName, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException e) {

            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public Map<String, Object> findCompaniesPagination(
            @RequestParam(name ="companyId", required = false) String companyId,
            @RequestParam(name ="companyName", required = false) String companyName,
            @RequestParam(name ="sbuId", required = false) String sbuId,
            @RequestParam(name = "draw") Integer draw, //required to avoid paging bugs!
            @RequestParam(name = "pageNumber") Integer pageNumber
    ){
        return companyService.findUserPagination(companyId, companyName,sbuId, pageNumber, 10, draw);
    }

    @GetMapping("/all")
    public List<CompanyEntity> getAllCompanies(){
        return companyRepository.findAll();
    }

    @DeleteMapping("/{sbuDelete}/")
    public ClientCompanyResponse deleteUser(@PathVariable("sbuDelete") String sbuDelete){
        return companyService.delete(sbuDelete);
    }

    @PatchMapping("/{companyId}/compname")
    public ClientCompanyResponse changeCompName(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        String compname = map.get("companyName");
        return companyService.updateCompName(compname, companyId);
    }
    @PatchMapping("/{companyId}/country")
    public ClientCompanyResponse changeCountry(@RequestBody Map<String, Integer> map, @PathVariable("companyId") String companyId){
        Integer countryId = map.get("countryId");
        return companyService.updateCountry(countryId, companyId);
    }

    @PatchMapping("/{companyId}/npwp")
    public ClientCompanyResponse changeNpwp(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        String npwp = map.get("npwp");
        return companyService.updateNpwp(npwp, companyId);
    }

    @PatchMapping("/{companyId}/add1")
    public ClientCompanyResponse changeAdd1(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        String add1 = map.get("address1");
        return companyService.updateAdd1(add1, companyId);
    }

    @PatchMapping("/{companyId}/add2")
    public ClientCompanyResponse changeAdd2(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        String add2 = map.get("address2");
        return companyService.updateAdd2(add2, companyId);
    }

    @PatchMapping("/{companyId}/coa")
    public ClientCompanyResponse changeCoa(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        String coa = map.get("coa");
        return companyService.updateCoa(coa, companyId);
    }

    @PatchMapping("/{companyId}/headimg")
    public ClientCompanyResponse changeHeadImg(@RequestBody Map<String, String> map, @PathVariable("companyId") String companyId){
        logger.debug("ENTER API HEADER LETTER");
        String headimg = map.get("headerImage");
        return companyService.updateHeadimg(headimg, companyId);
    }

}
